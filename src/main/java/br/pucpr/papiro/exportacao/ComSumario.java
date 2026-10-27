package br.pucpr.papiro.exportacao;

import br.pucpr.papiro.Documento;
import br.pucpr.papiro.elemento.Elemento;
import br.pucpr.papiro.elemento.Lista;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.elemento.Secao;
import br.pucpr.papiro.numeracao.Numeracao;
import java.util.ArrayList;
import java.util.List;

public class ComSumario extends ExportadorDecorator {

  public ComSumario(Exportador exportador) {
    super(exportador);
  }

  @Override
  public String exportar(Documento documento) {
    final var itens = new ArrayList<String>();
    coletarTitulos(documento.elementos(), List.of(), documento.numeracao(), itens);
    if (itens.isEmpty()) {
      return super.exportar(documento);
    }
    final var elementos = new ArrayList<Elemento>();
    elementos.add(new Paragrafo("Sumário"));
    elementos.add(new Lista(itens));
    elementos.addAll(documento.elementos());
    return super.exportar(documento.comElementos(elementos));
  }

  private void coletarTitulos(
      List<Elemento> elementos, List<Integer> pai, Numeracao numeracao, List<String> itens) {
    var contador = 0;
    for (final var elemento : elementos) {
      if (elemento instanceof Secao secao) {
        final var posicao = new ArrayList<>(pai);
        posicao.add(++contador);
        itens.add(numeracao.titulo(posicao, secao.titulo()));
        coletarTitulos(secao.elementos(), posicao, numeracao, itens);
      }
    }
  }
}
