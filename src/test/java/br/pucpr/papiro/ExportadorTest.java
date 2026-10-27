package br.pucpr.papiro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.pucpr.papiro.exportacao.ExportadorHtml;
import br.pucpr.papiro.exportacao.ExportadorMarkdown;
import br.pucpr.papiro.exportacao.ExportadorTexto;
import br.pucpr.papiro.numeracao.NumeracaoDecimal;
import br.pucpr.papiro.numeracao.SemNumeracao;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExportadorTest {
  private final Documento documento =
      Documento.novo("Relatorio")
          .autor("Ana")
          .paragrafo("Resumo")
          .secao("Vendas")
          .tabela("Mes", "Total")
          .linha("Janeiro", "10")
          .secao("Detalhes")
          .lista("a", "b")
          .fimDaSecao()
          .fimDaSecao()
          .secao("Fim")
          .construir();

  @Test
  void markdown() {
    assertEquals(
        """
        # Relatorio

        _Ana_

        Resumo

        ## 1 Vendas

        | Mes | Total |
        | --- | --- |
        | Janeiro | 10 |

        ### 1.1 Detalhes

        - a
        - b

        ## 2 Fim

        """,
        documento.exportar(new ExportadorMarkdown()));
  }

  @Test
  void textoAlinhaAsColunasDaTabela() {
    final var texto = documento.exportar(new ExportadorTexto());

    assertTrue(texto.startsWith("Relatorio\n=========\nAna\n\n"), texto);
    assertTrue(texto.contains("1 Vendas\n--------\n"), texto);
    assertTrue(texto.contains("Mes     | Total\n--------+------\nJaneiro | 10\n"), texto);
  }

  @Test
  void htmlUsaUmNivelDeTituloPorProfundidade() {
    final var html = documento.exportar(new ExportadorHtml());

    assertTrue(html.contains("<h2>1 Vendas</h2>"), html);
    assertTrue(html.contains("<h3>1.1 Detalhes</h3>"), html);
    assertTrue(html.contains("<tr><th>Mes</th><th>Total</th></tr>"), html);
    assertTrue(html.endsWith("</body>\n</html>\n"), html);
  }

  @Test
  void htmlEscapaCaracteresEspeciais() {
    final var html =
        Documento.novo("A & B").paragrafo("<script>").construir().exportar(new ExportadorHtml());

    assertTrue(html.contains("<title>A &amp; B</title>"), html);
    assertTrue(html.contains("<p>&lt;script&gt;</p>"), html);
  }

  @Test
  void numeracaoDecimal() {
    assertEquals("2.1.3", new NumeracaoDecimal().numerar(List.of(2, 1, 3)));
  }

  @Test
  void numeracaoPodeSerTrocadaSemMexerNoExportador() {
    final var documento =
        Documento.novo("x").numeracao(new SemNumeracao()).secao("Vendas").construir();

    assertTrue(documento.exportar(new ExportadorMarkdown()).contains("## Vendas\n"));
  }
}
