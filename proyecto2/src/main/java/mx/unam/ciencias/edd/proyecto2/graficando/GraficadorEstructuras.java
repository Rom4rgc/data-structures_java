package mx.unam.ciencias.edd.proyecto2.graficando;
import mx.unam.ciencias.edd.*;
/**
* Graficaer nodit
*/
public class GraficadorEstructuras<T extends Comparable<T>>{
  private Lista<T> elementos;
  private String estructuraSVG = "";
  public void estableceDimensiones(int ancho,int alto){
      this.estructuraSVG+= "<svg width='"+ancho+"'  height= '" + alto+ "'>\n";
  }

  public GraficadorEstructuras(Lista<T> l){
    this.elementos = l;
  }

  public String dibujaEstructuraDD(EstructuraDD estructura){
    /* En caso de no tener elementos, no se dibuja nada */
    if(elementos.getLongitud() == 0)
      return "";
    String res = "";
    switch(estructura){
      case LISTA:
        res+=dibujaListaCola(true);
        break;
      case PILA:
        res+=dibujaPila();
        break;
      case COLA:
        res+=dibujaListaCola(false);
        break;
      case NINGUNO:
        res+="";
        break;
    }
    return estructuraSVG+res;
  }
  /**
  * Método que lo dibuja
  */
  public String dibujaListaCola(boolean lista){
    String res = "";
    estableceDimensiones(elementos.getLongitud()*120, 150);
    int coordx = 10;
    int coordY = 30;
    while(elementos.getLongitud() > 1){
      T elemento = elementos.eliminaPrimero();
      res+= DibujaNodos.dibujaNodo(coordx, coordY, elemento.toString(), lista);
      coordx+=120;
    }
    res+=DibujaNodos.dibujaCuadrado(coordx, coordY, elementos.eliminaPrimero().toString());
    return res+"</svg>";
  }
  /**
  * Método pila
  */
  public String dibujaPila(){
    String res = "";
    estableceDimensiones(100,elementos.getLongitud()*70);
    Lista<T> reversa = elementos.reversa();
    int coordY = 40;
    while(reversa.getLongitud() > 0){
      T elemento = reversa.eliminaPrimero();
      res+=DibujaNodos.dibujaCuadrado(10, coordY, elemento.toString());
      coordY+=60;
    }
    return res+"</svg>";
  }

}
