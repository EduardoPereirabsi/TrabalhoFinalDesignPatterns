package br.pucpr.pulse;

import br.pucpr.pulse.appender.Appender;
import br.pucpr.pulse.appender.CompositeAppender;
import br.pucpr.pulse.appender.ConsoleAppender;
import br.pucpr.pulse.filter.LogFilter;
import br.pucpr.pulse.telemetry.LogListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class LoggerBuilder {
  private final String name;
  private final List<Appender> appenders = new ArrayList<>();
  private final List<LogListener> listeners = new ArrayList<>();
  private final Map<String, String> context = new LinkedHashMap<>();
  private LogLevel level = LogLevel.INFO;
  private LogFilter filter;

  LoggerBuilder(String name) {
    this.name = Objects.requireNonNull(name, "name");
  }

  public LoggerBuilder level(LogLevel level) {
    this.level = Objects.requireNonNull(level, "level");
    return this;
  }

  public LoggerBuilder appender(Appender appender) {
    appenders.add(Objects.requireNonNull(appender, "appender"));
    return this;
  }

  public LoggerBuilder filter(LogFilter filter) {
    this.filter = filter;
    return this;
  }

  public LoggerBuilder listener(LogListener listener) {
    listeners.add(Objects.requireNonNull(listener, "listener"));
    return this;
  }

  public LoggerBuilder context(String key, String value) {
    context.put(key, value);
    return this;
  }

  public Logger build() {
    return new Logger(name, level, target(), filter, listeners, context);
  }

  private Appender target() {
    if (appenders.isEmpty()) {
      return new ConsoleAppender();
    }
    if (appenders.size() == 1) {
      return appenders.get(0);
    }
    return new CompositeAppender(appenders);
  }
}
