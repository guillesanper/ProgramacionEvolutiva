package logic.cruce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CruceCO extends Cruce<Integer> {

    public CruceCO(int tamCromosoma) {
        super(tamCromosoma);
    }

    @Override
    public void cross(Integer[] c1, Integer[] c2) {
        Random rand = new Random();
        this.tamCromosoma = c1.length;

        // Codificamos las permutaciones a representación ordinal
        Integer[] p1cod = codificarOrdinal(c1);
        Integer[] p2cod = codificarOrdinal(c2);
        // Generamos un punto de cruce aleatorio (cruce de un punto)
        int cut = rand.nextInt(tamCromosoma-1)+1;

        // Realizamos el cruce de permutaciones
        Integer[] hijoCod1 = crearHijo(p1cod, p2cod, cut);
        Integer[] hijoCod2 = crearHijo(p2cod, p1cod, cut);

        // Decodificamos los cromosomas ordinales a permutaciones de habitaciones
        Integer[] hijo1 = decodificarOrdinal(hijoCod1);
        Integer[] hijo2 = decodificarOrdinal(hijoCod2);

        // Copiamos los hijos a los padres
        System.arraycopy(hijo1, 0, c1, 0, tamCromosoma);
        System.arraycopy(hijo2, 0, c2, 0, tamCromosoma);
    }

    /**
     * Decodifica un cromosoma de codificación ordinal a una permutación de habitaciones.
     * Se reconstruye la permutación original utilizando la lista de elementos disponibles.
     * Para cada gen se toma su valor como índice (con ajuste si fuera necesario) en la lista de disponibles,
     * se extrae el elemento correspondiente y se elimina de dicha lista.
     *
     * @param codificacion El cromosoma en codificación ordinal.
     * @return La permutación de habitaciones correspondiente.
     */
    private Integer[] decodificarOrdinal(Integer[] codificacion) {
        Integer[] cromosoma = new Integer[tamCromosoma];
        List<Integer> disponibles = new ArrayList<>();

        // Inicializamos la lista de elementos disponibles (1 a tamCromosoma)
        for (int i = 1; i <= tamCromosoma; i++) {
            disponibles.add(i);
        }

        // Para cada gen, se toma su valor como índice en la lista disponible.
        // Si el índice es mayor que el tamaño de la lista (lo que podría ocurrir en caso de ajuste),
        // se usa el último índice disponible.
        for (int i = 0; i < tamCromosoma; i++) {
            int index = codificacion[i];
            cromosoma[i] = disponibles.get(index);
            disponibles.remove(index);
        }

        return cromosoma;
    }


    /**
     * Codifica una permutación de habitaciones a un cromosoma de codificación ordinal.
     * Para cada habitacion se determina su posición relativa en la lista dinámica (disponibles).
     * @param cromosoma La permutación de habitaciones.
     * @return El cromosoma en codificación ordinal.
     */
    private Integer[] codificarOrdinal(Integer[] cromosoma) {
        Integer[] codificacion = new Integer[tamCromosoma];
        List<Integer> disponibles = new ArrayList<>();

        // Inicializamos la lista de elementos disponibles (1 a tamCromosoma)
        for (int i = 1; i <= tamCromosoma; i++) {
            disponibles.add(i);
        }

        // Para cada posición, se extrae el elemento en la posición indicada por el código ordinal.
        // Si el valor es mayor que el tamaño de la lista, se ajusta al último índice disponible.
        for (int i = 0; i < tamCromosoma; i++) {
            int ordinal = cromosoma[i];
            int index = disponibles.indexOf(ordinal);
            codificacion[i] = index;
            disponibles.remove(index);
        }

        return codificacion;
    }

    private Integer[] crearHijo(Integer[] c1, Integer[] c2, int cut) {
        // Realizamos el cruce de permutaciones
        Integer[] hijo = new Integer[tamCromosoma];

        Integer[] firstPart = Arrays.copyOfRange(c1, 0, cut-1);
        Integer[] secondPart = Arrays.copyOfRange(c2, cut-1, c2.length);

        System.arraycopy(firstPart, 0, hijo, 0, firstPart.length);
        System.arraycopy(secondPart, 0, hijo, firstPart.length, secondPart.length);

        return hijo;
    }

}
