package br.pucpr.pulse.appender;

import br.pucpr.pulse.LogEvent;

public interface Appender extends AutoCloseable {
  void append(LogEvent event);

  @Override
  default void close() {}
}
