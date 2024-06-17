package mx.unam.ciencias.edd.proyecto2.graficando;
import mx.unam.ciencias.edd.*;

public class ArbolGraf<T extends Comparable<T>>{

  private String arbolSVG = "";

  private Lista<T> elementos;

  private boolean esArbolAVL = false;

  /**
  * Método que establece las dimensiones del svg en donde se presentará el árbol
  */
  public void estableceDimensiones(EstructuraDD estructura){
    if(this.elementos.getLongitud() > 0 && estructura != EstructuraDD.ARBOLORDENADO){
      int altura = (int)Math.floor(Math.log(elementos.getLongitud())/Math.log(2));
      this.arbolSVG+= "<svg width='"+(elementos.getLongitud()*50)+"'  height= '" + altura*80+ "' >\n";
    }else{
      this.arbolSVG+= "<svg width='"+(elementos.getLongitud()*50)+"'  height= '" + elementos.getLongitud()*50+ "' >\n";
    }
  }
  /**
  * Constructor de la clase para graficarr arbol
  */
  public ArbolGraf(Lista<T> l){
    this.elementos = l;
  }

  public String dibujaArbol(EstructuraDD estructura){
    estableceDimensiones(estructura);
    if(elementos.getLongitud() == 0)
      return "";
    switch(estructura){
      case ARBOLORDENADO:
        arbolSVG+=dibujaArbolOrdenadoCompleto(estructura);
        break;
      case ARBOLCOMPLETO:
        arbolSVG+=dibujaArbolOrdenadoCompleto(estructura);
        break;
      case ARBOLROJINEGRO:
        arbolSVG+=dibujaARbolAVLRojinegro(estructura);
        break;
      case ARBOLAVL:
        esArbolAVL = true;
        arbolSVG+=dibujaARbolAVLRojinegro(estructura);
        break;
      case NINGUNO:
        arbolSVG="";
        break;
    }
    return arbolSVG;
  }

  /**
  * Método que dibuja un arbol binario ordenado / completo
  */
  public  String dibujaArbolOrdenadoCompleto(EstructuraDD estructura){
      ArbolBinario<T> arbol;
      if(estructura.equals(EstructuraDD.ARBOLORDENADO))
        arbol = new ArbolBinarioOrdenado<>(this.elementos);
      else
        arbol = new ArbolBinarioCompleto<>(this.elementos);
      VerticeArbolBinario raiz = arbol.raiz();

      /* Calcula la cantidad de vértices que hay en el subarbol izquierdo */
      int verticesEnIzquierdo, verticesEnDerecho;
      verticesEnDerecho = verticesEnIzquierdo = 0;
      if(raiz.hayIzquierdo())
        verticesEnIzquierdo = DibujaElmArbol.cuentaVerticesSubarbol(raiz.izquierdo());
      /* Calcula la cantidad de vértices que hay en el subarbol derecho */
      if(raiz.hayDerecho())
        verticesEnDerecho = DibujaElmArbol.cuentaVerticesSubarbol(raiz.derecho());

      /* Calcula la coordenada en x de la raíz */
      int xNueva = verticesEnIzquierdo*40+40;
      int yNueva = 30;
      int limiteInferior = 0;
      int limiteSuperior = xNueva + (verticesEnDerecho)*40;

      String izq , der;
      izq = der = "";
      if(raiz.hayIzquierdo())
        izq = dibujaArbol(xNueva, yNueva, raiz.izquierdo(), limiteInferior, xNueva);
      if(raiz.hayDerecho())
        der = dibujaArbol(xNueva, yNueva, raiz.derecho(), xNueva, limiteSuperior);

      return DibujaElmArbol.dibujaNodo(xNueva, yNueva, raiz, Color.NINGUNO)+ der + izq+"</svg>";
  }

  /**
  * Método que dibuja un arbol binario avl/rojinegro
  */
  public String dibujaARbolAVLRojinegro(EstructuraDD estructura){
    ArbolBinario<T> arbol;
    if(estructura.equals(EstructuraDD.ARBOLAVL))
      arbol = new ArbolAVL<>(this.elementos);
    else
      arbol = new ArbolRojinegro<>(this.elementos);
    VerticeArbolBinario raiz = arbol.raiz();
    int verticesEnIzquierdo, verticesEnDerecho;
    verticesEnDerecho = verticesEnIzquierdo = 0;
    if(raiz.hayIzquierdo())
      verticesEnIzquierdo = DibujaElmArbol.cuentaVerticesSubarbol(raiz.izquierdo());
    if(raiz.hayDerecho())
      verticesEnDerecho = DibujaElmArbol.cuentaVerticesSubarbol(raiz.derecho());

    int xNueva = verticesEnIzquierdo*40+40;
    int yNueva = 30;
    int limiteInferior = 0;
    int limiteSuperior = xNueva + (verticesEnDerecho)*40;
    String etiqueta, balance_altura;
    etiqueta = balance_altura = "";
    Color color = Color.NINGUNO;
    if(estructura.equals(EstructuraDD.ARBOLAVL)){
      balance_altura = raiz.toString().substring(raiz.toString().length()-4, raiz.toString().length());
      etiqueta = DibujaElmArbol.dibujaEtiqueta(xNueva+5, yNueva-12, balance_altura);
    }else
      color = Color.NEGRO;
    String izq , der;
    izq = der = "";
    if(raiz.hayIzquierdo())
      izq = dibujaArbol(xNueva, yNueva, raiz.izquierdo(), limiteInferior, xNueva);
    if(raiz.hayDerecho())
      der = dibujaArbol(xNueva, yNueva, raiz.derecho(), xNueva, limiteSuperior);

    return etiqueta + DibujaElmArbol.dibujaNodo(xNueva, yNueva, raiz, color)+ der + izq+"</svg>";
  }
  /**
  * Método para dibujar un árbol binario
  */
  public String dibujaArbol(int coorXAnterior, int coordYAnterior, VerticeArbolBinario v, int inferior, int superior){
    if(v == null)
      return "";
    int xNueva = DibujaElmArbol. calculaMitad(inferior, superior);
    int coordYNueva = coordYAnterior + 40;
    String arista;
    int coordXAux = (v.padre().hayIzquierdo() && v.padre().izquierdo() == v) ? coorXAnterior -12  : coorXAnterior + 12;
    arista = DibujaElmArbol.dibujaArista(coordXAux, coordYAnterior, xNueva, coordYNueva);

    /* coloresss */
    Color color;
    if(v.toString().substring(0,1).equals("R"))
      color = Color.ROJO;
    else if(v.toString().substring(0,1).equals("N"))
      color = Color.NEGRO;
    else
      color = Color.NINGUNO;
    String etiqueta, balance_altura;
    etiqueta = balance_altura = "";
    if(esArbolAVL){
      balance_altura = v.toString().substring(v.toString().length()-4, v.toString().length());
      coordXAux = (v.padre().hayIzquierdo() && v.padre().izquierdo() == v) ? xNueva-12 : xNueva+12;
      etiqueta = DibujaElmArbol.dibujaEtiqueta(coordXAux, coordYNueva-12, balance_altura);
    }
    if(DibujaElmArbol.esHoja(v))
      return etiqueta + arista + DibujaElmArbol.dibujaNodo(xNueva,coordYNueva,v,color);
    String izq, der;
    izq = der = "";
    if(v.hayIzquierdo())
        izq = dibujaArbol(xNueva, coordYNueva, v.izquierdo(), inferior, xNueva);
    if(v.hayDerecho())
        der = dibujaArbol(xNueva, coordYNueva, v.derecho(), xNueva, superior);
    return  etiqueta + arista + DibujaElmArbol.dibujaNodo(xNueva,coordYNueva,v,color)+ izq + der;
  }
}
