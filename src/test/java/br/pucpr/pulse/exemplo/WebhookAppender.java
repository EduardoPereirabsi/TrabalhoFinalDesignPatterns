package br.pucpr.pulse.exemplo;

import br.pucpr.pulse.LogEvent;
import br.pucpr.pulse.appender.AbstractAppender;
import br.pucpr.pulse.format.LogFormatter;
import java.util.ArrayList;
import java.util.List;

public class WebhookAppender extends AbstractAppender {
  private final String url;
  private final List<String> enviados = new ArrayList<>();

  public WebhookAppender(String url, LogFormatter formatter) {
    super(formatter);
    this.url = url;
  }

  @Override
  protected void write(LogEvent event, String formatted) {
    enviados.add("POST " + url + " " + formatted);
  }

  public List<String> getEnviados() {
    return List.copyOf(enviados);
  }
}
