package mx.unam.ciencias.edd.proyecto1;

import java.text.Collator;

public class Oracion implements Comparable<Oracion> {
    private String oracion;

    public Oracion(String oracion) {
        this.oracion = oracion;
    }

    @Override
    public String toString() {
        return this.oracion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Oracion oracionObj = (Oracion) o;
        // Considerar utilizar Collator aquí si desea ser consistente con la comparación ignorando acentos y mayúsculas/minúsculas.
        return this.oracion.equals(oracionObj.oracion);
    }

    @Override
    public int hashCode() {
        // Si decides modificar cómo funciona equals (por ejemplo, ignorando mayúsculas, minúsculas, acentos), asegúrate de ajustar también aquí.
        return oracion.hashCode();
    }

    @Override
    public int compareTo(Oracion c) {
        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.PRIMARY);
        // La comparación se realiza directamente entre las oraciones sin remover caracteres no alfabéticos para mantener consistencia con equals.
        return collator.compare(this.oracion, c.oracion);
    }
}

