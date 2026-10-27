package br.pucpr.papiro;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.pucpr.papiro.exportacao.Exportador;
import br.pucpr.papiro.exportacao.ExportadorHtml;
import br.pucpr.papiro.exportacao.ExportadorMarkdown;
import br.pucpr.papiro.exportacao.Formatos;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class FormatosTest {

  @AfterEach
  void restaurar() {
    Formatos.instancia().restaurarPadroes();
  }

  @Test
  void existeUmaUnicaInstancia() {
    assertSame(Formatos.instancia(), Formatos.instancia());
  }

  @Test
  void escolheOExportadorPelaExtensao() {
    final var formatos = Formatos.instancia();

    assertInstanceOf(ExportadorHtml.class, formatos.paraArquivo(Path.of("saida/relatorio.HTML")));
    assertInstanceOf(ExportadorMarkdown.class, formatos.para(".md"));
  }

  @Test
  void aceitaFormatosNovos() {
    final Exportador soTitulo = Documento::titulo;
    Formatos.instancia().registrar("titulo", soTitulo);

    assertSame(soTitulo, Formatos.instancia().para("titulo"));
  }

  @Test
  void formatoDesconhecidoEhErro() {
    final var erro =
        assertThrows(IllegalArgumentException.class, () -> Formatos.instancia().para("pdf"));

    assertTrue(erro.getMessage().contains("[html, md, txt]"), erro.getMessage());
  }
}
