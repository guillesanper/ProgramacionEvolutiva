package logic.mutacion;

import java.util.Arrays;
import java.util.Random;

public class MutacionInvencion implements Mutate {
    @Override
    public Integer[] mutate(Integer[] chromosome) {
        Integer[] mutation = new Integer[chromosome.length];
        // 1 2 3 4 | 5 6 7 8 9
        // 0 1 2 3 | 4 5 6 7 8
        int cut = new Random().nextInt(chromosome.length-1)+1;

        Integer[] firstHalf = Arrays.copyOfRange(chromosome, 0, cut);
        Integer[] secondHalf = Arrays.copyOfRange(chromosome, cut, chromosome.length);

        System.arraycopy(secondHalf, 0, mutation, 0, secondHalf.length);
        System.arraycopy(firstHalf, 0, mutation, secondHalf.length, firstHalf.length);


        return mutation;
    }
}
