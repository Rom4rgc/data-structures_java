package mx.unam.ciencias.edd.proyecto1;

import java.text.Collator;
import java.text.Normalizer;
import java.util.Comparator;
import mx.unam.ciencias.edd.Lista;

/**
 * Clase para ordenador lexicográfico.
 */
public class Ordenador {

    /**
     * Ordena una lista de oraciones lexicográficamente. Si reversa es verdadero, se invierte el orden de las oraciones.
     * @param oraciones lista de oraciones a ordenar
     * @param reversa si es verdadero, se invierte el orden de las oraciones
     * @return lista de oraciones ordenadas
     */
    public static Lista<Oracion> ordenar(Lista<Oracion> oraciones, boolean reversa) {
        Lista<Oracion> ordenadas = oraciones.mergeSort(Comparator.comparing(Ordenador::formatoOracion));
        if (reversa)
            ordenadas = ordenadas.reversa();
        return ordenadas;
    }

    /**
     * Formatea una oración eliminando caracteres no alfabéticos, convirtiéndola a minúsculas y eliminando acentos.
     * @param oracion la oración a formatear
     * @return la oración formateada
     */
    private static String formatoOracion(Oracion oracion) {
        String formateado = Normalizer.normalize(oracion.toString(), Normalizer.Form.NFD);
        return formateado.toLowerCase().replaceAll("[^a-z]", "");
    }
}
