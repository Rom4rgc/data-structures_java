package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
    quickSort(arreglo, 0, arreglo.length - 1, comparador);
}


    private static <T> void quickSort(T[] arreglo, int izquierda, int derecha,      Comparator<T> comparador) {
    if (izquierda >= derecha) return;

    T pivote = arreglo[(izquierda + derecha) / 2];

    int i = izquierda;
    int j = derecha;
    while (i <= j) {
        while (comparador.compare(arreglo[i], pivote) < 0) i++;
        while (comparador.compare(arreglo[j], pivote) > 0) j--;
        if (i <= j) {
            intercambia(arreglo, i, j);
            i++;
            j--;
        }
    }

    if (izquierda < j) quickSort(arreglo, izquierda, j, comparador);
    if (derecha > i) quickSort(arreglo, i, derecha, comparador);
}
    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void 
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        if(arreglo.length == 0) return;
        int indice;
        for(int i = 0; i < arreglo.length; i++){
          indice = i;
          for(int j = i; j < arreglo.length; j++){
            if(comparador.compare(arreglo[indice], arreglo[j]) > 0) indice = j;
          }
          intercambia(arreglo, indice, i);
        }
    }

    private static <T> void intercambia(T[] arreglo, int indice1, int indice2){
      T elemento1 = arreglo[indice1];
      T elemento2 = arreglo[indice2];
      arreglo[indice1] = elemento2;
      arreglo[indice2] = elemento1;
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        // Aquí va su código.
    int izquierda = 0;
    int derecha = arreglo.length - 1;

    while (izquierda <= derecha) {
        int medio = izquierda + (derecha - izquierda) / 2;
        int comparacion = comparador.compare(arreglo[medio], elemento);
        
        if (comparacion == 0) {
            return medio; // Elemento encontrado
        } else if (comparacion < 0) {
            izquierda = medio + 1; // Buscar en la mitad derecha
        } else {
            derecha = medio - 1; // Buscar en la mitad izquierda
        }
    }
    
    return -1; // Elemento no encontrado
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
