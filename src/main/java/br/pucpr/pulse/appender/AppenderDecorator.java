package br.pucpr.pulse.appender;

import java.util.Objects;

public abstract class AppenderDecorator implements Appender {
  private final Appender delegate;

  protected AppenderDecorator(Appender delegate) {
    this.delegate = Objects.requireNonNull(delegate, "delegate");
  }

  protected Appender getDelegate() {
    return delegate;
  }

  @Override
  public void close() {
    delegate.close();
  }
}
