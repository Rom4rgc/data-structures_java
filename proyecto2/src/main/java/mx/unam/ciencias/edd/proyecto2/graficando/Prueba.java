package mx.unam.ciencias.edd.proyecto2.graficando;
import mx.unam.ciencias.edd.*;

public class Prueba{
  public static void main(String[] agrs){

    Lista<Integer> lista = new Lista<>();
    for(int i = 0; i < 100; i++)
      lista.agregaFinal((int)(Math.random()*50+1));


    ArbolGraf<Integer> dibAr = new ArbolGraf<>(lista.copia());
    ArbolGraf<Integer> dibAr1 = new ArbolGraf<>(lista.copia());
    ArbolGraf<Integer> dibAr2 = new ArbolGraf<>(lista.copia());
    ArbolGraf<Integer> dibAr3 = new ArbolGraf<>(lista.copia());
    GraficadorEstructuras<Integer> dibLCP = new 
GraficadorEstructuras<>(lista.copia());
    GraficadorEstructuras<Integer> dibLCP1 = new 
GraficadorEstructuras<>(lista.copia());
    GraficadorEstructuras<Integer> dibLCP2 = new 
GraficadorEstructuras<>(lista.copia());
    // Imprimimir todos

    System.out.println("Arbol Rojinegro: ");
    System.out.println(dibAr.dibujaArbol(EstructuraDD.ARBOLROJINEGRO));
    System.out.println("Arbol AVL: ");
    System.out.println(dibAr1.dibujaArbol(EstructuraDD.ARBOLAVL));
    System.out.println("Arbol ORDENADO: ");
    System.out.println(dibAr2.dibujaArbol(EstructuraDD.ARBOLORDENADO));
    System.out.println("Arbol COMPLETO: ");
    System.out.println(dibAr3.dibujaArbol(EstructuraDD.ARBOLCOMPLETO));
    System.out.println("LISTA: ");
    System.out.println(dibLCP.dibujaEstructuraDD(EstructuraDD.LISTA));
    System.out.println("COLA: ");
    System.out.println(dibLCP1.dibujaEstructuraDD(EstructuraDD.COLA));
    System.out.println("PILA: ");
    System.out.println(dibLCP2.dibujaEstructuraDD(EstructuraDD.PILA));
  }
}
