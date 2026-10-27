package br.pucpr.papiro.exportacao;

import br.pucpr.papiro.Documento;
import br.pucpr.papiro.elemento.Lista;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.elemento.Tabela;
import java.util.List;

public class ExportadorMarkdown extends ExportadorBase {

  @Override
  protected String inicio(Documento documento) {
    final var md = new StringBuilder("# ").append(documento.titulo()).append("\n\n");
    if (!documento.autor().isEmpty()) {
      md.append('_').append(documento.autor()).append("_\n\n");
    }
    return md.toString();
  }

  @Override
  protected String secao(String titulo, int nivel) {
    return "#".repeat(Math.min(nivel + 1, 6)) + " " + titulo + "\n\n";
  }

  @Override
  protected String paragrafo(Paragrafo paragrafo) {
    return paragrafo.texto() + "\n\n";
  }

  @Override
  protected String lista(Lista lista) {
    final var md = new StringBuilder();
    for (final var item : lista.itens()) {
      md.append("- ").append(item).append('\n');
    }
    return md.append('\n').toString();
  }

  @Override
  protected String tabela(Tabela tabela) {
    final var md = new StringBuilder(linha(tabela.cabecalho()));
    md.append('|').append(" --- |".repeat(tabela.cabecalho().size())).append('\n');
    for (final var linha : tabela.linhas()) {
      md.append(linha(linha));
    }
    return md.append('\n').toString();
  }

  private static String linha(List<String> celulas) {
    return "| " + String.join(" | ", celulas) + " |\n";
  }
}
