package logic.cruce;

import java.util.Random;

public class CruceMonoPunto<T> extends Cruce<T> {
    public CruceMonoPunto( int tam_cromosoma) {
        super(tam_cromosoma);
    }

    @Override
    public void cross(T[] c1, T[] c2) {
        int i = 0;
        T temp;
        Random rand = new Random();
        int x = rand.nextInt(c1.length ) ; // Evita cruce en extremos
        for( i = 0; i < x; i++) {
            temp = c1[i];
            c1[i] = c2[i];
            c2[i] = temp;
        }
    }
}
