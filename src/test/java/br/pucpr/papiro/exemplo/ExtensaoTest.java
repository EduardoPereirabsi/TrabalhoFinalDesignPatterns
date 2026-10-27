package br.pucpr.papiro.exemplo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.pucpr.papiro.Documento;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.exportacao.ExportadorMarkdown;
import br.pucpr.papiro.exportacao.Formatos;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExtensaoTest {
  private final Documento artigo =
      Documento.novo("Artigo")
          .numeracao(new NumeracaoRomana())
          .secao("Introducao")
          .paragrafo("50% & mais")
          .fimDaSecao()
          .secao("Desenvolvimento")
          .secao("Metodo")
          .construir();

  @AfterEach
  void restaurar() {
    Formatos.instancia().restaurarPadroes();
  }

  @Test
  void clienteCriaUmFormatoNovo(@TempDir Path pasta) throws IOException {
    Formatos.instancia().registrar("tex", new ExportadorLatex());
    final var arquivo = pasta.resolve("artigo.tex");

    artigo.salvar(arquivo);

    final var latex = Files.readString(arquivo);
    assertTrue(latex.contains("\\section*{I Introducao}"), latex);
    assertTrue(latex.contains("\\subsection*{II.1 Metodo}"), latex);
    assertTrue(latex.contains("50\\% \\& mais"), latex);
  }

  @Test
  void clienteCriaUmaNumeracaoNova() {
    final var romana = new NumeracaoRomana();

    assertEquals("XIV", romana.numerar(List.of(14)));
    assertEquals("IV.2", romana.numerar(List.of(4, 2)));
  }

  @Test
  void clienteCriaUmDecoradorNovo() {
    final var md = artigo.exportar(new ComAvisoConfidencial(new ExportadorMarkdown()));

    assertTrue(md.startsWith("# Artigo\n\nCONFIDENCIAL"), md);
  }

  @Test
  void clienteCriaUmOuvinteNovo() {
    final var contador = new ContadorDePalavras();
    artigo.adicionarOuvinte(contador);

    artigo.adicionar(new Paragrafo("tres palavras novas"));

    assertEquals(artigo.contarPalavras(), contador.ultimaContagem());
  }
}
