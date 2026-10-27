package br.pucpr.papiro;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.pucpr.papiro.elemento.Lista;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.elemento.Secao;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ComposicaoTest {
  private final Documento documento =
      Documento.novo("x")
          .paragrafo("um dois")
          .secao("tres quatro")
          .tabela("cinco", "seis")
          .linha("sete", "oito nove")
          .secao("dez")
          .lista("onze", "doze treze")
          .construir();

  @Test
  void secaoSomaAsPalavrasDosFilhos() {
    final var secao =
        new Secao("titulo", new Paragrafo("a b"), new Secao("sub", new Paragrafo("c")));

    assertEquals(5, secao.contarPalavras());
  }

  @Test
  void documentoContaAsPalavrasDaArvoreInteira() {
    assertEquals(13, documento.contarPalavras());
  }

  @Test
  void iteradorPercorreTodosOsNiveis() {
    final var tipos = new ArrayList<String>();
    for (final var elemento : documento) {
      tipos.add(elemento.getClass().getSimpleName());
    }

    assertEquals(List.of("Paragrafo", "Secao", "Tabela", "Secao", "Lista"), tipos);
  }

  @Test
  void buscaEncontraElementosDentroDeSubsecoes() {
    assertEquals(List.of(new Lista("onze", "doze treze")), documento.buscar(Lista.class));
    assertEquals(2, documento.buscar(Secao.class).size());
  }
}
