package mx.unam.ciencias.edd.proyecto2.graficando;
import mx.unam.ciencias.edd.*;

/**
* Clase para dibujar monticulos
*/

public class DibujaMonticulo{
  /* Lista con los elementos iniciales */
  private Lista<Integer> elementos;

  public DibujaMonticulo(Lista<Integer> lista){
    this.elementos = lista;
  }

  public String dibujaMonticulo(){

    Lista<ValorIndexable<Integer>> l1 = new Lista<>();
    for(Integer elemento : elementos){
      double valor = elemento;
      l1.agregaFinal(new ValorIndexable<Integer>(elemento, valor));
    }
    MonticuloMinimo<ValorIndexable<Integer>> minHeap = new MonticuloMinimo<>(l1);
    Lista<Integer> lista2 = new Lista<>();
    for(int i = 0; i < minHeap.getElementos(); i++)
      lista2.agregaFinal(minHeap.get(i).getElemento());
    ArbolGraf<Integer> dibAr = new ArbolGraf<>(lista2);
    System.out.println(minHeap.toString()); 
    return dibAr.dibujaArbol(EstructuraDD.ARBOLCOMPLETO);
  }
}
