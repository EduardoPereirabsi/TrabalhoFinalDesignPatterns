package br.pucpr.pulse.appender;

import br.pucpr.pulse.LogEvent;
import java.util.ArrayList;
import java.util.List;

public final class BufferedAppender extends AppenderDecorator {
  private final int batchSize;
  private final List<LogEvent> buffer = new ArrayList<>();

  public BufferedAppender(Appender delegate, int batchSize) {
    super(delegate);
    this.batchSize = Math.max(1, batchSize);
  }

  @Override
  public void append(LogEvent event) {
    List<LogEvent> batch = null;
    synchronized (buffer) {
      buffer.add(event);
      if (buffer.size() >= batchSize) {
        batch = new ArrayList<>(buffer);
        buffer.clear();
      }
    }
    publish(batch);
  }

  public void flush() {
    final List<LogEvent> batch;
    synchronized (buffer) {
      batch = new ArrayList<>(buffer);
      buffer.clear();
    }
    publish(batch);
  }

  @Override
  public void close() {
    flush();
    super.close();
  }

  private void publish(List<LogEvent> batch) {
    if (batch == null) {
      return;
    }
    for (var event : batch) {
      getDelegate().append(event);
    }
  }
}
