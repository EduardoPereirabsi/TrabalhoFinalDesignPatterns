package br.pucpr.papiro.exemplo;

import br.pucpr.papiro.Documento;
import br.pucpr.papiro.SalvamentoAutomatico;
import br.pucpr.papiro.elemento.Lista;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.elemento.Secao;
import br.pucpr.papiro.elemento.Tabela;
import br.pucpr.papiro.exportacao.ComRodape;
import br.pucpr.papiro.exportacao.ComSumario;
import br.pucpr.papiro.exportacao.ExportadorMarkdown;
import br.pucpr.papiro.exportacao.ExportadorTexto;
import br.pucpr.papiro.exportacao.Formatos;
import br.pucpr.papiro.numeracao.SemNumeracao;
import java.nio.file.Path;

public final class ExemploDeUso {
  private ExemploDeUso() {}

  public static void main(String[] args) {
    usoPadrao();
    usoConfigurado();
    usoEstendido();
  }

  private static void usoPadrao() {
    cabecalho("1. Uso padrao");

    final var relatorio =
        Documento.novo("Relatório de Vendas")
            .autor("Equipe Comercial")
            .paragrafo("Resumo das vendas do primeiro trimestre.")
            .secao("Resultados")
            .paragrafo("As vendas cresceram 12% em relação ao trimestre anterior.")
            .tabela("Mês", "Total")
            .linha("Janeiro", "R$ 10.000")
            .linha("Fevereiro", "R$ 11.500")
            .linha("Março", "R$ 12.300")
            .fimDaSecao()
            .secao("Próximos passos")
            .lista("Abrir a loja de Curitiba", "Contratar dois vendedores")
            .fimDaSecao()
            .construir();

    System.out.println(relatorio.exportar(new ExportadorMarkdown()));

    relatorio.salvar(Path.of("saida", "relatorio.html"));
    System.out.println("salvo em saida/relatorio.html");
    System.out.println(
        relatorio.contarPalavras()
            + " palavras, "
            + relatorio.buscar(Tabela.class).size()
            + " tabela(s)");
  }

  private static void usoConfigurado() {
    cabecalho("2. Configurando com as pecas prontas");

    final var ata =
        Documento.novo("Ata de Reunião")
            .numeracao(new SemNumeracao())
            .secao("Participantes")
            .lista("Ana", "Bruno", "Carla")
            .fimDaSecao()
            .secao("Decisões")
            .paragrafo("Aprovado o novo layout do site.")
            .fimDaSecao()
            .construir();

    final var exportador =
        new ComSumario(new ComRodape(new ExportadorTexto(), "Documento gerado pelo Papiro"));
    System.out.println(ata.exportar(exportador));

    ata.adicionarOuvinte(new SalvamentoAutomatico(Path.of("saida", "ata.md")));
    ata.adicionar(new Secao("Pendências", new Lista("Enviar o orçamento até sexta")));
    System.out.println("ata alterada e salva automaticamente em saida/ata.md");
  }

  private static void usoEstendido() {
    cabecalho("3. Cliente estendendo a biblioteca");

    Formatos.instancia().registrar("tex", new ExportadorLatex());

    final var artigo =
        Documento.novo("Padrões de Projeto na Prática")
            .autor("Equipe Papiro")
            .numeracao(new NumeracaoRomana())
            .secao("Introdução")
            .paragrafo("Este artigo mostra 8 padrões em uma biblioteca de verdade.")
            .fimDaSecao()
            .secao("Desenvolvimento")
            .secao("Builder")
            .paragrafo("Monta o documento passo a passo.")
            .construir();

    artigo.adicionarOuvinte(new ContadorDePalavras());
    artigo.adicionar(new Paragrafo("Parágrafo incluído depois do documento pronto."));

    System.out.println(artigo.exportar(new ComAvisoConfidencial(new ExportadorLatex())));

    artigo.salvar(Path.of("saida", "artigo.tex"));
    System.out.println("salvo em saida/artigo.tex");
  }

  private static void cabecalho(String titulo) {
    System.out.println();
    System.out.println("========== " + titulo + " ==========");
    System.out.println();
  }
}
