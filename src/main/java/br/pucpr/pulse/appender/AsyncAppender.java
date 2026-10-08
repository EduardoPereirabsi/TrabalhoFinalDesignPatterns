package br.pucpr.pulse.appender;

import br.pucpr.pulse.LogEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class AsyncAppender extends AppenderDecorator {
  private final BlockingQueue<LogEvent> queue;
  private final Thread worker;
  private volatile boolean running = true;

  public AsyncAppender(Appender delegate) {
    this(delegate, 1024);
  }

  public AsyncAppender(Appender delegate, int capacity) {
    super(delegate);
    this.queue = new LinkedBlockingQueue<>(capacity);
    this.worker = new Thread(this::drain, "pulse-async");
    this.worker.setDaemon(true);
    this.worker.start();
  }

  @Override
  public void append(LogEvent event) {
    if (!queue.offer(event)) {
      getDelegate().append(event);
    }
  }

  @Override
  public void close() {
    running = false;
    try {
      worker.join(TimeUnit.SECONDS.toMillis(2));
    } catch (InterruptedException interrupted) {
      Thread.currentThread().interrupt();
    }
    super.close();
  }

  private void drain() {
    try {
      while (running || !queue.isEmpty()) {
        final var event = queue.poll(50, TimeUnit.MILLISECONDS);
        if (event != null) {
          getDelegate().append(event);
        }
      }
    } catch (InterruptedException interrupted) {
      Thread.currentThread().interrupt();
    }
  }
}
