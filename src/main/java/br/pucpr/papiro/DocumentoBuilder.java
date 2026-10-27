package br.pucpr.papiro;

import br.pucpr.papiro.elemento.Elemento;
import br.pucpr.papiro.elemento.Lista;
import br.pucpr.papiro.elemento.Paragrafo;
import br.pucpr.papiro.elemento.Secao;
import br.pucpr.papiro.elemento.Tabela;
import br.pucpr.papiro.numeracao.Numeracao;
import br.pucpr.papiro.numeracao.NumeracaoDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public final class DocumentoBuilder {
  private final String titulo;
  private String autor = "";
  private Numeracao numeracao = new NumeracaoDecimal();
  private final List<Elemento> elementos = new ArrayList<>();
  private final Deque<SecaoAberta> secoesAbertas = new ArrayDeque<>();

  DocumentoBuilder(String titulo) {
    this.titulo = Objects.requireNonNull(titulo, "titulo");
  }

  public DocumentoBuilder autor(String autor) {
    this.autor = Objects.requireNonNull(autor, "autor");
    return this;
  }

  public DocumentoBuilder numeracao(Numeracao numeracao) {
    this.numeracao = Objects.requireNonNull(numeracao, "numeracao");
    return this;
  }

  public DocumentoBuilder paragrafo(String texto) {
    atual().add(new Paragrafo(texto));
    return this;
  }

  public DocumentoBuilder lista(String... itens) {
    atual().add(new Lista(itens));
    return this;
  }

  public DocumentoBuilder tabela(String... cabecalho) {
    atual().add(new Tabela(cabecalho));
    return this;
  }

  public DocumentoBuilder linha(String... celulas) {
    final var atual = atual();
    if (atual.isEmpty() || !(atual.getLast() instanceof Tabela tabela)) {
      throw new IllegalStateException("linha() precisa vir logo depois de tabela()");
    }
    atual.set(atual.size() - 1, tabela.comLinha(celulas));
    return this;
  }

  public DocumentoBuilder secao(String titulo) {
    secoesAbertas.push(new SecaoAberta(titulo, new ArrayList<>()));
    return this;
  }

  public DocumentoBuilder fimDaSecao() {
    if (secoesAbertas.isEmpty()) {
      throw new IllegalStateException("nenhuma secao aberta para fechar");
    }
    final var secao = secoesAbertas.pop();
    atual().add(new Secao(secao.titulo(), secao.elementos()));
    return this;
  }

  public Documento construir() {
    while (!secoesAbertas.isEmpty()) {
      fimDaSecao();
    }
    return new Documento(titulo, autor, numeracao, elementos);
  }

  private List<Elemento> atual() {
    return secoesAbertas.isEmpty() ? elementos : secoesAbertas.peek().elementos();
  }

  private record SecaoAberta(String titulo, List<Elemento> elementos) {}
}
