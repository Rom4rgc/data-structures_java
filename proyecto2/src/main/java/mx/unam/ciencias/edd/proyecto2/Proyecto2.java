package mx.unam.ciencias.edd.proyecto2;
import java.io.IOException;

/**
 * Clase principal del Proyecto 2 que gestiona la entrada, procesa los datos y genera la salida correspondiente.
 */
public class Proyecto2 {

    public static void main(String[] args) {
        try {
            String contenido = obtenerContenidoEntrada(args);
            TestEntrada analizador = new TestEntrada(contenido);
            String estructuraSVG = analizador.imprimeEstructura();

            System.out.println(estructuraSVG); 
            LectorEntrada.escribirArchivo("prueba.svg", estructuraSVG); 
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1); 
        }
    }


    private static String obtenerContenidoEntrada(String[] args) throws IOException {
        if (args.length > 0) {
            return LectorEntrada.leerArchivo(args[0]);
        } else {
            return LectorEntrada.leerConsola();
        }
    }
}

