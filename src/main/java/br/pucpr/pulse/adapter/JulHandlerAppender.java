package br.pucpr.pulse.adapter;

import br.pucpr.pulse.LogEvent;
import br.pucpr.pulse.LogLevel;
import br.pucpr.pulse.appender.AbstractAppender;
import br.pucpr.pulse.format.LogFormatter;
import br.pucpr.pulse.format.PlainTextFormatter;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class JulHandlerAppender extends AbstractAppender {
  private final Handler handler;

  public JulHandlerAppender(Handler handler) {
    this(handler, new PlainTextFormatter());
  }

  public JulHandlerAppender(Handler handler, LogFormatter formatter) {
    super(formatter);
    this.handler = Objects.requireNonNull(handler, "handler");
  }

  @Override
  protected void write(LogEvent event, String formatted) {
    final var julRecord = new LogRecord(toJulLevel(event.level()), formatted);
    julRecord.setLoggerName(event.logger());
    julRecord.setInstant(event.timestamp());
    julRecord.setThrown(event.error());
    handler.publish(julRecord);
  }

  @Override
  public void close() {
    handler.flush();
    handler.close();
  }

  private static Level toJulLevel(LogLevel level) {
    return switch (level) {
      case TRACE -> Level.FINEST;
      case DEBUG -> Level.FINE;
      case INFO -> Level.INFO;
      case WARN -> Level.WARNING;
      case ERROR, FATAL -> Level.SEVERE;
    };
  }
}
