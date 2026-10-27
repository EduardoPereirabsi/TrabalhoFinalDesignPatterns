package br.pucpr.papiro.exportacao;

import br.pucpr.papiro.Documento;
import br.pucpr.papiro.elemento.Lista;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.elemento.Tabela;
import java.util.List;

public class ExportadorHtml extends ExportadorBase {
  private static final String ESTILO =
      "body{font-family:sans-serif;max-width:800px;margin:auto}"
          + "table{border-collapse:collapse}th,td{border:1px solid #999;padding:4px 8px}";

  @Override
  protected String inicio(Documento documento) {
    final var titulo = escapar(documento.titulo());
    final var html = new StringBuilder();
    html.append("<!DOCTYPE html>\n<html lang=\"pt-BR\">\n<head>\n<meta charset=\"utf-8\">\n");
    html.append("<title>").append(titulo).append("</title>\n");
    html.append("<style>").append(ESTILO).append("</style>\n</head>\n<body>\n");
    html.append("<h1>").append(titulo).append("</h1>\n");
    if (!documento.autor().isEmpty()) {
      html.append("<p><em>").append(escapar(documento.autor())).append("</em></p>\n");
    }
    return html.toString();
  }

  @Override
  protected String fim(Documento documento) {
    return "</body>\n</html>\n";
  }

  @Override
  protected String secao(String titulo, int nivel) {
    final var h = Math.min(nivel + 1, 6);
    return "<section>\n<h" + h + ">" + escapar(titulo) + "</h" + h + ">\n";
  }

  @Override
  protected String fimDaSecao(int nivel) {
    return "</section>\n";
  }

  @Override
  protected String paragrafo(Paragrafo paragrafo) {
    return "<p>" + escapar(paragrafo.texto()) + "</p>\n";
  }

  @Override
  protected String lista(Lista lista) {
    final var html = new StringBuilder("<ul>\n");
    for (final var item : lista.itens()) {
      html.append("<li>").append(escapar(item)).append("</li>\n");
    }
    return html.append("</ul>\n").toString();
  }

  @Override
  protected String tabela(Tabela tabela) {
    final var html = new StringBuilder("<table>\n");
    html.append(linha("th", tabela.cabecalho()));
    for (final var linha : tabela.linhas()) {
      html.append(linha("td", linha));
    }
    return html.append("</table>\n").toString();
  }

  private static String linha(String tag, List<String> celulas) {
    final var html = new StringBuilder("<tr>");
    for (final var celula : celulas) {
      html.append('<').append(tag).append('>').append(escapar(celula));
      html.append("</").append(tag).append('>');
    }
    return html.append("</tr>\n").toString();
  }

  private static String escapar(String texto) {
    return texto
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;");
  }
}
