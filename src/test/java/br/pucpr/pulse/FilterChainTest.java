package br.pucpr.pulse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.pucpr.pulse.filter.LevelFilter;
import br.pucpr.pulse.filter.LogFilter;
import br.pucpr.pulse.filter.LoggerNameFilter;
import br.pucpr.pulse.filter.RateLimitFilter;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FilterChainTest {

  private static LogEvent evento(LogLevel level, String logger) {
    return new LogEvent(Instant.now(), level, logger, "msg", Map.of(), null);
  }

  @Test
  void correACorrenteAteOFim() {
    final LogFilter corrente =
        new LevelFilter(LogLevel.INFO).then(new LoggerNameFilter("br.pucpr"));

    assertTrue(corrente.accept(evento(LogLevel.WARN, "br.pucpr.app")));
    assertFalse(corrente.accept(evento(LogLevel.DEBUG, "br.pucpr.app")));
    assertFalse(corrente.accept(evento(LogLevel.WARN, "com.outro.app")));
  }

  @Test
  void oPrimeiroQueNegaInterrompeACorrente() {
    final var contador = new ContadorFilter();
    final LogFilter corrente = new LevelFilter(LogLevel.ERROR).then(contador);

    corrente.accept(evento(LogLevel.INFO, "app"));

    assertEquals(0, contador.chamadas, "filtro seguinte nao deve ser consultado apos um DENY");
  }

  @Test
  void limitaAQuantidadeDeEventosPorJanela() {
    final LogFilter corrente = new RateLimitFilter(2, 60_000);

    assertTrue(corrente.accept(evento(LogLevel.INFO, "app")));
    assertTrue(corrente.accept(evento(LogLevel.INFO, "app")));
    assertFalse(corrente.accept(evento(LogLevel.INFO, "app")));
  }

  private static final class ContadorFilter extends LogFilter {
    private int chamadas;

    @Override
    protected Decision decide(LogEvent event) {
      chamadas++;
      return Decision.NEUTRAL;
    }
  }
}
