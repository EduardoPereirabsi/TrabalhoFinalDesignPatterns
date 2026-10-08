package br.pucpr.pulse.filter;

import br.pucpr.pulse.LogEvent;

public final class LoggerNameFilter extends LogFilter {
  private final String prefix;

  public LoggerNameFilter(String prefix) {
    this.prefix = prefix;
  }

  @Override
  protected Decision decide(LogEvent event) {
    return event.logger().startsWith(prefix) ? Decision.NEUTRAL : Decision.DENY;
  }
}
