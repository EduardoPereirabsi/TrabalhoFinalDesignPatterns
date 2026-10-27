package br.pucpr.papiro.exemplo;

import br.pucpr.papiro.Documento;
import br.pucpr.papiro.elemento.Lista;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.elemento.Tabela;
import br.pucpr.papiro.exportacao.ExportadorBase;
import java.util.List;

public class ExportadorLatex extends ExportadorBase {
  private static final List<String> COMANDOS = List.of("section", "subsection", "subsubsection");

  @Override
  protected String inicio(Documento documento) {
    return "\\documentclass{article}\n"
        + "\\usepackage[utf8]{inputenc}\n"
        + "\\title{"
        + escapar(documento.titulo())
        + "}\n"
        + "\\author{"
        + escapar(documento.autor())
        + "}\n"
        + "\\begin{document}\n"
        + "\\maketitle\n\n";
  }

  @Override
  protected String fim(Documento documento) {
    return "\\end{document}\n";
  }

  @Override
  protected String secao(String titulo, int nivel) {
    final var comando = COMANDOS.get(Math.min(nivel, COMANDOS.size()) - 1);
    return "\\" + comando + "*{" + escapar(titulo) + "}\n\n";
  }

  @Override
  protected String paragrafo(Paragrafo paragrafo) {
    return escapar(paragrafo.texto()) + "\n\n";
  }

  @Override
  protected String lista(Lista lista) {
    final var latex = new StringBuilder("\\begin{itemize}\n");
    for (final var item : lista.itens()) {
      latex.append("  \\item ").append(escapar(item)).append('\n');
    }
    return latex.append("\\end{itemize}\n\n").toString();
  }

  @Override
  protected String tabela(Tabela tabela) {
    final var latex = new StringBuilder("\\begin{tabular}{");
    latex.append("|l".repeat(tabela.cabecalho().size())).append("|}\n\\hline\n");
    latex.append(linha(tabela.cabecalho())).append("\\hline\n");
    for (final var linha : tabela.linhas()) {
      latex.append(linha(linha));
    }
    return latex.append("\\hline\n\\end{tabular}\n\n").toString();
  }

  private static String linha(List<String> celulas) {
    return String.join(" & ", celulas.stream().map(ExportadorLatex::escapar).toList()) + " \\\\\n";
  }

  private static String escapar(String texto) {
    final var latex = new StringBuilder();
    for (final var c : texto.toCharArray()) {
      if ("&%$#_{}".indexOf(c) >= 0) {
        latex.append('\\');
      }
      latex.append(c);
    }
    return latex.toString();
  }
}
