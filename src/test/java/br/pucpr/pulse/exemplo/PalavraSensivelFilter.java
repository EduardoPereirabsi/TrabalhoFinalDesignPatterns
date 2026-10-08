package br.pucpr.pulse.exemplo;

import br.pucpr.pulse.LogEvent;
import br.pucpr.pulse.filter.LogFilter;
import java.util.List;
import java.util.Locale;

public class PalavraSensivelFilter extends LogFilter {
  private final List<String> palavras;

  public PalavraSensivelFilter(String... palavras) {
    this.palavras = List.of(palavras);
  }

  @Override
  protected Decision decide(LogEvent event) {
    final var mensagem = event.message().toLowerCase(Locale.ROOT);
    for (var palavra : palavras) {
      if (mensagem.contains(palavra)) {
        return Decision.DENY;
      }
    }
    return Decision.NEUTRAL;
  }
}
