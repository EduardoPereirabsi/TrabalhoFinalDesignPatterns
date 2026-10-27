package br.pucpr.papiro;

import br.pucpr.papiro.elemento.Elemento;
import br.pucpr.papiro.elemento.Secao;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

final class IteradorDeElementos implements Iterator<Elemento> {
  private final Deque<Iterator<Elemento>> pilha = new ArrayDeque<>();

  IteradorDeElementos(List<Elemento> elementos) {
    pilha.push(elementos.iterator());
  }

  @Override
  public boolean hasNext() {
    while (!pilha.isEmpty() && !pilha.peek().hasNext()) {
      pilha.pop();
    }
    return !pilha.isEmpty();
  }

  @Override
  public Elemento next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    final var elemento = pilha.peek().next();
    if (elemento instanceof Secao secao) {
      pilha.push(secao.elementos().iterator());
    }
    return elemento;
  }
}
