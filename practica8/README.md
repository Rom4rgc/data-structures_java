Estructuras de Datos
====================

Práctica 8: Trayectoría mínima y algoritmo de Dijkstra
------------------------------------------------------

### Fecha de entrega: martes 30 de abril, 2024

Deben modificar su clase
[`Grafica`](https://aztlan.fciencias.unam.mx/gitlab/2024-2-edd/practica8/-/blob/main/src/main/java/mx/unam/ciencias/edd/Grafica.java)
para que tenga pesos en sus aristas. Además, deben implementar los nuevos métodos
[`trayectoriaMinima()`](https://aztlan.fciencias.unam.mx/gitlab/2024-2-edd/practica8/-/blob/main/src/main/java/mx/unam/ciencias/edd/Grafica.java#L395)
y
[`dijkstra()`](https://aztlan.fciencias.unam.mx/gitlab/2024-2-edd/practica8/-/blob/main/src/main/java/mx/unam/ciencias/edd/Grafica.java#L410).

Una vez que hayan terminado sus clases, la práctica debe compilar al hacer:

```
$ mvn compile
```

También debe pasar todas sus pruebas unitarias al hacer:

```
$ mvn test
```

Y por último, debe correr correctamente el programa escrito en la clase
[`Practica8`](https://aztlan.fciencias.unam.mx/gitlab/2024-2-edd/practica8/-/blob/main/src/main/java/mx/unam/ciencias/edd/Practica8.java)
al ejecutar:

```
$ mvn install
...
$ java -jar target/practica8.jar
```

Los únicos archivos que deben modificar son:

* `ArbolAVL.java`,
* `ArbolBinario.java`,
* `ArbolBinarioCompleto.java`,
* `ArbolBinarioOrdenado.java`,
* `ArbolRojinegro.java`,
* `Arreglos.java`,
* `Cola.java`,
* `Grafica.java`,
* `Lista.java`,
* `MeteSaca.java`,
* `MonticuloArreglo.java`,
* `MonticuloMinimo.java`,
* `Pila.java`, y
* `ValorIndexable.java`.

*No deben modificar de ninguna manera ninguno de los otros archivos de la
práctica*.

### Repositorio

Pueden clonar la práctica con el siguiente comando:

```
$ git clone https://aztlan.fciencias.unam.mx/gitlab/2024-2-edd/practica8.git
```

### Documentación

La documentación generada por JavaDoc la pueden consultar aquí:

[Documentación generada por JavaDoc para la práctica
8](https://aztlan.fciencias.unam.mx/~canek/2024-2-edd/practica8/apidocs/index.html)

### Capítulos del libro

Los capítulos [del
libro](https://tienda.fciencias.unam.mx/es/home/437-estructuras-de-datos-con-java-moderno-9786073009157.html)
relacionados a esta práctica son:

19. Algoritmo de Dijkstra
