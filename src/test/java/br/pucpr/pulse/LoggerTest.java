package br.pucpr.pulse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.pucpr.pulse.appender.MemoryAppender;
import br.pucpr.pulse.filter.LevelFilter;
import org.junit.jupiter.api.Test;

class LoggerTest {

  @Test
  void ignoraEventosAbaixoDoNivel() {
    final var memoria = new MemoryAppender();
    final var log = Logger.named("app").level(LogLevel.WARN).appender(memoria).build();

    log.debug("nao deve aparecer");
    log.info("nao deve aparecer");
    log.warn("deve aparecer");

    assertEquals(1, memoria.getEvents().size());
    assertEquals(LogLevel.WARN, memoria.getEvents().get(0).level());
  }

  @Test
  void interpolaArgumentosNaMensagem() {
    final var memoria = new MemoryAppender();
    final var log = Logger.named("app").appender(memoria).build();

    log.info("usuario {} fez {} pedidos", "Eduardo", 3);

    assertEquals("usuario Eduardo fez 3 pedidos", memoria.getEvents().get(0).message());
  }

  @Test
  void propagaContextoParaOsEventos() {
    final var memoria = new MemoryAppender();
    final var log = Logger.named("app").appender(memoria).build().with("requestId", "abc-123");

    log.info("processando");

    assertEquals("abc-123", memoria.getEvents().get(0).context().get("requestId"));
  }

  @Test
  void contextoDerivadoNaoAfetaOLoggerOriginal() {
    final var memoria = new MemoryAppender();
    final var original = Logger.named("app").appender(memoria).build();
    final var derivado = original.with("tenant", "pucpr");

    original.info("sem contexto");
    derivado.info("com contexto");

    assertTrue(memoria.getEvents().get(0).context().isEmpty());
    assertEquals("pucpr", memoria.getEvents().get(1).context().get("tenant"));
  }

  @Test
  void filtroBloqueiaAntesDeChegarNoAppender() {
    final var memoria = new MemoryAppender();
    final var log =
        Logger.named("app")
            .level(LogLevel.TRACE)
            .filter(new LevelFilter(LogLevel.ERROR))
            .appender(memoria)
            .build();

    log.info("barrado");
    log.error("passa");

    assertEquals(1, memoria.getEvents().size());
  }
}
