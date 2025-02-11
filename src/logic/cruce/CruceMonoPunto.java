package logic.cruce;

import model.Individuo;

import java.util.Random;

public class CruceMonoPunto<T> extends Cruce<T> {
    public CruceMonoPunto( int tam_cromosoma) {
        super(tam_cromosoma);
    }

    @Override
    public void cruzar(T[] c1, T[] c2) {
        int i = 0;
        T temp;
        Random rand = new Random();
        int x = rand.nextInt(this.tamCromosoma);
        for( i = 0; i < x; i++) {
            temp = c1[i];
            c1[i] = c2[i];
            c2[i] = temp;
        }
    }
}
