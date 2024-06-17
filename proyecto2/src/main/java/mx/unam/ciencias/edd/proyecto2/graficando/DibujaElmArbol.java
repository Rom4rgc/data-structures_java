package mx.unam.ciencias.edd.proyecto2.graficando;
import mx.unam.ciencias.edd.*;
/**
TERMINAR
*/
public class DibujaElmArbol{


  public  static  String dibujaArista(int x1, int y1, int x2, int y2){
    return "<line x1='"+x1+"' y1='"+y1+"' x2= '"+x2+"' y2='" + y2 +"' style='stroke:black; stroke-width:1'></line>\n";
  }

  public static String dibujaNodo(int x, int y, VerticeArbolBinario vertice, Color c){
    String color = "";
    switch(c){
      case ROJO:
        color = "Red";
        break;
      case NEGRO:
        color = "Black";
        break;
      case NINGUNO:
        color = "White";
        break;
    }
    int y1 = y+2;
    String colorletra ="";
    if(c.equals(Color.NEGRO) || c.equals(Color.ROJO))
      colorletra = "white";
    else
      colorletra = "Black";
    return "<circle cx= '"+x+"' cy= '"+y+"' r='12' stroke='black' fill='"+color+"'  /> \n<text x= '"+x+"' y= '"+y1+
    "' text-anchor='middle' fill='"+ colorletra+"' font-size='10px' font-family='Arial' dy='.1em'>"+
    vertice.get()+"</text>\n";
  }

  public static int calculaMitad(int x1, int x2){
    return (int)Math.floor((x1 + x2)/2);
  }

  public static  boolean esHoja(VerticeArbolBinario vertice){
    return !vertice.hayIzquierdo() && !vertice.hayDerecho();
  }
  /**
  * Método árboles binarios AVL
  */
  public static String dibujaEtiqueta(int x, int y, String etiqueta){
    int y1 = y-3;
    int x1 = x-2;
    return "<text x= '"+x1+"' y= '"+y1+"' text-anchor='middle' fill='black' font-size='10px' font-family='Arial' dy='.3em'>"+
    etiqueta+"</text>\n";
  }
  /**
  * Método calcular la cantidad de nodos en un subarbol Td, Ti
  */
  public static int cuentaVerticesSubarbol(VerticeArbolBinario v){
    if(!v.hayIzquierdo() && v.hayDerecho())
      return 1 + cuentaVerticesSubarbol(v.derecho());
    if(!v.hayDerecho() && v.hayIzquierdo())
      return 1 + cuentaVerticesSubarbol(v.izquierdo());
    if(esHoja(v))
      return 1;
    else
      return 1 + cuentaVerticesSubarbol(v.izquierdo()) + cuentaVerticesSubarbol(v.derecho());
  }
}
