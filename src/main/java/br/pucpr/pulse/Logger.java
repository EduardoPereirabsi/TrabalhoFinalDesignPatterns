package br.pucpr.pulse;

import br.pucpr.pulse.appender.Appender;
import br.pucpr.pulse.filter.LogFilter;
import br.pucpr.pulse.telemetry.LogListener;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Logger {
  private final String name;
  private final LogLevel level;
  private final Appender appender;
  private final LogFilter filter;
  private final List<LogListener> listeners;
  private final Map<String, String> context;

  Logger(
      String name,
      LogLevel level,
      Appender appender,
      LogFilter filter,
      List<LogListener> listeners,
      Map<String, String> context) {
    this.name = name;
    this.level = level;
    this.appender = appender;
    this.filter = filter;
    this.listeners = List.copyOf(listeners);
    this.context = Map.copyOf(context);
  }

  public static LoggerBuilder named(String name) {
    return new LoggerBuilder(name);
  }

  public String getName() {
    return name;
  }

  public LogLevel getLevel() {
    return level;
  }

  public boolean isEnabled(LogLevel level) {
    return level.isAtLeast(this.level);
  }

  public Logger with(String key, String value) {
    final var merged = new LinkedHashMap<>(context);
    merged.put(key, value);
    return new Logger(name, level, appender, filter, listeners, merged);
  }

  public void trace(String message, Object... args) {
    log(LogLevel.TRACE, null, message, args);
  }

  public void debug(String message, Object... args) {
    log(LogLevel.DEBUG, null, message, args);
  }

  public void info(String message, Object... args) {
    log(LogLevel.INFO, null, message, args);
  }

  public void warn(String message, Object... args) {
    log(LogLevel.WARN, null, message, args);
  }

  public void error(String message, Object... args) {
    log(LogLevel.ERROR, null, message, args);
  }

  public void error(Throwable error, String message, Object... args) {
    log(LogLevel.ERROR, error, message, args);
  }

  public void fatal(Throwable error, String message, Object... args) {
    log(LogLevel.FATAL, error, message, args);
  }

  public void log(LogLevel level, Throwable error, String message, Object... args) {
    if (!isEnabled(level)) {
      return;
    }
    final var event =
        new LogEvent(Instant.now(), level, name, interpolate(message, args), context, error);
    if (filter != null && !filter.accept(event)) {
      return;
    }
    appender.append(event);
    for (var listener : new ArrayList<>(listeners)) {
      listener.onLog(event);
    }
  }

  public void close() {
    appender.close();
  }

  private static String interpolate(String template, Object... args) {
    if (args == null || args.length == 0) {
      return template;
    }
    final var sb = new StringBuilder(template.length() + 16);
    var next = 0;
    var i = 0;
    while (i < template.length()) {
      final var placeholder =
          next < args.length
              && i + 1 < template.length()
              && template.charAt(i) == '{'
              && template.charAt(i + 1) == '}';
      if (placeholder) {
        sb.append(args[next++]);
        i += 2;
      } else {
        sb.append(template.charAt(i++));
      }
    }
    return sb.toString();
  }
}
