package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
            this.llave = llave;
            this.valor = valor;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
            for (int i = 0; i < entradas.length; i++) {
                if (entradas[i] != null) {
                    indice = i;
                    iterador = entradas[i].iterator();
                    return;
                }
            }
            iterador = null;
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            return iterador != null;
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() {
            if (iterador == null)
                throw new NoSuchElementException("Iterador Null");
            if (iterador.hasNext()) {
                Entrada entrada = iterador.next();
                if (!iterador.hasNext())
                    mueveIterador();
                return entrada;
            }
            throw new NoSuchElementException("No hay más elementos, carajo!");
        }

        /* Mueve el iterador a la siguiente entrada válida. */
        private void mueveIterador() {
            for (int i = indice + 1; i < entradas.length; i++) {
                if (entradas[i] != null) {
                    // Actualizamos el índice
                    indice = i;
                    // Cambiamos el iterador al iterador de esa lista
                    iterador = entradas[i].iterator();
                    return;
                }
            }
            iterador = null;
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador implements Iterator<K> {
        /* Regresa el siguiente elemento. */
        @Override
        public K next() {
            if (!super.hasNext())
                throw new NoSuchElementException();
            return super.siguiente().llave;
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador implements Iterator<V> {
        /* Regresa el siguiente elemento. */
        @Override
        public V next() {
            if (!super.hasNext())
                throw new NoSuchElementException();
            return super.siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[]) Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * 
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * 
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * 
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        this.dispersor = dispersor;
        int mascara = getPotencia(capacidad);
        entradas = nuevoArreglo(mascara);
    }

    /**
     * Método auxiliar para calcular la potencia de 3 que más se acerque
     * 
     * @param int número
     */
    private int getPotencia(int n) {
        n = (n < 64) ? 64 : n;
        int c = 1;
        while (c < n * 2)
            c *= 2;
        return c;
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * 
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        if (llave == null || valor == null)
            throw new IllegalArgumentException("Llave/valor incorrectos");
        int i = dispersor.dispersa(llave) & getMascara();
        if (entradas[i] == null) {
            entradas[i] = new Lista<Entrada>();
            entradas[i].agrega(new Entrada(llave, valor));
            elementos++;
        } else if (entradas[i] != null) {
            boolean existeLlave = false;
            // Vamos a ver si no hay una entrada con la misma llave
            for (Entrada entrada : entradas[i]) {
                if (entrada.llave.equals(llave)) {
                    entrada.valor = valor;
                    existeLlave = true;
                    break;
                }
            }
            if (existeLlave == false) {
                entradas[i].agrega(new Entrada(llave, valor));
                elementos++;
            }
        }
        if (factor_carga() >= MAXIMA_CARGA) {
            doblar_capacidad_arreglo();
        }
    }

    /**
     * Método para obtener la máscara del hash
     * 
     * @return Máscara -> longitud (que es potencia de 2) - 1
     */
    public int getMascara() {
        return entradas.length - 1;
    }

    /**
     * Método para obtener el factor de carga
     * 
     * @return double Factor de carga
     */
    private double factor_carga() {
        double carga = (elementos + 0.0) / entradas.length;
        return carga;
    }

    /**
     * Método para aumentar el tamaño del arreglo una vez que se haya excedido la
     * capacidad de carga
     */
    private void doblar_capacidad_arreglo() {
        int indice = 0;
        Lista<Entrada>[] nuevasEntradas = nuevoArreglo(entradas.length * 2);
        for (int i = 0; i < entradas.length; i++) {
            if (entradas[i] != null) {
                // Agregamos las entradas en el nuevo arreglo, otra vez redefiniendo su indice pues
                // el tamaño aumentó
                for (Entrada entrada : entradas[i]) {
                    indice = dispersor.dispersa(entrada.llave) & (nuevasEntradas.length - 1);
                    if (nuevasEntradas[indice] == null) {
                        nuevasEntradas[indice] = new Lista<Entrada>();
                        nuevasEntradas[indice].agrega(entrada);
                    } else
                        nuevasEntradas[indice].agrega(entrada);
                }
            }
        }
        entradas = nuevasEntradas;
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * 
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException   si la llave no está en el diccionario.
     */
    public V get(K llave) {
        if (llave == null)
            throw new IllegalArgumentException("La llave es inválida");
        int i = dispersor.dispersa(llave) & getMascara();
        if (entradas[i] == null)
            throw new NoSuchElementException("No existe el elemento");
        for (Entrada entrada : entradas[i]) {
            if (entrada.llave.equals(llave))
                return entrada.valor;
        }
        throw new NoSuchElementException("No se encontró el elemento");
    }

    // Aquí va tu código
    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * 
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <code>true</code> si la llave está en el diccionario,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(K llave) {
        if (llave == null)
            return false;
        int i = dispersor.dispersa(llave) & getMascara();
        if (entradas[i] == null)
            return false;
        for (Entrada entrada : entradas[i]) {
            if (entrada.llave.equals(llave))
                return true;
        }
        return false;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * 
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException   si la llave no se encuentra en
     *                                  el diccionario.
     */
    public void elimina(K llave) {
        if (llave == null) 
            throw new IllegalArgumentException("Llave inválida");
        
        int i = dispersor.dispersa(llave) & getMascara();
        if (entradas[i] == null) 
            throw new NoSuchElementException("No se encontró la llave");
        
        for(Entrada entrada : entradas[i]){
          if(entrada.llave.equals(llave)) entradas[i].elimina(entrada);
          if(entradas[i].esVacia())
            entradas[i] = null;
          elementos--;
          break;
        }
    }



    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * 
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        int colisiones = 0;
        for (Lista<Entrada> lista : entradas) {
            if (lista != null && lista.getLongitud() > 1) {
                colisiones += lista.getLongitud() - 1;
            }
        }
        return colisiones;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * 
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        int maxColision = 0;
        for (Lista<Entrada> lista : entradas) {
            if (lista != null) {
                maxColision = Math.max(maxColision, lista.getLongitud() - 1);
            }
        }
        return maxColision;
    }

    /**
     * Nos dice la carga del diccionario.
     * 
     * @return la carga del diccionario.
     */
    public double carga() {
        return (double) elementos / entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * 
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * 
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        return elementos == 0;
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        entradas = nuevoArreglo(entradas.length);
        elementos = 0;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * 
     * @return una representación en cadena del diccionario.
     */
    @Override
    public String toString() {
        if (elementos == 0)
            return "{}";
        StringBuilder res = new StringBuilder("{ ");
        for (int i = 0; i < entradas.length; i++) {
            if (entradas[i] != null) {
                for (Entrada entrada : entradas[i])
                    res.append(String.format("'%s': '%s', ", entrada.llave, entrada.valor));
            }
        }
        return res.append("}").toString();
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * 
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked")
        Diccionario<K, V> d = (Diccionario<K, V>) o;
        if (d.getElementos() != this.elementos)
            return false;
        Iterator<K> iteradorLlaves = iteradorLlaves();
        while (iteradorLlaves.hasNext()) {
            K llave = iteradorLlaves.next();
            if (!d.contiene(llave) || !d.get(llave).equals(get(llave))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * 
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * 
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override
    public Iterator<V> iterator() {
        return new IteradorValores();
    }

    // Aquí va tu código
    /**
     * Método para aumentar la capacidad del arreglo al limpiar el diccionario.
     */
    public void limpiaConCapacidad(int capacidad) {
        if (capacidad < MINIMA_CAPACIDAD) {
            capacidad = MINIMA_CAPACIDAD;
        }
        entradas = nuevoArreglo(capacidad);
        elementos = 0;
    }

    /**
     * Clase interna para la lista.
     */
    private class Lista<T> implements Iterable<T> {
        private class Nodo {
            T elemento;
            Nodo siguiente;

            Nodo(T elemento) {
                this.elemento = elemento;
            }
        }

        private Nodo cabeza;
        private Nodo rabo;
        private int longitud;

        public void agrega(T elemento) {
            Nodo nuevo = new Nodo(elemento);
            if (cabeza == null) {
                cabeza = rabo = nuevo;
            } else {
                rabo.siguiente = nuevo;
                rabo = nuevo;
            }
            longitud++;
        }

        public boolean esVacia() {
            return cabeza == null;
        }

        public int getLongitud() {
            return longitud;
        }

        public void elimina(T elemento) {
            if (cabeza == null)
                return;
            if (cabeza.elemento.equals(elemento)) {
                cabeza = cabeza.siguiente;
                if (cabeza == null)
                    rabo = null;
                longitud--;
                return;
            }
            Nodo anterior = cabeza;
            Nodo actual = cabeza.siguiente;
            while (actual != null && !actual.elemento.equals(elemento)) {
                anterior = actual;
                actual = actual.siguiente;
            }
            if (actual != null) {
                anterior.siguiente = actual.siguiente;
                if (actual == rabo)
                    rabo = anterior;
                longitud--;
            }
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private Nodo actual = cabeza;

                @Override
                public boolean hasNext() {
                    return actual != null;
                }

                @Override
                public T next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    T elemento = actual.elemento;
                    actual = actual.siguiente;
                    return elemento;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }
}

