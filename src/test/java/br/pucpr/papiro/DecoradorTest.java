package br.pucpr.papiro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.pucpr.papiro.exportacao.ComRodape;
import br.pucpr.papiro.exportacao.ComSumario;
import br.pucpr.papiro.exportacao.ExportadorMarkdown;
import org.junit.jupiter.api.Test;

class DecoradorTest {
  private final Documento documento =
      Documento.novo("x")
          .secao("Introducao")
          .fimDaSecao()
          .secao("Resultados")
          .secao("Vendas")
          .construir();

  @Test
  void sumarioListaAsSecoesNumeradas() {
    final var md = documento.exportar(new ComSumario(new ExportadorMarkdown()));

    assertTrue(
        md.startsWith("# x\n\nSumário\n\n- 1 Introducao\n- 2 Resultados\n- 2.1 Vendas\n\n"), md);
  }

  @Test
  void rodapeVaiNoFinal() {
    final var md =
        documento.exportar(new ComRodape(new ExportadorMarkdown(), "gerado pelo Papiro"));

    assertTrue(md.endsWith("gerado pelo Papiro\n\n"), md);
  }

  @Test
  void decoradoresSeEmpilham() {
    final var md =
        documento.exportar(new ComSumario(new ComRodape(new ExportadorMarkdown(), "fim")));

    assertTrue(md.contains("Sumário"), md);
    assertTrue(md.endsWith("fim\n\n"), md);
  }

  @Test
  void documentoOriginalNaoMuda() {
    documento.exportar(new ComSumario(new ComRodape(new ExportadorMarkdown(), "fim")));

    assertEquals(2, documento.elementos().size());
  }
}
