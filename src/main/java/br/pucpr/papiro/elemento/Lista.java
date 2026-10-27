package br.pucpr.papiro.elemento;

import java.util.List;

public record Lista(List<String> itens) implements Elemento {
  public Lista {
    itens = List.copyOf(itens);
  }

  public Lista(String... itens) {
    this(List.of(itens));
  }

  @Override
  public int contarPalavras() {
    return itens.stream().mapToInt(Palavras::contar).sum();
  }
}
