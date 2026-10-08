package br.pucpr.pulse.filter;

import br.pucpr.pulse.LogEvent;

public final class RateLimitFilter extends LogFilter {
  private final int maxEvents;
  private final long windowMillis;

  private long windowStart = System.currentTimeMillis();
  private int seen;

  public RateLimitFilter(int maxEvents, long windowMillis) {
    this.maxEvents = maxEvents;
    this.windowMillis = windowMillis;
  }

  @Override
  protected synchronized Decision decide(LogEvent event) {
    final var now = System.currentTimeMillis();
    if (now - windowStart >= windowMillis) {
      windowStart = now;
      seen = 0;
    }
    return ++seen > maxEvents ? Decision.DENY : Decision.NEUTRAL;
  }
}
