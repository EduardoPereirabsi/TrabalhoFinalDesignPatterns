package br.pucpr.pulse.telemetry;

import br.pucpr.pulse.LogEvent;
import br.pucpr.pulse.LogLevel;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class MetricsCollector implements LogListener {
  private final Map<LogLevel, AtomicLong> counters = new EnumMap<>(LogLevel.class);

  public MetricsCollector() {
    for (var level : LogLevel.values()) {
      counters.put(level, new AtomicLong());
    }
  }

  @Override
  public void onLog(LogEvent event) {
    counters.get(event.level()).incrementAndGet();
  }

  public long count(LogLevel level) {
    return counters.get(level).get();
  }

  public long total() {
    return counters.values().stream().mapToLong(AtomicLong::get).sum();
  }

  public Map<LogLevel, Long> snapshot() {
    final var copy = new EnumMap<LogLevel, Long>(LogLevel.class);
    counters.forEach((level, counter) -> copy.put(level, counter.get()));
    return Collections.unmodifiableMap(copy);
  }
}
