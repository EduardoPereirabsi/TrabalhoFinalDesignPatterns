package br.pucpr.papiro.exemplo;

import br.pucpr.papiro.Documento;
import br.pucpr.papiro.elemento.Elemento;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.exportacao.Exportador;
import br.pucpr.papiro.exportacao.ExportadorDecorator;
import java.util.ArrayList;

public class ComAvisoConfidencial extends ExportadorDecorator {

  public ComAvisoConfidencial(Exportador exportador) {
    super(exportador);
  }

  @Override
  public String exportar(Documento documento) {
    final var elementos = new ArrayList<Elemento>();
    elementos.add(new Paragrafo("CONFIDENCIAL - uso interno"));
    elementos.addAll(documento.elementos());
    return super.exportar(documento.comElementos(elementos));
  }
}
