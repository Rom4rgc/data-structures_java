package mx.unam.ciencias.edd.proyecto1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import mx.unam.ciencias.edd.Lista;

/** Clase para manejar lectura de la entrada estándar, archivos y escritura de archivos */
public class ManejadorArchivos {

    /** Constructor privado para evitar instanciación */
    private ManejadorArchivos() {
    }

    /**
     * Lee la entrada estándar y regresa una lista con las oraciones leídas.
     * @return Lista con las oraciones leídas de la entrada estándar.
     */
    public static Lista<Oracion> leerEntradaEstandar() {
        Lista<Oracion> oraciones = new Lista<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                oraciones.agrega(new Oracion(linea));
            }
        } catch (Exception e) {
            System.err.println("Error al leer la entrada estándar.");
            System.exit(1);
        }
        return oraciones;
    }

    /**
     * Lee el contenido de los archivos y regresa una lista con las oraciones leídas.
     * @param archivos Lista con los nombres de los archivos a leer.
     * @return Lista con las oraciones leídas de los archivos.
     */
    public static Lista<Oracion> leerArchivos(Lista<String> archivos) {
        Lista<Oracion> oraciones = new Lista<>();
        for (String archivo : archivos) {
            try (BufferedReader br = new BufferedReader(new java.io.FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    oraciones.agrega(new Oracion(linea));
                }
            } catch (Exception e) {
                System.err.println("Error al leer el archivo " + archivo + ": " + e.getMessage());
            }
        }
        return oraciones;
    }

    /**
     * Guarda una lista de oraciones en un archivo de texto.
     * @param archivo Nombre del archivo donde se guardarán las oraciones.
     * @param oraciones Lista de oraciones a guardar en el archivo.
     */
    public static void guardarArchivo(String archivo, Lista<Oracion> oraciones) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(archivo))) {
            for (Oracion oracion : oraciones) {
                writer.write(oracion.toString());
                writer.newLine();
            }
            System.out.println("¡Lista guardada correctamente en el archivo " + archivo + "!");
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo " + archivo + ": " + e.getMessage());
        }
    }
}


