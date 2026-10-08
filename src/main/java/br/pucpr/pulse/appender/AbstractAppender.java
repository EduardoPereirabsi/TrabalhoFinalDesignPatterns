package br.pucpr.pulse.appender;

import br.pucpr.pulse.LogEvent;
import br.pucpr.pulse.format.LogFormatter;
import br.pucpr.pulse.format.PlainTextFormatter;

public abstract class AbstractAppender implements Appender {
  private final LogFormatter formatter;

  protected AbstractAppender(LogFormatter formatter) {
    this.formatter = formatter == null ? new PlainTextFormatter() : formatter;
  }

  @Override
  public final void append(LogEvent event) {
    final String formatted;
    try {
      formatted = formatter.format(event);
    } catch (RuntimeException error) {
      onError(event, error);
      return;
    }
    try {
      write(event, formatted);
    } catch (RuntimeException error) {
      onError(event, error);
    }
  }

  protected abstract void write(LogEvent event, String formatted);

  protected void onError(LogEvent event, RuntimeException error) {
    System.err.println("[pulse] nao foi possivel registrar o evento: " + error);
  }

  protected LogFormatter getFormatter() {
    return formatter;
  }
}
