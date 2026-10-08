package br.pucpr.pulse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class LogManager {
  private static final LogManager INSTANCE = new LogManager();

  private final Map<String, Logger> loggers = new ConcurrentHashMap<>();
  private volatile Consumer<LoggerBuilder> defaults = builder -> {};

  private LogManager() {}

  public static LogManager getInstance() {
    return INSTANCE;
  }

  public void configure(Consumer<LoggerBuilder> defaults) {
    this.defaults = defaults == null ? builder -> {} : defaults;
    loggers.clear();
  }

  public Logger getLogger(String name) {
    return loggers.computeIfAbsent(
        name,
        key -> {
          final var builder = Logger.named(key);
          defaults.accept(builder);
          return builder.build();
        });
  }

  public Logger getLogger(Class<?> type) {
    return getLogger(type.getName());
  }

  public void reset() {
    loggers.values().forEach(Logger::close);
    loggers.clear();
    defaults = builder -> {};
  }
}
