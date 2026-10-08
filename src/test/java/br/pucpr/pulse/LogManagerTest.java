package br.pucpr.pulse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import br.pucpr.pulse.appender.MemoryAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class LogManagerTest {

  @AfterEach
  void limpar() {
    LogManager.getInstance().reset();
  }

  @Test
  void existeUmaUnicaInstancia() {
    assertSame(LogManager.getInstance(), LogManager.getInstance());
  }

  @Test
  void devolveOMesmoLoggerParaOMesmoNome() {
    final var manager = LogManager.getInstance();

    assertSame(manager.getLogger("pedidos"), manager.getLogger("pedidos"));
  }

  @Test
  void aplicaConfiguracaoPadraoATodosOsLoggers() {
    final var memoria = new MemoryAppender();
    final var manager = LogManager.getInstance();
    manager.configure(builder -> builder.level(LogLevel.DEBUG).appender(memoria));

    manager.getLogger("a").debug("um");
    manager.getLogger("b").debug("dois");

    assertEquals(2, memoria.getEvents().size());
  }
}
