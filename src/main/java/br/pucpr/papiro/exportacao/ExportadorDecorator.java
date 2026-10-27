package br.pucpr.papiro.exportacao;

import br.pucpr.papiro.Documento;
import java.util.Objects;

public abstract class ExportadorDecorator implements Exportador {
  private final Exportador exportador;

  protected ExportadorDecorator(Exportador exportador) {
    this.exportador = Objects.requireNonNull(exportador, "exportador");
  }

  @Override
  public String exportar(Documento documento) {
    return exportador.exportar(documento);
  }
}
