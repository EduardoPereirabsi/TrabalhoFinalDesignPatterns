package br.pucpr.papiro.exemplo;

import br.pucpr.papiro.Documento;
import br.pucpr.papiro.OuvinteDocumento;

public class ContadorDePalavras implements OuvinteDocumento {
  private int ultimaContagem;

  @Override
  public void documentoAlterado(Documento documento) {
    ultimaContagem = documento.contarPalavras();
    System.out.println("[contador] " + documento.titulo() + ": " + ultimaContagem + " palavras");
  }

  public int ultimaContagem() {
    return ultimaContagem;
  }
}
