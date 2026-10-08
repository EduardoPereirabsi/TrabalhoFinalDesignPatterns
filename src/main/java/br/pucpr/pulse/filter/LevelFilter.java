package br.pucpr.pulse.filter;

import br.pucpr.pulse.LogEvent;
import br.pucpr.pulse.LogLevel;

public final class LevelFilter extends LogFilter {
  private final LogLevel minimum;

  public LevelFilter(LogLevel minimum) {
    this.minimum = minimum;
  }

  @Override
  protected Decision decide(LogEvent event) {
    return event.level().isAtLeast(minimum) ? Decision.NEUTRAL : Decision.DENY;
  }
}
