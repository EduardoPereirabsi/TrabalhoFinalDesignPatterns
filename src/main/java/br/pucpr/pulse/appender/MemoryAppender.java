package br.pucpr.pulse.appender;

import br.pucpr.pulse.LogEvent;
import br.pucpr.pulse.format.LogFormatter;
import br.pucpr.pulse.format.PlainTextFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class MemoryAppender extends AbstractAppender {
  private final List<LogEvent> events = new CopyOnWriteArrayList<>();
  private final List<String> lines = new CopyOnWriteArrayList<>();

  public MemoryAppender() {
    this(new PlainTextFormatter());
  }

  public MemoryAppender(LogFormatter formatter) {
    super(formatter);
  }

  @Override
  protected void write(LogEvent event, String formatted) {
    events.add(event);
    lines.add(formatted);
  }

  public List<LogEvent> getEvents() {
    return List.copyOf(events);
  }

  public List<String> getLines() {
    return List.copyOf(lines);
  }

  public void clear() {
    events.clear();
    lines.clear();
  }
}
