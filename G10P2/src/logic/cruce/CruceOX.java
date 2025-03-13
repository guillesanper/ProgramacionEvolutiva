// Java
package logic.cruce;

import java.util.Random;

public class CruceOX extends Cruce<Integer>{

    public CruceOX(int tamCromosoma) {
        super(tamCromosoma);
    }

    @Override
    public void cross(Integer[] parent1, Integer[] parent2) {
        // Verify that both parents have the same length.
        if (parent1.length != parent2.length) {
            throw new IllegalArgumentException("Parents must have the same length.");
        }
        int length = parent1.length;

        // Create offspring arrays.
        Integer[] child1 = new Integer[length];
        Integer[] child2 = new Integer[length];

        Random rnd = new Random();
        // Select two random crossover points.
        int cut1 = rnd.nextInt(length);
        int cut2 = rnd.nextInt(length);
        if (cut1 > cut2) {
            int temp = cut1;
            cut1 = cut2;
            cut2 = temp;
        }

        // Copy the segment from parent1 to child1 and from parent2 to child2.
        for (int i = cut1; i <= cut2; i++) {
            child1[i] = parent1[i];
            child2[i] = parent2[i];
        }

        // Fill the remaining positions for child1 using parent2's order.
        int current = (cut2 + 1) % length;
        for (int i = 0; i < length; i++) {
            int idx = (cut2 + 1 + i) % length;
            if (!contains(child1, parent2[idx])) {
                child1[current] = parent2[idx];
                current = (current + 1) % length;
            }
        }

        // Fill the remaining positions for child2 using parent1's order.
        current = (cut2 + 1) % length;
        for (int i = 0; i < length; i++) {
            int idx = (cut2 + 1 + i) % length;
            if (!contains(child2, parent1[idx])) {
                child2[current] = parent1[idx];
                current = (current + 1) % length;
            }
        }

        // Copy offspring back into the parents array (in-place modification).
        System.arraycopy(child1, 0, parent1, 0, length);
        System.arraycopy(child2, 0, parent2, 0, length);
    }

    // Helper method to check for duplicate genes (ignoring null).
    private boolean contains(Integer[] array, Integer gene) {
        for (Integer value : array) {
            if (value != null && value.equals(gene)) {
                return true;
            }
        }
        return false;
    }
}