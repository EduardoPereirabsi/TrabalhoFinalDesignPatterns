package br.pucpr.papiro.exportacao;

import br.pucpr.papiro.Documento;

@FunctionalInterface
public interface Exportador {
  String exportar(Documento documento);
}
