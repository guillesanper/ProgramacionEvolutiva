package logic.mutacion;

import java.util.Random;

public class MutacionInversion implements Mutate {

    @Override
    public Integer[] mutate(Integer[] chromosome) {
        Random rand = new Random();

        int length = chromosome.length;
        int start = rand.nextInt(length);
        int end = rand.nextInt(length);
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }

        Integer[] newChromosome = chromosome.clone();
        Integer[] temp = new Integer[end - start + 1];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = newChromosome[start + i];
        }

        for (int i = 0; i < temp.length; i++) {
            newChromosome[start + i] = temp[temp.length - 1 - i];
        }

        return newChromosome;
    }
}