package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        // Aquí va su código.
      if(cabeza == null) return "";
      String m = "";
      Nodo nodo = cabeza;
      while(nodo != null){
        m += nodo.elemento.toString() + ",";
        nodo = nodo.siguiente;
      }
      return m;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        // Aquí va su código.
  if (elemento == null) {
  throw new IllegalArgumentException("No se puede agregar un elemento nulo a la cola.");
    }
    Nodo nuevoNodo = new Nodo(elemento);
    if (esVacia()) {
        cabeza = rabo = nuevoNodo;
    } else {
        rabo.siguiente = nuevoNodo;
        rabo = nuevoNodo;
    }
    }
}
