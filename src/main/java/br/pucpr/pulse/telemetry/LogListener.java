package br.pucpr.pulse.telemetry;

import br.pucpr.pulse.LogEvent;

@FunctionalInterface
public interface LogListener {
  void onLog(LogEvent event);
}
