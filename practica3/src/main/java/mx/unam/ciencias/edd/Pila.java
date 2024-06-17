package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        // Aquí va su código.
        String s = "";
        Nodo nodo = cabeza;

        while (nodo != null) {
            s += nodo.elemento + "\n";
            nodo = nodo.siguiente;
        }

        return s;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        // Aquí va su código.
        if (elemento == null)
            throw new IllegalArgumentException();

        Nodo nodo = new Nodo(elemento);

        if (cabeza == null) {
            cabeza = nodo;
            rabo = nodo;
        } else {
            nodo.siguiente = cabeza;
            cabeza = nodo;
        }
    }
}
