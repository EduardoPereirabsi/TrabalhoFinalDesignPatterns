package br.pucpr.pulse.format;

import br.pucpr.pulse.LogEvent;

@FunctionalInterface
public interface LogFormatter {
  String format(LogEvent event);
}
