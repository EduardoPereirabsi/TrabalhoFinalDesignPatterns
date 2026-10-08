package br.pucpr.pulse.exemplo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.pucpr.pulse.Logger;
import org.junit.jupiter.api.Test;

class ExtensaoTest {

  @Test
  void clienteEstendeTodosOsPontosSemAlterarALib() {
    final var webhook = new WebhookAppender("https://hooks.exemplo.com", new CsvFormatter());
    final var alertas = new AlertaListener();
    final var log =
        Logger.named("auth")
            .appender(webhook)
            .filter(new PalavraSensivelFilter("senha"))
            .listener(alertas)
            .build();

    log.info("login ok");
    log.info("senha: 123");
    log.error("conta bloqueada");

    assertEquals(2, webhook.getEnviados().size(), "o filtro deve barrar a senha");
    assertTrue(webhook.getEnviados().get(0).contains(";INFO;auth;"), "deve sair em CSV");
    assertEquals(1, alertas.getAlertas().size(), "so o ERROR gera alerta");
  }
}
