package br.pucpr.pulse.appender;

import br.pucpr.pulse.LogEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class CompositeAppender implements Appender {
  private final List<Appender> children = new CopyOnWriteArrayList<>();

  public CompositeAppender(Appender... children) {
    this(Arrays.asList(children));
  }

  public CompositeAppender(Collection<? extends Appender> children) {
    this.children.addAll(children);
  }

  public CompositeAppender add(Appender appender) {
    children.add(appender);
    return this;
  }

  public CompositeAppender remove(Appender appender) {
    children.remove(appender);
    return this;
  }

  public int size() {
    return children.size();
  }

  @Override
  public void append(LogEvent event) {
    for (var child : children) {
      child.append(event);
    }
  }

  @Override
  public void close() {
    for (var child : children) {
      child.close();
    }
  }
}
