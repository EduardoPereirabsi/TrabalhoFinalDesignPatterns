package br.pucpr.papiro.elemento;

import java.util.Objects;

public record Paragrafo(String texto) implements Elemento {
  public Paragrafo {
    Objects.requireNonNull(texto, "texto");
  }

  @Override
  public int contarPalavras() {
    return Palavras.contar(texto);
  }
}
