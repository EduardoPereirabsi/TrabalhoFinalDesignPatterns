package br.pucpr.papiro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.pucpr.papiro.elemento.Lista;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.elemento.Secao;
import br.pucpr.papiro.elemento.Tabela;
import java.util.List;
import org.junit.jupiter.api.Test;

class DocumentoBuilderTest {

  @Test
  void montaSecoesDentroDeSecoes() {
    final var documento =
        Documento.novo("Relatorio")
            .autor("Ana")
            .paragrafo("introducao")
            .secao("Vendas")
            .paragrafo("cresceram")
            .secao("Detalhes")
            .lista("a", "b")
            .fimDaSecao()
            .fimDaSecao()
            .construir();

    assertEquals("Ana", documento.autor());
    assertEquals(
        List.of(
            new Paragrafo("introducao"),
            new Secao(
                "Vendas", new Paragrafo("cresceram"), new Secao("Detalhes", new Lista("a", "b")))),
        documento.elementos());
  }

  @Test
  void linhasEntramNaTabelaAnterior() {
    final var documento =
        Documento.novo("x")
            .tabela("Mes", "Total")
            .linha("Jan", "10")
            .linha("Fev", "20")
            .construir();

    final var tabela = (Tabela) documento.elementos().getFirst();
    assertEquals(List.of(List.of("Jan", "10"), List.of("Fev", "20")), tabela.linhas());
  }

  @Test
  void linhaSemTabelaEhErro() {
    final var builder = Documento.novo("x").paragrafo("texto");

    assertThrows(IllegalStateException.class, () -> builder.linha("a"));
  }

  @Test
  void linhaComQuantidadeErradaDeColunasEhErro() {
    final var builder = Documento.novo("x").tabela("a", "b");

    assertThrows(IllegalArgumentException.class, () -> builder.linha("so uma"));
  }

  @Test
  void construirFechaAsSecoesAbertas() {
    final var documento = Documento.novo("x").secao("A").paragrafo("p").construir();

    assertEquals(List.of(new Secao("A", new Paragrafo("p"))), documento.elementos());
  }
}
