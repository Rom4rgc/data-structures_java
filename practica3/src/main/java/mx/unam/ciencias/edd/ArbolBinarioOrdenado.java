package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
          pila = new Pila<Vertice>();
          Vertice v = raiz;
          while(v != null){
            pila.mete(v);
            v = v.izquierdo;
          }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
    return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            // Aquí va su código.
    if (!hasNext()) {
        throw new NoSuchElementException("No hay más elementos en el árbol.");
    }

    Vertice actual = pila.saca();
    T elemento = actual.elemento;

    if (actual.derecho != null) {
        Vertice v = actual.derecho;
        while (v != null) {
            pila.mete(v);
            v = v.izquierdo;
        }
    }

    return elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
    if (elemento == null) {
        throw new IllegalArgumentException("Elemento inválido");
    }

    Vertice nuevo = nuevoVertice(elemento);
    if (raiz == null) {
        raiz = nuevo;
    } else {
        Vertice v = raiz;
        Vertice p = null;
        while (v != null) {
            p = v;
            if (elemento.compareTo(v.elemento) <= 0) {
                v = v.izquierdo;
            } else {
                v = v.derecho;
            }
        }

        if (elemento.compareTo(p.elemento) <= 0) {
            p.izquierdo = nuevo;
        } else {
            p.derecho = nuevo;
        }
        nuevo.padre = p;
    }

    ultimoAgregado = nuevo;
    elementos++;
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
    Vertice v = vertice(busca(elemento));
    if (v == null) return;

    elementos--;

    if (v.hayIzquierdo() && v.hayDerecho()) {
        Vertice eliminable = intercambiaEliminable(v);
        v.elemento = eliminable.elemento;
        eliminaVertice(eliminable);
    } else {
        eliminaVertice(v);
    }
}

private Vertice intercambiaEliminable(Vertice v) {
    return maximoEnSubArbol(v.izquierdo);
}

private Vertice maximoEnSubArbol(Vertice v) {
    if (!v.hayDerecho()) return v;
    return maximoEnSubArbol(v.derecho);
}

private void eliminaVertice(Vertice v) {
    if (v.hayIzquierdo()) {
        reemplaza(v, v.izquierdo);
    } else {
        reemplaza(v, v.derecho);
    }
}

private void reemplaza(Vertice viejo, Vertice nuevo) {
    if (viejo == raiz) {
        raiz = nuevo;
        if (nuevo != null) {
            nuevo.padre = null;
        }
    } else {
        Vertice padre = viejo.padre;
        if (viejo == padre.izquierdo) {
            padre.izquierdo = nuevo;
        } else {
            padre.derecho = nuevo;
        }
        if (nuevo != null) {
            nuevo.padre = padre;
        }
    }
}

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        // Aquí va su código.
    Vertice sucesor = maximoEnSubArbol(vertice.izquierdo);
    T temp = vertice.elemento;
    vertice.elemento = sucesor.elemento;
    sucesor.elemento = temp;
    return sucesor;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        // Aquí va su código.
    if (!vertice.hayIzquierdo() && !vertice.hayDerecho()) {
        // Caso 1: El vértice no tiene hijos
        if (vertice == raiz) {
            raiz = null; 
        } else {
            if (vertice.padre.izquierdo == vertice)
                vertice.padre.izquierdo = null;
            else
                vertice.padre.derecho = null; 
        }
    } else if (!vertice.hayIzquierdo() || !vertice.hayDerecho()) {
        // Caso 2: El vértice tiene un hijo
        Vertice hijo = (vertice.hayIzquierdo()) ? vertice.izquierdo : vertice.derecho;
        if (vertice == raiz) {
            raiz = hijo; 
            hijo.padre = null;
        } else {
            hijo.padre = vertice.padre;
            if (vertice.padre.izquierdo == vertice)
                vertice.padre.izquierdo = hijo; 
            else
                vertice.padre.derecho = hijo; 
        }
    } else {
        // Caso 3: El vértice tiene dos hijos
        Vertice sucesor = intercambiaEliminable(vertice);
        eliminaVertice(sucesor); 
    }
}

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
    return busca(elemento, raiz);
}

private VerticeArbolBinario<T> busca(T elemento, VerticeArbolBinario<T> vertice) {
    if (vertice == null || vertice.elemento.equals(elemento))
        return vertice;
    if (elemento.compareTo(vertice.elemento) < 0)
        return busca(elemento, vertice.izquierdo);
    else
        return busca(elemento, vertice.derecho);
}

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> v) {
      if (v == null || !v.hayIzquierdo())
            return;
        Vertice q = vertice(v);
        Vertice p = q.izquierdo;
        p.padre = q.padre;
        if(q != this.raiz)
            if(q.padre.izquierdo == q )
                q.padre.izquierdo = p;
            else
                q.padre.derecho = p;
        else
            raiz = p;
        q.izquierdo = p.derecho;
        if(p.hayDerecho())
            p.derecho.padre = q;
        p.derecho = q;
        q.padre = p;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> v) {
        if(v == null || !v.hayDerecho())
          return;
        Vertice p = vertice(v);
        Vertice q = p.derecho;
        q.padre = p.padre;
        if(this.raiz != p){
          if(p.padre.izquierdo == p)
            p.padre.izquierdo = q;
          else
            p.padre.derecho = q;
        }
        else
          this.raiz = q;
        p.derecho = q.izquierdo;
        if(q.hayIzquierdo())
          q.izquierdo.padre = p;
        q.izquierdo = p;
        p.padre = q;
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
       dfsPreOrder(this.raiz, accion);
    }

    private void dfsPreOrder(Vertice vertice, AccionVerticeArbolBinario<T> accion){
      if(vertice == null) return;
      accion.actua(vertice);
      dfsPreOrder(vertice.izquierdo, accion);
      dfsPreOrder(vertice.derecho, accion );
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
      dfsInOrder(this.raiz, accion);
    }

    private void dfsInOrder(Vertice vertice, AccionVerticeArbolBinario<T> accion){
      if(vertice == null) return;
      dfsInOrder(vertice.izquierdo, accion);
      accion.actua(vertice);
      dfsInOrder(vertice.derecho, accion );
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
      dfsPostOrder(this.raiz, accion);
    }

    private void dfsPostOrder(Vertice vertice, AccionVerticeArbolBinario<T> accion){
      if(vertice == null) return;
      dfsPostOrder(vertice.izquierdo, accion);
      dfsPostOrder(vertice.derecho, accion );
      accion.actua(vertice);
    }
    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
