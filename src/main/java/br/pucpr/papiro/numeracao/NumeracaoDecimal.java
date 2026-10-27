package br.pucpr.papiro.numeracao;

import java.util.List;
import java.util.stream.Collectors;

public class NumeracaoDecimal implements Numeracao {
  @Override
  public String numerar(List<Integer> posicao) {
    return posicao.stream().map(String::valueOf).collect(Collectors.joining("."));
  }
}
