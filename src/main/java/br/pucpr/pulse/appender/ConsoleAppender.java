package br.pucpr.pulse.appender;

import br.pucpr.pulse.LogEvent;
import br.pucpr.pulse.LogLevel;
import br.pucpr.pulse.format.LogFormatter;
import br.pucpr.pulse.format.PlainTextFormatter;

public final class ConsoleAppender extends AbstractAppender {
  private final LogLevel errorThreshold;

  public ConsoleAppender() {
    this(new PlainTextFormatter());
  }

  public ConsoleAppender(LogFormatter formatter) {
    this(formatter, LogLevel.WARN);
  }

  public ConsoleAppender(LogFormatter formatter, LogLevel errorThreshold) {
    super(formatter);
    this.errorThreshold = errorThreshold;
  }

  @Override
  protected void write(LogEvent event, String formatted) {
    final var stream = event.level().isAtLeast(errorThreshold) ? System.err : System.out;
    stream.println(formatted);
  }
}
