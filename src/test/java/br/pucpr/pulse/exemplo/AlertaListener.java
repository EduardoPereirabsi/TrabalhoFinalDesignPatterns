package br.pucpr.pulse.exemplo;

import br.pucpr.pulse.LogEvent;
import br.pucpr.pulse.LogLevel;
import br.pucpr.pulse.telemetry.LogListener;
import java.util.ArrayList;
import java.util.List;

public class AlertaListener implements LogListener {
  private final List<String> alertas = new ArrayList<>();

  @Override
  public void onLog(LogEvent event) {
    if (event.level().isAtLeast(LogLevel.ERROR)) {
      alertas.add("ALERTA para o plantao: " + event.message());
    }
  }

  public List<String> getAlertas() {
    return List.copyOf(alertas);
  }
}
