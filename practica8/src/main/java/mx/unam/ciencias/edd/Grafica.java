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
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        private T elemento;
        /* El color del vértice. */
        private Color color;
        /* La distancia del vértice. */
        private double distancia;
        /* El índice del vértice. */
        private int indice;
        /* La lista de vecinos del vértice. */
        private Lista<Vecino> vecinos;
        
        private Vertice predecesor;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            color = Color.NINGUNO;
            this.vecinos = new Lista<Vecino>();
            this.distancia = Double.POSITIVE_INFINITY; 
            this.predecesor = null; 
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

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            if (distancia > vertice.distancia)
                return 1;
            else if (distancia < vertice.distancia)
                return -1;
            return 0;
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
        this.vecino = vecino;
        this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
        return vecino.get();
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
        return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
        return vecino.getColor();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
        return vecino.vecinos();
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino<T> {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica<T>.Vertice v, Grafica<T>.Vecino a);
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
        conecta(a, b, 1.0);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */

    public void conecta(T a, T b, double peso) {

    if (a.equals(b))
        throw new IllegalArgumentException("No se puede conectar un elemento consigo mismo.");
    if (peso <= 0)
        throw new IllegalArgumentException("El peso debe ser positivo.");

    Vertice verticeA = buscaVertice(a);
    Vertice verticeB = buscaVertice(b);

    if (verticeA == null || verticeB == null)
        throw new NoSuchElementException("Uno o ambos elementos no existen en la gráfica.");

    if (sonVecinos(verticeA, verticeB))
        throw new IllegalArgumentException("Los elementos ya están conectados.");

    verticeA.vecinos.agregaFinal(new Vecino(verticeB, peso));
    verticeB.vecinos.agregaFinal(new Vecino(verticeA, peso));
    aristas++;
}
// Método para buscar vértices, ya existente pero crucial para asegurar que los elementos pueden ser manejados como se espera
private Vertice buscaVertice(T elemento) {
    for (Vertice v : vertices) {
        if (v.get().equals(elemento)) {
            return v;
        }
    }
    return null;
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
        throw new IllegalArgumentException("No se puede desconectar un elemento consigo mismo.");

    Vertice verticeA = buscaVertice(a);
    Vertice verticeB = buscaVertice(b);

    if (verticeA == null || verticeB == null)
        throw new NoSuchElementException("Uno o ambos elementos no existen en la gráfica.");

    if (!sonVecinos(verticeA, verticeB))
        throw new IllegalArgumentException("Los elementos no están conectados.");



        Vecino vecinoB = obtenerVecino(verticeA.vecinos, verticeB);
        Vecino vecinoA = obtenerVecino(verticeB.vecinos, verticeA);

        verticeA.vecinos.elimina(vecinoB);
        verticeB.vecinos.elimina(vecinoA);

        aristas--;
    }

 private Vecino obtenerVecino(Lista<Vecino> lista, Vertice vertice){
      for(Vecino vec : lista){
        if(vec.vecino == vertice) return vec;
      }
      return null;
    }

private void removeVecino(Vertice vertice, Vertice aEliminar) {
    Iterator<Vecino> it = vertice.vecinos.iterator();
    while (it.hasNext()) {
        Vecino vecino = it.next();
        if (vecino.vecino.equals(aEliminar)) {
            it.remove();
            break;
        }
    }
}
    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
@Override public boolean contiene(T elemento) {
    if (elemento == null) throw new IllegalArgumentException("Elemento inválido");
    return buscaVertice(elemento) != null;
}
    

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
@Override public void elimina(T elemento) {
    if (elemento == null) throw new IllegalArgumentException("Elemento inválido");

    Vertice vertice = buscaVertice(elemento);  // Usamos buscaVertice para obtener el vértice correspondiente
    if (vertice == null) throw new NoSuchElementException("No se encontró el elemento " + elemento.toString());

        int aristasEliminados = 0;

        for(Vecino v : vertice.vecinos){
          for(Vecino vecinoDelVecino : v.vecino.vecinos){
            if(vecinoDelVecino.vecino == vertice){
              v.vecino.vecinos.elimina(vecinoDelVecino);
              aristasEliminados++;
            }
          }
        }
        vertices.elimina(vertice);
        this.aristas -= aristasEliminados;
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
    Vertice verticeA = buscaVertice(a);
    Vertice verticeB = buscaVertice(b);

    if (verticeA == null || verticeB == null)
        throw new NoSuchElementException("Uno de los elementos no existe en la gráfica.");

    return sonVecinos(verticeA, verticeB);
}

private boolean sonVecinos(Vertice verticeA, Vertice verticeB) {
    for (Vecino vecino : verticeA.vecinos) {
        if (vecino.vecino.equals(verticeB)) {
            return true;
        }
    }
    return false;
}

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
    Vertice verticeA = buscaVertice(a);
    Vertice verticeB = buscaVertice(b);

    if (verticeA == null || verticeB == null)
        throw new NoSuchElementException("Uno o ambos elementos no están en la gráfica.");

    for (Vecino vecino : verticeA.vecinos) {
        if (vecino.vecino.equals(verticeB)) {
            return vecino.peso;
        }
    }

    throw new IllegalArgumentException("Los elementos no están conectados.");
}

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
    if (peso <= 0)
        throw new IllegalArgumentException("El peso debe ser mayor que cero.");

    Vertice verticeA = buscaVertice(a);
    Vertice verticeB = buscaVertice(b);

    if (verticeA == null || verticeB == null)
        throw new NoSuchElementException("Uno o ambos elementos no están en la gráfica.");

    boolean conectados = false;
    for (Vecino vecino : verticeA.vecinos) {
        if (vecino.vecino.equals(verticeB)) {
            vecino.peso = peso;
            conectados = true;
        }
    }

    for (Vecino vecino : verticeB.vecinos) {
        if (vecino.vecino.equals(verticeA)) {
            vecino.peso = peso;  
    }

    if (!conectados)
        throw new IllegalArgumentException("Los elementos no están conectados.");
}
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
        if(vertice == null || (vertice.getClass() != Vertice.class && vertice.getClass() != Vecino.class))
          throw new IllegalArgumentException("Vértice inválido");
        if(vertice.getClass() == Vertice.class){
          Vertice v = (Vertice) vertice;
          v.color = color;
        }
        if(vertice.getClass() == Vecino.class){
          Vecino v = (Vecino) vertice;
          v.vecino.color = color;
        }
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
public boolean esConexa() {
    if (vertices.getLongitud() == 0 || vertices.getLongitud() == 1) return true;
    Cola<Vertice> estructura = new Cola<>();
    Vertice w = vertices.getPrimero();
    paraCadaVertice(v -> setColor(v, Color.ROJO));
    setColor(w, Color.NEGRO);
    estructura.mete(w);
    while (!estructura.esVacia()) {
        Vertice aux = estructura.saca();
        for (Vecino vecino : aux.vecinos) {  // Aquí accedemos a los vecinos correctamente
            Vertice v = vecino.vecino;  // Obtenemos el vértice asociado al vecino
            if (v.getColor().equals(Color.ROJO)) {
                setColor(v, Color.NEGRO);
                estructura.mete(v);
            }
        }
    }
    for (Vertice v : vertices) {
        if (!v.getColor().equals(Color.NEGRO))
            return false;
    }
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
        recorridofs(elemento, accion, new Cola<Vertice>());
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
        recorridofs(elemento, accion, new Pila<Vertice>());
    }


private void recorridofs(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> estructura) {
    paraCadaVertice(v -> setColor(v, Color.ROJO));
    Vertice w = getVertice(elemento);
    setColor(w, Color.NEGRO);
    estructura.mete(w);
    while (!estructura.esVacia()) {
        Vertice aux = estructura.saca();
        accion.actua(aux);
        for (Vecino vecino : aux.vecinos) {  // Usar Vecino para iterar
            Vertice v = vecino.vecino;  // Acceder al Vertice dentro del Vecino
            if (v.getColor().equals(Color.ROJO)) {
                setColor(v, Color.NEGRO);
                estructura.mete(v);
            }
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
          for(Vecino vv : v.vecinos){
            if(vv.getColor() == Color.ROJO)
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


        for(Vertice v : vertices){
          if(!grafica.contiene(v.get())) return false;
        }
        for(Vertice v : vertices){
          for(Vecino vg : v.vecinos){
            if(!grafica.sonVecinos(v.elemento, vg.get()))
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

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
public Lista<VerticeGrafica<T>> trayectoriaMinima(T origenT, T destinoT) {
    if (origenT == null || destinoT == null)
        throw new IllegalArgumentException("Los elementos de origen y destino no pueden ser nulos.");

    Vertice origen = getVertice(origenT);
    Vertice destino = getVertice(destinoT);

    if (origen == null || destino == null)
        throw new NoSuchElementException("El origen y/o destino no existen en la gráfica.");

    if (origen == destino) {
        Lista<VerticeGrafica<T>> ruta = new Lista<>();
        ruta.agrega(origen);
        return ruta;
    }

    inicializaDistancias();
    origen.distancia = 0;
    Cola<Vertice> cola = new Cola<>();
    cola.mete(origen);

    while (!cola.esVacia()) {
        Vertice vertice = cola.saca();
        for (Vecino vecino : vertice.vecinos) {
            if (vecino.vecino.distancia == Double.POSITIVE_INFINITY) {
                vecino.vecino.distancia = vertice.distancia + 1;
                vecino.vecino.predecesor = vertice;
                cola.mete(vecino.vecino);
            }
        }
    }

    return reconstruyeTrayectoria(destino);
}


    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
public Lista<VerticeGrafica<T>> dijkstra(T origenT, T destinoT) {
    if (origenT == null || destinoT == null)
        throw new IllegalArgumentException("Los elementos de origen y destino no pueden ser nulos.");

    Vertice origen = getVertice(origenT);
    Vertice destino = getVertice(destinoT);

    if (origen == null || destino == null)
        throw new NoSuchElementException("El origen y/o destino no existen en la gráfica.");

    if (origen == destino) {
        Lista<VerticeGrafica<T>> ruta = new Lista<>();
        ruta.agrega(origen);
        return ruta;
    }

    inicializaDistancias();
    origen.distancia = 0;
    Lista<Vertice> noVisitados = new Lista<>();
    for (Vertice v : vertices) {
        noVisitados.agregaFinal(v);
    }

    while (!noVisitados.esVacia()) {
        Vertice u = extraeMinimo(noVisitados);
        for (Vecino vecino : u.vecinos) {
            Vertice v = vecino.vecino;
            double peso = vecino.peso;
            double distanciaPorU = u.distancia + peso;
            if (distanciaPorU < v.distancia) {
                v.distancia = distanciaPorU;
                v.predecesor = u;
            }
        }
    }

    return reconstruyeTrayectoria(destino);
}

private Vertice extraeMinimo(Lista<Vertice> vertices) {
    Iterator<Vertice> it = vertices.iterator();
    Vertice minimo = it.next();
    while (it.hasNext()) {
        Vertice v = it.next();
        if (v.distancia < minimo.distancia) {
            minimo = v;
        }
    }
    vertices.elimina(minimo);
    return minimo;
}

private Lista<VerticeGrafica<T>> reconstruyeTrayectoria(Vertice destino) {
    if (destino.distancia == Double.POSITIVE_INFINITY) {
        return new Lista<>();  
    }

    Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
    for (Vertice vertice = destino; vertice != null; vertice = vertice.predecesor) {
        trayectoria.agregaInicio(vertice);
    }
    return trayectoria;
}

private void inicializaDistancias() {
    for (Vertice vertice : vertices) {
        vertice.distancia = Double.POSITIVE_INFINITY;
        vertice.predecesor = null;
    }
}
}
