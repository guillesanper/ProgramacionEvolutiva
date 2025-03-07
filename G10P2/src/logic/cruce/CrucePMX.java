package logic.cruce;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

        Random rand = new Random();

        // Elegir dos puntos de corte aleatorios
        int cut1 = rand.nextInt(length);
        int cut2 = rand.nextInt(length);


        if (cut1 > cut2) {
            int temp = cut1;
            cut1 = cut2;
            cut2 = temp;
        }
        // Se generan dos hijos aplicando PMX en sentidos opuestos
        Integer[] child1 = pmxCrossover(parent1, parent2, cut1, cut2);
        Integer[] child2 = pmxCrossover(parent2, parent1, cut1, cut2);

        // Se copian los hijos en los arreglos de los padres (operación in-place)
        System.arraycopy(child1, 0, parent1, 0, length);
        System.arraycopy(child2, 0, parent2, 0, length);
    }

    /**
     * Aplica el operador PMX para generar un hijo a partir de dos padres.
     * p1 define el segmento a copiar y p2 se utiliza para completar el resto.
     */
    private Integer[] pmxCrossover(Integer[] p1, Integer[] p2, int cut1, int cut2) {
        Set<Integer> metidos = new HashSet<>();

        int length = p1.length;
        // Creamos un arreglo hijo inicialmente vacío (llenado de null)
        Integer[] child =  new Integer[length];
        // Copiar el segmento de p1 en el hijo
        for (int i = cut1; i <= cut2; i++) {
            child[i] = p2[i];
            metidos.add(p2[i]);
        }
        // Para cada posición del segmento en p2, si el gen no está ya en el hijo, se reubica
        for (int i = 0; i < length; i++) {
            if (i >= cut1 && i <= cut2) continue;
            int gene = p1[i];
            if (!metidos.contains(gene)) {
                child[i] = gene;
                metidos.add(gene);
            }
        }

        // Rellenar las posiciones vacías con el
        for (int i = 0; i < length; i++) {
            if (child[i] == null) {
                // buscar pos en la cadena del otro padre
                int pos = indexOf(p2, p1[i]);
                // cambiarlo por el de mi misma pos
                child[i] = p1[pos];
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
}
