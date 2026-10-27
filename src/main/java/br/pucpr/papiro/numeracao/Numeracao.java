package br.pucpr.papiro.numeracao;

import java.util.List;

@FunctionalInterface
public interface Numeracao {
  String numerar(List<Integer> posicao);

  default String titulo(List<Integer> posicao, String titulo) {
    final var numero = numerar(posicao);
    return numero.isEmpty() ? titulo : numero + " " + titulo;
  }
}
