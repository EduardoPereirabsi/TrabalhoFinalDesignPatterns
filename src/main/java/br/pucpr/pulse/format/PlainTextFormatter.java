package br.pucpr.pulse.format;

import br.pucpr.pulse.LogEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;

public final class PlainTextFormatter implements LogFormatter {
  private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ISO_INSTANT;

  @Override
  public String format(LogEvent event) {
    final var sb = new StringBuilder();
    sb.append(TIMESTAMP.format(event.timestamp()))
        .append(" [")
        .append(String.format("%-5s", event.level()))
        .append("] ")
        .append(event.logger())
        .append(" - ")
        .append(event.message());
    if (!event.context().isEmpty()) {
      sb.append(' ').append(event.context());
    }
    if (event.hasError()) {
      sb.append(System.lineSeparator()).append(stackTrace(event));
    }
    return sb.toString();
  }

  private static String stackTrace(LogEvent event) {
    final var writer = new StringWriter();
    event.error().printStackTrace(new PrintWriter(writer, true));
    return writer.toString().stripTrailing();
  }
}
