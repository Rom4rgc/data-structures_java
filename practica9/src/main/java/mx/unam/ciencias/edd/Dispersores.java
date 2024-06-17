package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        if(llave.length % 4 != 0){
            byte[] nuevoArreglo = new byte[(llave.length + 3) / 4 * 4];
            System.arraycopy(llave, 0, nuevoArreglo, 0, llave.length);
            llave = nuevoArreglo;
        }
        int r = 0;
        for(int i = 0; i < llave.length; i+=4){
            r ^= combina_big_endian(llave[i], llave[i+1], llave[i+2], llave[i+3]);
        }
        return r;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        int a, b, c;
        int[] arr = new int[3];
        a = b =  0x9e3779b9;
        c = 0xffffffff;
        int i = 0;
 
        int residuo = llave.length % 12;
        while(i < llave.length - residuo){
            a += combina_little_endian(llave[i], llave[i+1], llave[i+2], llave[i+3]);
            b += combina_little_endian(llave[i+4], llave[i+5], llave[i+6], llave[i+7]);
            c += combina_little_endian(llave[i+8], llave[i+9], llave[i+10], llave[i+11]);
            arr = mezcla_bob_jenkins(a, b, c);
            a = arr[0]; b = arr[1]; c = arr[2];
            i += 12;
        }
        c += llave.length;
        /* Revisamos los residuos del arreglo para intentar construir los números que podamos */
        switch(residuo){
            case 11: c += ((llave[i+10] & 0xFF)<<24);
            case 10: c += ((llave[i+9] & 0xFF)<<16);
            case 9 : c += ((llave[i+8] & 0xFF)<<8);
            case 8 : b += ((llave[i+7] & 0xFF)<<24);
            case 7 : b += ((llave[i+6] & 0xFF)<<16);
            case 6 : b += ((llave[i+5] & 0xFF)<<8);
            case 5 : b += (llave[i+4] & 0xFF);
            case 4 : a += ((llave[i+3] & 0xFF)<<24);
            case 3 : a += ((llave[i+2] & 0xFF)<<16);
            case 2 : a += ((llave[i+1] & 0xFF)<<8);
            case 1 : a += (llave[i] & 0xFF);
        }
        arr = mezcla_bob_jenkins(a, b, c);
        return arr[2];
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        int h = 5381;
        for(byte b : llave){
            h = (h << 5) + h + (b & 0xFF);
        }
        return h;
    }

    /**
     * Método que combina cuatro bytes en un entero de 32 bits en el esquema big-endian.
     * @param a primer byte.
     * @param b segundo byte.
     * @param c tercer byte.
     * @param d cuarto byte.
     * @return entero de 32 bits.
     */
    private static int combina_big_endian(byte a, byte b, byte c, byte d) {
        return ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | (d & 0xFF);
    }

    /**
     * Método que combina cuatro bytes en un entero de 32 bits en el esquema little-endian.
     * @param a primer byte.
     * @param b segundo byte.
     * @param c tercer byte.
     * @param d cuarto byte.
     * @return entero de 32 bits.
     */
    private static int combina_little_endian(byte a, byte b, byte c, byte d) {
        return ((a & 0xFF)) | ((b & 0xFF) << 8) | ((c & 0xFF) << 16) | ((d & 0xFF) << 24);
    }

    /**
     * Método que se encarga de mezclar tres enteros en el algoritmo de Bob Jenkins.
     * @param a primer entero.
     * @param b segundo entero.
     * @param c tercer entero.
     * @return arreglo con los tres enteros mezclados.
     */
    private static int[] mezcla_bob_jenkins(int a, int b, int c) {
        a -= b;   a -= c;   a ^= (c >>> 13);
        b -= c;   b -= a;   b ^= (a << 8);
        c -= a;   c -= b;   c ^= (b >>> 13);
        a -= b;   a -= c;   a ^= (c >>> 12);
        b -= c;   b -= a;   b ^= (a << 16);
        c -= a;   c -= b;   c ^= (b >>> 5);
        a -= b;   a -= c;   a ^= (c >>> 3);
        b -= c;   b -= a;   b ^= (a << 10);
        c -= a;   c -= b;   c ^= (b >>> 15);

        return new int[]{a, b, c};
    }
}

