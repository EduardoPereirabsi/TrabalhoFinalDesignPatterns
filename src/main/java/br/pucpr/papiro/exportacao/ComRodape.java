package br.pucpr.papiro.exportacao;

import br.pucpr.papiro.Documento;
import br.pucpr.papiro.elemento.Elemento;
import br.pucpr.papiro.elemento.Paragrafo;
import java.util.ArrayList;
import java.util.Objects;

public class ComRodape extends ExportadorDecorator {
  private final String texto;

  public ComRodape(Exportador exportador, String texto) {
    super(exportador);
    this.texto = Objects.requireNonNull(texto, "texto");
  }

  @Override
  public String exportar(Documento documento) {
    final var elementos = new ArrayList<Elemento>(documento.elementos());
    elementos.add(new Paragrafo(texto));
    return super.exportar(documento.comElementos(elementos));
  }
}
