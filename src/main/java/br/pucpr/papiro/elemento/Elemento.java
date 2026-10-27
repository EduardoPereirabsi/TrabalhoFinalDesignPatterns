package br.pucpr.papiro.elemento;

public sealed interface Elemento permits Paragrafo, Lista, Tabela, Secao {
  int contarPalavras();
}
