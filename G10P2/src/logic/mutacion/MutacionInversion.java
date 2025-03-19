package logic.mutacion;

import java.util.Random;

public class MutacionInversion implements Mutate{

    @Override
    public void mutate(Integer[] chromosome) {
        Random rand = new Random();

        int length = chromosome.length;
        int start = rand.nextInt(length);
        int end = rand.nextInt(length);
        if(start>end){
            int temp = start;
            start = end;
            end = temp;
        }

        Integer[] temp = new Integer[end-start+1];
        for(int i = 0; i < temp.length; i++){
            temp[i] = chromosome[start+i];
        }

        for(int i = 0; i < temp.length; i++){
            chromosome[start+i] = temp[temp.length-1-i];
        }
    }
}
