package br.pucpr.pulse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.pucpr.pulse.appender.AsyncAppender;
import br.pucpr.pulse.appender.BufferedAppender;
import br.pucpr.pulse.appender.CompositeAppender;
import br.pucpr.pulse.appender.MemoryAppender;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AppenderTest {

  private static LogEvent evento(String mensagem) {
    return new LogEvent(Instant.now(), LogLevel.INFO, "app", mensagem, Map.of(), null);
  }

  @Test
  void compositeEntregaParaTodosOsFilhos() {
    final var primeiro = new MemoryAppender();
    final var segundo = new MemoryAppender();
    final var composite = new CompositeAppender(primeiro, segundo);

    composite.append(evento("oi"));

    assertEquals(1, primeiro.getEvents().size());
    assertEquals(1, segundo.getEvents().size());
  }

  @Test
  void bufferedSoEntregaQuandoOLoteEnche() {
    final var memoria = new MemoryAppender();
    final var buffered = new BufferedAppender(memoria, 3);

    buffered.append(evento("1"));
    buffered.append(evento("2"));
    assertEquals(0, memoria.getEvents().size());

    buffered.append(evento("3"));
    assertEquals(3, memoria.getEvents().size());
  }

  @Test
  void bufferedDescarregaOQueSobrouNoClose() {
    final var memoria = new MemoryAppender();
    final var buffered = new BufferedAppender(memoria, 10);

    buffered.append(evento("1"));
    buffered.append(evento("2"));
    buffered.close();

    assertEquals(2, memoria.getEvents().size());
  }

  @Test
  void asyncEntregaTudoAteOClose() {
    final var memoria = new MemoryAppender();
    final var async = new AsyncAppender(memoria, 64);

    for (var i = 0; i < 50; i++) {
      async.append(evento("evento " + i));
    }
    async.close();

    assertEquals(50, memoria.getEvents().size());
  }
}
