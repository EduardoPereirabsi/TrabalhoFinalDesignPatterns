package br.pucpr.papiro;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.pucpr.papiro.elemento.Paragrafo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class OuvinteTest {

  @Test
  void avisaOsOuvintesQuandoODocumentoMuda() {
    final var documento = Documento.novo("x").construir();
    final var avisos = new ArrayList<Integer>();
    documento.adicionarOuvinte(d -> avisos.add(d.elementos().size()));

    documento.adicionar(new Paragrafo("um"));
    documento.adicionar(new Paragrafo("dois"));
    documento.remover(new Paragrafo("um"));

    assertEquals(List.of(1, 2, 1), avisos);
  }

  @Test
  void ouvinteRemovidoNaoEhMaisAvisado() {
    final var documento = Documento.novo("x").construir();
    final var avisos = new ArrayList<Documento>();
    final OuvinteDocumento ouvinte = avisos::add;
    documento.adicionarOuvinte(ouvinte);

    documento.removerOuvinte(ouvinte);
    documento.adicionar(new Paragrafo("um"));

    assertEquals(List.of(), avisos);
  }

  @Test
  void salvamentoAutomaticoGravaOArquivoACadaMudanca(@TempDir Path pasta) throws IOException {
    final var arquivo = pasta.resolve("rascunho.md");
    final var documento = Documento.novo("Rascunho").construir();
    documento.adicionarOuvinte(new SalvamentoAutomatico(arquivo));

    documento.adicionar(new Paragrafo("primeira linha"));

    assertEquals("# Rascunho\n\nprimeira linha\n\n", Files.readString(arquivo));
  }
}
