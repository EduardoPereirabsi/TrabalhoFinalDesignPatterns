package br.pucpr.papiro.exportacao;

import br.pucpr.papiro.Documento;
import br.pucpr.papiro.elemento.Lista;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.elemento.Tabela;
import java.util.ArrayList;
import java.util.List;

public class ExportadorTexto extends ExportadorBase {

  @Override
  protected String inicio(Documento documento) {
    final var texto = new StringBuilder(sublinhado(documento.titulo(), '='));
    if (!documento.autor().isEmpty()) {
      texto.append(documento.autor()).append('\n');
    }
    return texto.append('\n').toString();
  }

  @Override
  protected String secao(String titulo, int nivel) {
    return (nivel == 1 ? sublinhado(titulo, '-') : titulo + "\n") + "\n";
  }

  @Override
  protected String paragrafo(Paragrafo paragrafo) {
    return paragrafo.texto() + "\n\n";
  }

  @Override
  protected String lista(Lista lista) {
    final var texto = new StringBuilder();
    for (final var item : lista.itens()) {
      texto.append("  * ").append(item).append('\n');
    }
    return texto.append('\n').toString();
  }

  @Override
  protected String tabela(Tabela tabela) {
    final var linhas = new ArrayList<List<String>>();
    linhas.add(tabela.cabecalho());
    linhas.addAll(tabela.linhas());

    final var larguras = new int[tabela.cabecalho().size()];
    for (final var linha : linhas) {
      for (var i = 0; i < larguras.length; i++) {
        larguras[i] = Math.max(larguras[i], linha.get(i).length());
      }
    }

    final var texto = new StringBuilder();
    for (var l = 0; l < linhas.size(); l++) {
      texto.append(linha(linhas.get(l), larguras));
      if (l == 0) {
        texto.append(separador(larguras));
      }
    }
    return texto.append('\n').toString();
  }

  private static String linha(List<String> celulas, int[] larguras) {
    final var colunas = new ArrayList<String>();
    for (var i = 0; i < larguras.length; i++) {
      colunas.add(String.format("%-" + larguras[i] + "s", celulas.get(i)));
    }
    return String.join(" | ", colunas).stripTrailing() + "\n";
  }

  private static String separador(int[] larguras) {
    final var colunas = new ArrayList<String>();
    for (final var largura : larguras) {
      colunas.add("-".repeat(largura));
    }
    return String.join("-+-", colunas) + "\n";
  }

  private static String sublinhado(String titulo, char marca) {
    return titulo + "\n" + String.valueOf(marca).repeat(titulo.length()) + "\n";
  }
}
