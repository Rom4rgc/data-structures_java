package mx.unam.ciencias.edd.proyecto2.graficando;
import mx.unam.ciencias.edd.*;

public class DibujaGrafica<T extends Comparable<T>>{

  private class Punto{
    public T elemento;
    /* Coordenada en x*/
    public double x;
    /* Coordenada en y*/
    public double y;

    public Punto(double x,double y, T elemento ){
      this.x = x;
      this.y = y;
      this.elemento = elemento;
    }
  }
  /* Lista elementos */
  private Lista<T> elementos;
  public Grafica<T> graf = new Grafica<>();
  private String estructuraSVG = "";

  public DibujaGrafica(Lista<T> lista){
    for(T elemento : lista)
      try{
        graf.agrega(elemento);
      }catch(Exception e){
        System.out.println("No se pueden ingresar elementos repetidos");
      }
  }

  public void estableceDimensiones(int ancho,int alto){
      this.estructuraSVG+= "<svg width='"+ancho+"'  height= '" + alto+ "' >\n";
  }

  public Lista<Punto> generaPuntos(){
    Lista<Punto> listaPuntos = new Lista<>();
    for(T elemento : graf){
      listaPuntos.agregaFinal(new Punto(0, 0, elemento)); 
    }
    return listaPuntos;
  }
  /**
  * Método de la cadena de los puntos
  */
  public String dibujaPuntos(Lista<Punto> listaPuntos){
    String vertices = "";
    for(Punto p : listaPuntos)
      vertices+=dibujaVertice(p);
    return vertices;
  }
  /**
  * No dibujar cada Punto
  */
  public String dibujaVertice(Punto p){
      return dibujaNodo(p.x, p.y, p.elemento);
  }

  public String dibujaNodo(double x, double y, T elemento){
    double y1 = y+2;
    String colorletra = "white";
    String[] colores = {"black", "red", "blue"};
    String color = colores[((int)(Math.random()*2))];
    return "<circle cx= '"+x+"' cy= '"+y+"' r='10' stroke='blue' fill='"+color+"'  />\n <text x= '"+x+"' y= '"+y1+
    "' text-anchor='middle' fill='"+ colorletra+"' font-size='8px' font-family='Arial' dy='.1em'>"+
    elemento.toString()+"</text>\n";
  }

  public String dibujaAristas(Lista<Punto> listaPuntos){
    String aristas = "";
    for(Punto p : listaPuntos){
      for(Punto q : listaPuntos){
        if(graf.sonVecinos(p.elemento, q.elemento))
          aristas+=conectaVertices(p, q);
      }
    }
    return aristas;
  }

  public String conectaVertices(Punto p1, Punto p2){
    return dibujaArista(p1.x,p1.y,p2.x,p2.y);
  }

  public String dibujaArista(double x1, double y1, double x2, double y2){
    double y1N, y2N;
    y1N = (y1 < 200) ? y1+8 : y1 -8;
    y2N = (y2 < 200) ? y2+8 : y2 -8;
    return "<line x1='"+x1+"' y1='"+y1N+"' x2='"+x2+"' y2='" + y2N+"' style='stroke:black; stroke-width:1'></line>\n";
  }

  public void asignaCoordenadas(Lista<Punto> listaPuntos){
    if(listaPuntos.getLongitud() == 1){
      listaPuntos.get(0).x = 250;
      listaPuntos.get(0).y = 200;
      return;
    }
    int mitad = listaPuntos.getLongitud()/2;
    Lista<Punto> lista2 = new Lista<>();
    for(int i = 0; i < mitad; i++)
      lista2.agregaFinal(listaPuntos.eliminaPrimero());
    double xSuperior, xInferior, ySuperior, yInferior;
    xSuperior = 250;
    xInferior = 650;
    ySuperior = yInferior = 200;
    double deltaX1 = 400/listaPuntos.getLongitud();
    double deltaX2 = 400/lista2.getLongitud();
    for(Punto p : listaPuntos){
      p.x = xSuperior;
      p.y = ySuperior;
      xSuperior = xSuperior+deltaX1;
      ySuperior = funcionCircular(xSuperior, 1);
    }
    for(Punto p : lista2){
      p.x = xInferior;
      p.y = yInferior;
      xInferior = xInferior-deltaX2;
      yInferior = funcionCircular(xInferior, -1);
    }
    for(Punto p : lista2)
      listaPuntos.agregaFinal(p);
  }
  /**
  * circunferencia
  */
  public double funcionCircular(double x, int signo){
    return (signo * Math.sqrt(Math.pow(200,2)-Math.pow((x-450),2))) + 210;
  }
  /**
  * Adibujar la gráfica
  */
  public String dibujaGrafica(){
    estableceDimensiones(700, 420);
    Lista<Punto> listaPuntos = generaPuntos();
    if(listaPuntos.getLongitud() == 0) return "Gráfica Vacía";
    asignaCoordenadas(listaPuntos);
    String svgPuntos = dibujaPuntos(listaPuntos);
    String svgAristas = dibujaAristas(listaPuntos);
    return estructuraSVG+svgPuntos+svgAristas+"</svg>";
  }
}
