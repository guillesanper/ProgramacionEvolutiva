package logic.mutacion;

import java.util.Random;

public class MutacionIntercambio implements Mutate {
    @Override
    public void mutate(Integer[] chromosome) {
        Random rand = new Random();

        int pos1 = rand.nextInt(chromosome.length);
        int pos2 = rand.nextInt(chromosome.length);

        if (pos1 == pos2) {
            if (pos1 == 0) {
                pos1++;
            }
        }

        int temp = chromosome[pos1];
        chromosome[pos1] = chromosome[pos2];
        chromosome[pos2] = temp;
    }

}
