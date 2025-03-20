package logic.mutacion;

import java.util.Random;

public class MutacionIntercambio implements Mutate {
    @Override
    public Integer[] mutate(Integer[] originalChromosome) {
        Random rand = new Random();
        Integer[] chromosome = originalChromosome.clone();


        int pos1 = rand.nextInt(chromosome.length);
        int pos2 = rand.nextInt(chromosome.length);

        if (pos1 == pos2) {
            if (pos1 == 0) {
                pos1++;
            }
        }

        chromosome[pos1] = originalChromosome[pos2];
        chromosome[pos2] = originalChromosome[pos1];

        return chromosome;
    }

}
