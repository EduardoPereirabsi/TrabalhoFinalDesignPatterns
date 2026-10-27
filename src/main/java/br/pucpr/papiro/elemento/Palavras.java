package br.pucpr.papiro.elemento;

final class Palavras {
  private Palavras() {}

  static int contar(String texto) {
    final var limpo = texto.strip();
    return limpo.isEmpty() ? 0 : limpo.split("\\s+").length;
  }
}
