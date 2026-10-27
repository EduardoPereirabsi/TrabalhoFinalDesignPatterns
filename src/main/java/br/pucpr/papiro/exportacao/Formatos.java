package br.pucpr.papiro.exportacao;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class Formatos {
  private static final Formatos INSTANCIA = new Formatos();

  private final Map<String, Exportador> exportadores = new ConcurrentHashMap<>();

  private Formatos() {
    restaurarPadroes();
  }

  public static Formatos instancia() {
    return INSTANCIA;
  }

  public void registrar(String extensao, Exportador exportador) {
    exportadores.put(normalizar(extensao), Objects.requireNonNull(exportador, "exportador"));
  }

  public Exportador para(String extensao) {
    final var exportador = exportadores.get(normalizar(extensao));
    if (exportador == null) {
      throw new IllegalArgumentException(
          "formato desconhecido: " + extensao + " (registrados: " + extensoes() + ")");
    }
    return exportador;
  }

  public Exportador paraArquivo(Path arquivo) {
    final var nome = arquivo.getFileName().toString();
    final var ponto = nome.lastIndexOf('.');
    if (ponto < 0) {
      throw new IllegalArgumentException("o arquivo " + nome + " nao tem extensao");
    }
    return para(nome.substring(ponto + 1));
  }

  public List<String> extensoes() {
    return exportadores.keySet().stream().sorted().toList();
  }

  public void restaurarPadroes() {
    exportadores.clear();
    registrar("html", new ExportadorHtml());
    registrar("md", new ExportadorMarkdown());
    registrar("txt", new ExportadorTexto());
  }

  private static String normalizar(String extensao) {
    final var minuscula = extensao.toLowerCase(Locale.ROOT);
    return minuscula.startsWith(".") ? minuscula.substring(1) : minuscula;
  }
}
