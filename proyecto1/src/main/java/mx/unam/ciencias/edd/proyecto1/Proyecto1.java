package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.Lista;

/**
 * Proyecto 1: Ordenador Lexicográfico.
 */
public class Proyecto1 {

    public static void main(String[] args) {
        GestorArgumentos gestorArgumentos = new GestorArgumentos(args);
        Lista<String> archivos = gestorArgumentos.getArchivosEntrada();
        String archivoSalida = gestorArgumentos.getArchivoSalida();
        boolean reversa = gestorArgumentos.getReversa();

        Lista<Oracion> contenido = new Lista<>();

        if (GestorArgumentos.hayEntradaEstandar())
            contenido = ManejadorArchivos.leerEntradaEstandar();

        Lista<Oracion> contenidoArchivos = ManejadorArchivos.leerArchivos(archivos);
        for (Oracion oracion : contenidoArchivos)
            contenido.agrega(oracion);

        contenido = Ordenador.ordenar(contenido, reversa);

        if (archivoSalida != null)
            ManejadorArchivos.guardarArchivo(archivoSalida, contenido);

        for (Oracion oracion : contenido)
            System.out.println(oracion);
    }
}

