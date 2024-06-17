package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().get();
    }
}

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        private T elemento;
        /* El color del vértice. */
        private Color color;
        /* La lista de vecinos del vértice. */
        private Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            color = Color.NINGUNO;
            vecinos = new Lista<Vertice>();
        }
        /* Regresa el elemento del vértice. */
        @Override public T get() {
          return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
         return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
         return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }
}

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
     if(elemento == null || contiene(elemento)) throw new IllegalArgumentException("Elemento inválido");
 for (Vertice vertice : vertices) {
        if (vertice.get().equals(elemento)) {
            throw new IllegalArgumentException("El elemento ya había sido agregado a la gráfica.");
        }
    }
    vertices.agregaFinal(new Vertice(elemento));
}
    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
    if (a.equals(b))
        throw new IllegalArgumentException("Los vértices a conectar no pueden ser el mismo.");

    Vertice verticeA = null, verticeB = null;
    for (Vertice vertice : vertices) {
        if (vertice.get().equals(a))
            verticeA = vertice;
        else if (vertice.get().equals(b))
            verticeB = vertice;
    }

    if (verticeA == null || verticeB == null)
        throw new NoSuchElementException("Ambos elementos deben estar en la gráfica.");

    if (verticeA.vecinos.contiene(verticeB) || verticeB.vecinos.contiene(verticeA))
        throw new IllegalArgumentException("Los vértices ya están conectados.");

    verticeA.vecinos.agregaFinal(verticeB);
    verticeB.vecinos.agregaFinal(verticeA);
    aristas++; 
}

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
    if (a.equals(b))
        throw new IllegalArgumentException("Los vértices a desconectar no pueden ser el mismo.");

    Vertice verticeA = null, verticeB = null;
    for (Vertice vertice : vertices) {
        if (vertice.get().equals(a))
            verticeA = vertice;
        else if (vertice.get().equals(b))
            verticeB = vertice;
    }

    if (verticeA == null || verticeB == null)
        throw new NoSuchElementException("Ambos elementos deben estar en la gráfica.");

    if (!verticeA.vecinos.contiene(verticeB) || !verticeB.vecinos.contiene(verticeA))
        throw new IllegalArgumentException("Los vértices no están conectados.");

    verticeA.vecinos.elimina(verticeB);
    verticeB.vecinos.elimina(verticeA);
    aristas--;  // Decrementamos el contador de aristas ya que se ha eliminado una conexión

    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
        Vertice vertice = getVertice(elemento);
        return vertice != null;
    }
   

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
    Vertice verticeAEliminar = null;
    for (Vertice v : vertices) {
        if (v.elemento.equals(elemento)) {
            verticeAEliminar = v;
            break;
        }
    }

    if (verticeAEliminar == null) {
        throw new NoSuchElementException("El elemento no está contenido en la gráfica.");
    }

    for (Vertice vecino : verticeAEliminar.vecinos) {
        vecino.vecinos.elimina(verticeAEliminar);
        aristas--;  
    }


    vertices.elimina(verticeAEliminar);
}


    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
    if (a.equals(b)) {
        throw new IllegalArgumentException("Los elementos no pueden ser iguales.");
    }

    Vertice verticeA = null, verticeB = null;
    for (Vertice vertice : vertices) {
        if (vertice.get().equals(a))
            verticeA = vertice;
        if (vertice.get().equals(b))
            verticeB = vertice;
        if (verticeA != null && verticeB != null)
            break;
    }

    if (verticeA == null || verticeB == null)
        throw new NoSuchElementException("Uno o ambos elementos no están en la gráfica.");

    return verticeA.vecinos.contiene(verticeB);
}

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        if(elemento == null) throw new IllegalArgumentException("Elemento inválido");   
        for (Vertice v : vertices) {
        if (v.elemento.equals(elemento)) {
            return v;
        }
    }
    throw new NoSuchElementException("El elemento no es parte de la gráfica.");
}

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
    boolean encontrado = false;
    for (Vertice v : vertices) {
        if (v == vertice) { 
            v.color = color;
            encontrado = true;
            break;
        }
    }
    if (!encontrado) {
        throw new IllegalArgumentException("El vértice proporcionado no pertenece a la gráfica.");
    }
}

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        if(vertices.getLongitud() == 0 || vertices.getLongitud() == 1) return true;
        Cola<Vertice> estructura = new Cola<>();
        Vertice w = vertices.getPrimero();
        paraCadaVertice(v -> setColor(v, Color.ROJO));
        setColor(w, Color.NEGRO);
        estructura.mete(w);
        while(!estructura.esVacia()){
          Vertice aux = estructura.saca();
          for(Vertice v : aux.vecinos)
            if(v.getColor().equals(Color.ROJO)){
              setColor(v, Color.NEGRO);
              estructura.mete(v);
            }
        }
        for(Vertice v : vertices)
          if(!v.getColor().equals(Color.NEGRO))
            return false;
        paraCadaVertice(v -> setColor(v, Color.NINGUNO));
        return true;
    }


    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
    for (Vertice vertice : vertices) {
        accion.actua(vertice);
    }
}
    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
          if(elemento == null || !contiene(elemento)) throw new NoSuchElementException("El elemento "+elemento.toString()+" no está en la gráfica");
        auxiliarRecorrido(elemento, accion, new Cola<Vertice>());
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {

        if(elemento == null || !contiene(elemento)) throw new NoSuchElementException("El elemento "+elemento.toString()+" no está en la gráfica");
        auxiliarRecorrido(elemento, accion, new Pila<Vertice>());
    }
   

 private void auxiliarRecorrido(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> estructura){
      paraCadaVertice(v -> setColor(v, Color.ROJO));
      Vertice w = getVertice(elemento);
      setColor(w, Color.NEGRO);
      estructura.mete(w);
      while(!estructura.esVacia()){
        Vertice aux = estructura.saca();
        accion.actua(aux);
        for(Vertice v : aux.vecinos)
          if(v.getColor().equals(Color.ROJO)){
            setColor(v, Color.NEGRO);
            estructura.mete(v);
          }
      }
      paraCadaVertice(v -> setColor(v, Color.NINGUNO));
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
      return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        paraCadaVertice(v -> setColor(v, Color.ROJO));
        String s = "{";
        String e = "{";
        for(Vertice v : vertices){
           s += v.get() + ", ";
          for(Vertice vv : v.vecinos){
            if(vv.color == Color.ROJO)
              e += "(" + v.get() + ", " + vv.get() + "), ";
            v.color = Color.NEGRO;
          }
        }
        paraCadaVertice(v -> setColor(v, Color.NINGUNO));
        return s + "}, " + e + "}";
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        if(grafica.getAristas() != aristas || grafica.vertices.getLongitud() != vertices.getLongitud()) return false;
        /* Checamos si ambas gráficas tienen los mismos vértices */
        for(Vertice v : vertices){
          if(!grafica.contiene(v.get())) return false;
        }
        for(Vertice v : vertices){
          for(Vertice vg : v.vecinos){
            if(!grafica.sonVecinos(v.elemento, vg.elemento))
                return false;
          }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
    private Vertice getVertice(T elemento){
      if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
      for(Vertice vertice : vertices)
          if(vertice.get().equals(elemento)) return vertice;
      return null;
    }
}

