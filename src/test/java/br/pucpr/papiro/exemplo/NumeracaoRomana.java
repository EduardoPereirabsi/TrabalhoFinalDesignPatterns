package br.pucpr.papiro.exemplo;

import br.pucpr.papiro.numeracao.Numeracao;
import java.util.List;

public class NumeracaoRomana implements Numeracao {
  private static final int[] VALORES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
  private static final String[] SIMBOLOS = {
    "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"
  };

  @Override
  public String numerar(List<Integer> posicao) {
    final var numero = new StringBuilder(romano(posicao.getFirst()));
    for (var i = 1; i < posicao.size(); i++) {
      numero.append('.').append(posicao.get(i));
    }
    return numero.toString();
  }

  private static String romano(int valor) {
    final var romano = new StringBuilder();
    var restante = valor;
    for (var i = 0; i < VALORES.length; i++) {
      while (restante >= VALORES[i]) {
        romano.append(SIMBOLOS[i]);
        restante -= VALORES[i];
      }
    }
    return romano.toString();
  }
}
