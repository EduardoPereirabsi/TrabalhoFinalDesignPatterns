package br.pucpr.pulse.exemplo;

import br.pucpr.pulse.LogEvent;
import br.pucpr.pulse.format.LogFormatter;

public class CsvFormatter implements LogFormatter {
  @Override
  public String format(LogEvent event) {
    return String.join(
        ";",
        event.timestamp().toString(),
        event.level().name(),
        event.logger(),
        quote(event.message()));
  }

  private static String quote(String value) {
    return "\"" + value.replace("\"", "\"\"") + "\"";
  }
}
