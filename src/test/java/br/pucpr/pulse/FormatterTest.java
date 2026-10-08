package br.pucpr.pulse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.pucpr.pulse.format.JsonFormatter;
import br.pucpr.pulse.format.PlainTextFormatter;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FormatterTest {
  private static final Instant MOMENTO = Instant.parse("2026-10-29T13:00:00Z");

  @Test
  void textoSimplesTemDataNivelLoggerEMensagem() {
    final var evento = new LogEvent(MOMENTO, LogLevel.INFO, "app", "pronto", Map.of(), null);

    assertEquals(
        "2026-10-29T13:00:00Z [INFO ] app - pronto", new PlainTextFormatter().format(evento));
  }

  @Test
  void jsonGeraUmObjetoComOsCampos() {
    final var evento =
        new LogEvent(MOMENTO, LogLevel.WARN, "app", "lento", Map.of("ms", "900"), null);

    final var json = new JsonFormatter().format(evento);

    assertEquals(
        "{\"timestamp\":\"2026-10-29T13:00:00Z\",\"level\":\"WARN\",\"logger\":\"app\","
            + "\"message\":\"lento\",\"context\":{\"ms\":\"900\"}}",
        json);
  }

  @Test
  void jsonEscapaAspasEQuebrasDeLinha() {
    final var evento =
        new LogEvent(MOMENTO, LogLevel.INFO, "app", "disse \"oi\"\nfim", Map.of(), null);

    final var json = new JsonFormatter().format(evento);

    assertTrue(json.contains("\"message\":\"disse \\\"oi\\\"\\nfim\""), json);
  }
}
