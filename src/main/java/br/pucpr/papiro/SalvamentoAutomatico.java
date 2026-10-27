package br.pucpr.papiro;

import java.nio.file.Path;
import java.util.Objects;

public class SalvamentoAutomatico implements OuvinteDocumento {
  private final Path arquivo;

  public SalvamentoAutomatico(Path arquivo) {
    this.arquivo = Objects.requireNonNull(arquivo, "arquivo");
  }

  @Override
  public void documentoAlterado(Documento documento) {
    documento.salvar(arquivo);
  }
}
