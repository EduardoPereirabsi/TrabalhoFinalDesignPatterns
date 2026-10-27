package br.pucpr.papiro.exportacao;

import br.pucpr.papiro.Documento;
import br.pucpr.papiro.elemento.Elemento;
import br.pucpr.papiro.elemento.Lista;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.elemento.Secao;
import br.pucpr.papiro.elemento.Tabela;
import br.pucpr.papiro.numeracao.Numeracao;
import java.util.ArrayList;
import java.util.List;

public abstract class ExportadorBase implements Exportador {

  @Override
  public final String exportar(Documento documento) {
    final var saida = new StringBuilder();
    saida.append(inicio(documento));
    escrever(documento.elementos(), List.of(), documento.numeracao(), saida);
    saida.append(fim(documento));
    return saida.toString();
  }

  private void escrever(
      List<Elemento> elementos, List<Integer> pai, Numeracao numeracao, StringBuilder saida) {
    var contador = 0;
    for (final var elemento : elementos) {
      switch (elemento) {
        case Paragrafo p -> saida.append(paragrafo(p));
        case Lista l -> saida.append(lista(l));
        case Tabela t -> saida.append(tabela(t));
        case Secao s -> {
          final var posicao = new ArrayList<>(pai);
          posicao.add(++contador);
          saida.append(secao(numeracao.titulo(posicao, s.titulo()), posicao.size()));
          escrever(s.elementos(), posicao, numeracao, saida);
          saida.append(fimDaSecao(posicao.size()));
        }
      }
    }
  }

  protected String inicio(Documento documento) {
    return "";
  }

  protected String fim(Documento documento) {
    return "";
  }

  protected abstract String secao(String titulo, int nivel);

  protected String fimDaSecao(int nivel) {
    return "";
  }

  protected abstract String paragrafo(Paragrafo paragrafo);

  protected abstract String lista(Lista lista);

  protected abstract String tabela(Tabela tabela);
}
