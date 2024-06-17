package mx.unam.ciencias.edd.proyecto2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Clase para leer la entrada del programa ya sea por consola o por archivo de texto.
 */
public class LectorEntrada {

    public static String leerArchivo(String nombre) throws IOException {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(nombre), StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().startsWith("#")) {
                    contenido.append(linea.split("#")[0].trim()).append(" ");
                }
            }
        }
        return contenido.toString().trim();
    }


    public static String leerConsola() {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().startsWith("#")) {
                    contenido.append(linea.split("#")[0].trim()).append(" ");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from standard input: " + e.getMessage());
            System.exit(1);
        }
        return contenido.toString().trim();
    }

    /**
     * Escribe una cadena de texto en un archivo
     */
    public static void escribirArchivo(String documento, String texto) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(documento), StandardCharsets.UTF_8)) {
            writer.write(texto);
            writer.newLine();
        }
    }

}
