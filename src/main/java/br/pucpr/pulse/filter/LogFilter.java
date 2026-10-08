package br.pucpr.pulse.filter;

import br.pucpr.pulse.LogEvent;
import java.util.Objects;

public abstract class LogFilter {
  public enum Decision {
    ACCEPT,
    DENY,
    NEUTRAL
  }

  private LogFilter next;

  public LogFilter then(LogFilter next) {
    Objects.requireNonNull(next, "next");
    var last = this;
    while (last.next != null) {
      last = last.next;
    }
    last.next = next;
    return this;
  }

  public final boolean accept(LogEvent event) {
    return switch (decide(event)) {
      case ACCEPT -> true;
      case DENY -> false;
      case NEUTRAL -> next == null || next.accept(event);
    };
  }

  protected abstract Decision decide(LogEvent event);
}
