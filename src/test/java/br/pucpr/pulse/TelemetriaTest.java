package br.pucpr.pulse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.pucpr.pulse.appender.MemoryAppender;
import br.pucpr.pulse.telemetry.MetricsCollector;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TelemetriaTest {

  @Test
  void coletaContadoresPorNivel() {
    final var metricas = new MetricsCollector();
    final var log =
        Logger.named("app")
            .level(LogLevel.TRACE)
            .appender(new MemoryAppender())
            .listener(metricas)
            .build();

    log.info("um");
    log.info("dois");
    log.error("tres");

    assertEquals(2, metricas.count(LogLevel.INFO));
    assertEquals(1, metricas.count(LogLevel.ERROR));
    assertEquals(3, metricas.total());
  }

  @Test
  void notificaTodosOsObservadores() {
    final List<String> alertas = new ArrayList<>();
    final var metricas = new MetricsCollector();
    final var log =
        Logger.named("app")
            .appender(new MemoryAppender())
            .listener(metricas)
            .listener(event -> alertas.add(event.message()))
            .build();

    log.error("disco cheio");

    assertEquals(1, metricas.count(LogLevel.ERROR));
    assertEquals(List.of("disco cheio"), alertas);
  }
}
