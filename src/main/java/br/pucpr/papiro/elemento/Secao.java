package br.pucpr.papiro.elemento;

import java.util.List;
import java.util.Objects;

public record Secao(String titulo, List<Elemento> elementos) implements Elemento {
  public Secao {
    Objects.requireNonNull(titulo, "titulo");
    elementos = List.copyOf(elementos);
  }

  public Secao(String titulo, Elemento... elementos) {
    this(titulo, List.of(elementos));
  }

  @Override
  public int contarPalavras() {
    var total = Palavras.contar(titulo);
    for (final var elemento : elementos) {
      total += elemento.contarPalavras();
    }
    return total;
  }
}
