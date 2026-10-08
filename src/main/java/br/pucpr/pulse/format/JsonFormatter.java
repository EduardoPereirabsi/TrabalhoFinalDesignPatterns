package br.pucpr.pulse.format;

import br.pucpr.pulse.LogEvent;
import java.time.format.DateTimeFormatter;

public final class JsonFormatter implements LogFormatter {
  private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ISO_INSTANT;

  @Override
  public String format(LogEvent event) {
    final var sb = new StringBuilder("{");
    field(sb, "timestamp", TIMESTAMP.format(event.timestamp()), true);
    field(sb, "level", event.level().name(), false);
    field(sb, "logger", event.logger(), false);
    field(sb, "message", event.message(), false);
    if (event.hasError()) {
      field(sb, "error", String.valueOf(event.error()), false);
    }
    if (!event.context().isEmpty()) {
      sb.append(",\"context\":{");
      var first = true;
      for (var entry : event.context().entrySet()) {
        field(sb, entry.getKey(), entry.getValue(), first);
        first = false;
      }
      sb.append('}');
    }
    return sb.append('}').toString();
  }

  private static void field(StringBuilder sb, String name, String value, boolean first) {
    if (!first) {
      sb.append(',');
    }
    sb.append('"').append(escape(name)).append("\":\"").append(escape(value)).append('"');
  }

  private static String escape(String value) {
    final var sb = new StringBuilder(value.length());
    for (var c : value.toCharArray()) {
      switch (c) {
        case '"' -> sb.append("\\\"");
        case '\\' -> sb.append("\\\\");
        case '\n' -> sb.append("\\n");
        case '\r' -> sb.append("\\r");
        case '\t' -> sb.append("\\t");
        default -> sb.append(c);
      }
    }
    return sb.toString();
  }
}
