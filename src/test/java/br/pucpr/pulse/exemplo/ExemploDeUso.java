package br.pucpr.pulse.exemplo;

import br.pucpr.pulse.LogLevel;
import br.pucpr.pulse.LogManager;
import br.pucpr.pulse.Logger;
import br.pucpr.pulse.appender.AsyncAppender;
import br.pucpr.pulse.appender.ConsoleAppender;
import br.pucpr.pulse.appender.FileAppender;
import br.pucpr.pulse.filter.LevelFilter;
import br.pucpr.pulse.format.JsonFormatter;
import br.pucpr.pulse.telemetry.MetricsCollector;
import java.nio.file.Path;

public class ExemploDeUso {

  public static void main(String[] args) {
    usoPadrao();
    usoConfigurado();
    usoEstendido();
  }

  static void usoPadrao() {
    System.out.println("=== 1. Uso padrao: zero configuracao ===");
    final var log = LogManager.getInstance().getLogger("loja");
    log.info("aplicacao iniciada na porta {}", 8080);
    log.warn("estoque baixo do produto {}", "SKU-42");
  }

  static void usoConfigurado() {
    System.out.println();
    System.out.println("=== 2. Uso configurado com as pecas prontas ===");
    final var metricas = new MetricsCollector();
    final var log =
        Logger.named("pagamentos")
            .level(LogLevel.DEBUG)
            .appender(new ConsoleAppender(new JsonFormatter()))
            .appender(new AsyncAppender(new FileAppender(Path.of("build", "logs", "app.log"))))
            .filter(new LevelFilter(LogLevel.INFO))
            .listener(metricas)
            .build()
            .with("requestId", "req-7781");

    log.debug("descartado pelo filtro");
    log.info("pagamento aprovado: R$ {}", "149,90");
    log.error(new IllegalStateException("timeout"), "gateway indisponivel");
    log.close();

    System.out.println("metricas: " + metricas.snapshot());
  }

  static void usoEstendido() {
    System.out.println();
    System.out.println("=== 3. Cliente estendendo a biblioteca ===");
    final var webhook = new WebhookAppender("https://hooks.exemplo.com/logs", new CsvFormatter());
    final var alertas = new AlertaListener();
    final var log =
        Logger.named("auth")
            .appender(webhook)
            .filter(new PalavraSensivelFilter("senha", "token"))
            .listener(alertas)
            .build();

    log.info("login de {}", "eduardo");
    log.info("senha recebida: 123456");
    log.error("conta {} bloqueada", "patrick");

    webhook.getEnviados().forEach(System.out::println);
    alertas.getAlertas().forEach(System.out::println);
  }
}
