package br.pucpr.papiro.elemento;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record Tabela(List<String> cabecalho, List<List<String>> linhas) implements Elemento {
  public Tabela {
    cabecalho = List.copyOf(cabecalho);
    linhas = linhas.stream().map(List::copyOf).toList();
    for (final var linha : linhas) {
      if (linha.size() != cabecalho.size()) {
        throw new IllegalArgumentException(
            "a linha " + linha + " deveria ter " + cabecalho.size() + " colunas");
      }
    }
  }

  public Tabela(String... cabecalho) {
    this(List.of(cabecalho), List.of());
  }

  public Tabela comLinha(String... celulas) {
    final var novas = new ArrayList<>(linhas);
    novas.add(List.of(celulas));
    return new Tabela(cabecalho, novas);
  }

  @Override
  public int contarPalavras() {
    return Stream.concat(cabecalho.stream(), linhas.stream().flatMap(List::stream))
        .mapToInt(Palavras::contar)
        .sum();
  }
}
