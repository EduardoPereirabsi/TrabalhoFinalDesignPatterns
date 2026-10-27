package br.pucpr.papiro;

import br.pucpr.papiro.elemento.Elemento;
import br.pucpr.papiro.exportacao.Exportador;
import br.pucpr.papiro.exportacao.Formatos;
import br.pucpr.papiro.numeracao.Numeracao;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class Documento implements Iterable<Elemento> {
  private final String titulo;
  private final String autor;
  private final Numeracao numeracao;
  private final List<Elemento> elementos;
  private final List<OuvinteDocumento> ouvintes = new ArrayList<>();

  Documento(String titulo, String autor, Numeracao numeracao, List<Elemento> elementos) {
    this.titulo = Objects.requireNonNull(titulo, "titulo");
    this.autor = Objects.requireNonNull(autor, "autor");
    this.numeracao = Objects.requireNonNull(numeracao, "numeracao");
    this.elementos = new ArrayList<>(elementos);
  }

  public static DocumentoBuilder novo(String titulo) {
    return new DocumentoBuilder(titulo);
  }

  public String titulo() {
    return titulo;
  }

  public String autor() {
    return autor;
  }

  public Numeracao numeracao() {
    return numeracao;
  }

  public List<Elemento> elementos() {
    return Collections.unmodifiableList(elementos);
  }

  public void adicionar(Elemento elemento) {
    elementos.add(Objects.requireNonNull(elemento, "elemento"));
    notificar();
  }

  public boolean remover(Elemento elemento) {
    final var removeu = elementos.remove(elemento);
    if (removeu) {
      notificar();
    }
    return removeu;
  }

  public void adicionarOuvinte(OuvinteDocumento ouvinte) {
    ouvintes.add(Objects.requireNonNull(ouvinte, "ouvinte"));
  }

  public void removerOuvinte(OuvinteDocumento ouvinte) {
    ouvintes.remove(ouvinte);
  }

  public int contarPalavras() {
    var total = 0;
    for (final var elemento : elementos) {
      total += elemento.contarPalavras();
    }
    return total;
  }

  public <T extends Elemento> List<T> buscar(Class<T> tipo) {
    final var encontrados = new ArrayList<T>();
    for (final var elemento : this) {
      if (tipo.isInstance(elemento)) {
        encontrados.add(tipo.cast(elemento));
      }
    }
    return encontrados;
  }

  @Override
  public Iterator<Elemento> iterator() {
    return new IteradorDeElementos(elementos);
  }

  public String exportar(Exportador exportador) {
    return exportador.exportar(this);
  }

  public void salvar(Path arquivo) {
    salvar(arquivo, Formatos.instancia().paraArquivo(arquivo));
  }

  public void salvar(Path arquivo, Exportador exportador) {
    try {
      final var pasta = arquivo.toAbsolutePath().getParent();
      if (pasta != null) {
        Files.createDirectories(pasta);
      }
      Files.writeString(arquivo, exportar(exportador));
    } catch (IOException e) {
      throw new UncheckedIOException("nao foi possivel salvar " + arquivo, e);
    }
  }

  public Documento comElementos(List<Elemento> novosElementos) {
    return new Documento(titulo, autor, numeracao, novosElementos);
  }

  private void notificar() {
    for (final var ouvinte : List.copyOf(ouvintes)) {
      ouvinte.documentoAlterado(this);
    }
  }
}
