package logic.cruce;

import java.util.Random;

public class CrucePMX extends Cruce<Integer> {

    public CrucePMX(int tamCromosoma) {
        super(tamCromosoma);
    }

    @Override
    public void cross(Integer[] parent1, Integer[] parent2) {
        // Verificamos que ambos padres tengan la misma longitud
        if (parent1.length != parent2.length) {
            throw new IllegalArgumentException("Los padres deben tener la misma longitud.");
        }
        int length = parent1.length;
        // Se generan dos hijos aplicando PMX en sentidos opuestos
        Integer[] child1 = pmxCrossover(parent1, parent2);
        Integer[] child2 = pmxCrossover(parent2, parent1);

        // Se copian los hijos en los arreglos de los padres (operación in-place)
        System.arraycopy(child1, 0, parent1, 0, length);
        System.arraycopy(child2, 0, parent2, 0, length);
    }

    /**
     * Aplica el operador PMX para generar un hijo a partir de dos padres.
     * p1 define el segmento a copiar y p2 se utiliza para completar el resto.
     */
    private Integer[] pmxCrossover(Integer[] p1, Integer[] p2) {
        int length = p1.length;
        // Creamos un arreglo hijo inicialmente vacío (llenado de null)
        @SuppressWarnings("unchecked")
        Integer[] child =  new Integer[length];
        for (int i = 0; i < length; i++) {
            child[i] = null;
        }
        Random rand = new Random();
        // Elegir dos puntos de corte aleatorios
        int cut1 = rand.nextInt(length);
        int cut2 = rand.nextInt(length);
        if (cut1 > cut2) {
            int temp = cut1;
            cut1 = cut2;
            cut2 = temp;
        }
        // Copiar el segmento de p1 en el hijo
        for (int i = cut1; i <= cut2; i++) {
            child[i] = p1[i];
        }
        // Para cada posición del segmento en p2, si el gen no está ya en el hijo, se reubica
        for (int i = cut1; i <= cut2; i++) {
            Integer gene = p2[i];
            if (!contains(child, gene)) {
                int pos = i;
                // Se busca la posición en la que colocar el gen conflictivo
                while (child[pos] != null) {
                    // Se obtiene el gen de p1 en esta posición (que ya está en el hijo)
                    Integer mappedGene = p1[pos];
                    // Se busca la posición en p2 en la que aparece dicho gen
                    pos = indexOf(p2, mappedGene);
                }
                child[pos] = gene;
            }
        }
        // Rellenar las posiciones vacías con los genes de p2
        for (int i = 0; i < length; i++) {
            if (child[i] == null) {
                child[i] = p2[i];
            }
        }
        return child;
    }

    /**
     * Devuelve el índice en el arreglo donde se encuentra el elemento.
     * Retorna -1 si no se encuentra.
     */
    private int indexOf(Integer[] array, Integer element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Retorna true si el arreglo contiene el elemento (comparación con equals), ignorando null.
     */
    private boolean contains(Integer[] array, Integer element) {
        for (Integer item : array) {
            if (item != null && item.equals(element)) {
                return true;
            }
        }
        return false;
    }
}
