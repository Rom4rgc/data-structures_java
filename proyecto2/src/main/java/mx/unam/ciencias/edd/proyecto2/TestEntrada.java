package mx.unam.ciencias.edd.proyecto2;
import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto2.graficando.*;

public class TestEntrada {

    private String contenido;
    private EstructuraDD estructura = EstructuraDD.NINGUNO;
    private Lista<Integer> datos = new Lista<>();

    public TestEntrada(String contenido) {
        this.contenido = contenido;
    }

    public void obtieneDatosEntrada() {
        StringBuilder estructura = new StringBuilder();
        StringBuilder numero = new StringBuilder();
        
        for (int i = 0; i < contenido.length(); i++) {
            char ch = contenido.charAt(i);
            if (ch == ':') break;
            
            if (Character.isLetter(ch)) {
                estructura.append(ch);
            } else if (Character.isDigit(ch)) {
                numero.append(ch);
            } else if (ch == ' ' && numero.length() > 0) {
                datos.agregaFinal(Integer.parseInt(numero.toString()));
                numero.setLength(0); 
            }
        }
        determinaEstructura(estructura.toString().toLowerCase());
    }

    public void determinaEstructura(String e) {
        switch (e) {
            case "arbolrojinegro": this.estructura = EstructuraDD.ARBOLROJINEGRO; break;
            case "arbolavl": this.estructura = EstructuraDD.ARBOLAVL; break;
            case "arbolcompleto": this.estructura = EstructuraDD.ARBOLCOMPLETO; break;
            case "arbolordenado": this.estructura = EstructuraDD.ARBOLORDENADO; break;
            case "cola": this.estructura = EstructuraDD.COLA; break;
            case "lista": this.estructura = EstructuraDD.LISTA; break;
            case "pila": this.estructura = EstructuraDD.PILA; break;
            case "grafica": this.estructura = EstructuraDD.GRAFICA; break;
            case "monticulominimo": case "monticulo": this.estructura = EstructuraDD.MONTICULOMINIMO; break;
            default: this.estructura = EstructuraDD.NINGUNO;
        }
    }

    public static boolean esNumero(String str) {
        return str != null && !str.isEmpty() && str.chars().allMatch(Character::isDigit);
    }

    public String imprimeEstructura() {
        obtieneDatosEntrada();
        if (estructura == EstructuraDD.NINGUNO) {
            return "Se ha producido un error en tu entrada";
        }

        switch (estructura) {
            case LISTA:
            case PILA:
            case COLA:
                GraficadorEstructuras<Integer> l = new GraficadorEstructuras<>(datos);
                return l.dibujaEstructuraDD(estructura);
            case GRAFICA:
                return obtieneGrafica();
            case MONTICULOMINIMO:
                DibujaMonticulo dibMonticulo = new DibujaMonticulo(datos);
                return dibMonticulo.dibujaMonticulo();
            default:
                ArbolGraf<Integer> dibAr = new ArbolGraf<>(datos);
                return dibAr.dibujaArbol(estructura);
        }
    }

    public String obtieneGrafica() {
        Lista<Integer> vertices = new Lista<>();
        String[] datos = contenido.split(" ");
        for (int i = 1; i < datos.length; i++) {
            try {
                int data = Integer.parseInt(datos[i]);
                if (!vertices.contiene(data)) {
                    vertices.agregaFinal(data);
                }
            } catch (NumberFormatException ignored) { }
        }

        DibujaGrafica<Integer> dg = new DibujaGrafica<>(vertices);
        for (int i = 1; i < datos.length - 1; i += 2) {
            try {
                dg.graf.conecta(Integer.parseInt(datos[i]), Integer.parseInt(datos[i + 1]));
            } catch (NumberFormatException e) {
                System.out.println("Error al conectar: " + e.getMessage());
            }
        }
        return dg.dibujaGrafica();
    }
}

