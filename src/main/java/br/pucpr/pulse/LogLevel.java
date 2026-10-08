package br.pucpr.pulse;

public enum LogLevel {
  TRACE,
  DEBUG,
  INFO,
  WARN,
  ERROR,
  FATAL;

  public boolean isAtLeast(LogLevel other) {
    return ordinal() >= other.ordinal();
  }
}
