package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.Lista;

/**
 * Clase para analizar y gestionar los argumentos de línea de comandos.
 */
public class GestorArgumentos {

    private Lista<String> archivosEntrada;
    private boolean reversa = false;
    private boolean guarda = false;
    private String archivoSalida = null;

    /**
     * Constructor que analiza los argumentos pasados al programa.
     * 
     * @param args Los argumentos de línea de comandos.
     */
    public GestorArgumentos(String[] args) {
        archivosEntrada = new Lista<>();
        procesarArgumentos(args);
    }

    private void procesarArgumentos(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-r":
                    if (reversa) {
                        throw new IllegalArgumentException("La bandera -r se especificó más de una vez.");
                    }
                    reversa = true;
                    break;
                case "-o":
                    if (guarda) {
                        throw new IllegalArgumentException("La bandera -o se especificó más de una vez.");
                    }
                    guarda = true;
                    if (i + 1 < args.length) {
                        archivoSalida = args[++i];
                    } else {
                        throw new IllegalArgumentException("Se esperaba un nombre de archivo después de -o.");
                    }
                    break;
                default:
                    archivosEntrada.agrega(args[i]);
                    break;
            }
        }

        if (archivosEntrada.esVacia() && !hayEntradaEstandar()) {
            throw new IllegalArgumentException("No se especificaron archivos de entrada ni hay entrada estándar.");
        }
    }

    public Lista<String> getArchivosEntrada() {
        return archivosEntrada;
    }

    public boolean getReversa() {
        return reversa;
    }

    public boolean getGuarda() {
        return guarda;
    }

    public String getArchivoSalida() {
        return archivoSalida;
    }

    /**
     * Verifica si se paso entrada estandar.
     * 
     * @return true si se paso entrada estandar, false en otro caso.
     */
    public static boolean hayEntradaEstandar() {
        return System.console() == null;
    }
}

