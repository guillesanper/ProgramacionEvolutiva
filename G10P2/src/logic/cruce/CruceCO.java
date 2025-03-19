package logic.cruce;

import java.util.ArrayList;
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
        int puntoCruce = rand.nextInt(tamCromosoma - 1) + 1;

        // Realizamos el cruce de permutaciones
        Integer[] hijoPermutacion1 = new Integer[tamCromosoma];
        Integer[] hijoPermutacion2 = new Integer[tamCromosoma];

        // Copiamos la primera parte de cada padre
        for (int i = 0; i < puntoCruce; i++) {
            hijoPermutacion1[i] = p1cod[i];
            hijoPermutacion2[i] = p2cod[i];
        }

        // Completamos los hijos con los elementos restantes en el orden del otro padre
        completarHijoPermutacion(p2cod, hijoPermutacion1, puntoCruce);
        completarHijoPermutacion(p1cod, hijoPermutacion2, puntoCruce);

        // Decodificamos los cromosomas ordinales a permutaciones de habitaciones
        Integer[] hijo1 = decodificarOrdinal(hijoPermutacion1);
        Integer[] hijo2 = decodificarOrdinal(hijoPermutacion2);

        // Copiamos los hijos a los padres
        System.arraycopy(p1cod, 0, c1, 0, tamCromosoma);
        System.arraycopy(p2cod, 0, c2, 0, tamCromosoma);
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

    /**
     * Completa un hijo añadiendo los elementos restantes en el orden en que aparecen en el otro padre,
     * evitando duplicados.
     * @param padreOrigen El padre que proporciona el orden.
     * @param hijo El hijo que se está completando.
     * @param puntoCruce El punto de cruce.
     */
    private void completarHijoPermutacion(Integer[] padreOrigen, Integer[] hijo, int puntoCruce) {
        // Array para marcar los elementos ya incluidos en el hijo.
        // Asumimos que los valores válidos están entre 0 y tamCromosoma-1.
        boolean[] elementosIncluidos = new boolean[tamCromosoma];

        // Marcamos los elementos ya copiados en el hijo.
        for (int i = 0; i < puntoCruce; i++) {
            elementosIncluidos[hijo[i]] = true;
        }

        int indiceHijo = puntoCruce;
        // Recorremos el padre origen e incorporamos los elementos que aún no se hayan incluido.
        for (int i = 0; i < tamCromosoma && indiceHijo < tamCromosoma; i++) {
            int valor = padreOrigen[i];
            if (!elementosIncluidos[valor]) {
                hijo[indiceHijo] = valor;
                elementosIncluidos[valor] = true;
                indiceHijo++;
            }
        }

        // Si por alguna razón aún faltan elementos (lo cual no debería ocurrir en una permutación válida),
        // los completamos buscando los valores que no se han incluido.
        if (indiceHijo < tamCromosoma) {
            for (int valor = 0; valor < tamCromosoma && indiceHijo < tamCromosoma; valor++) {
                if (!elementosIncluidos[valor]) {
                    hijo[indiceHijo] = valor;
                    indiceHijo++;
                }
            }
        }
    }

}
