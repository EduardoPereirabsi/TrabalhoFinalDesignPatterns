package br.pucpr.pulse;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public record LogEvent(
    Instant timestamp,
    LogLevel level,
    String logger,
    String message,
    Map<String, String> context,
    Throwable error) {

  public LogEvent {
    Objects.requireNonNull(timestamp, "timestamp");
    Objects.requireNonNull(level, "level");
    Objects.requireNonNull(logger, "logger");
    Objects.requireNonNull(message, "message");
    context = context == null ? Map.of() : Map.copyOf(context);
  }

  public boolean hasError() {
    return error != null;
  }
}
