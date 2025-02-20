package logic.cruce;
import java.util.Random;

public class CruceUniforme<T> extends Cruce<T>{

        public CruceUniforme(int tamCromosoma) {
            super(tamCromosoma);
        }

        //Con una probabilidad de 0.5 intercambia los cromosomas dados
        public void cross(T[] c1, T[] c2) {
            int i = 0;
            T temp = null;
            Random rand = new Random();
            double x ;
            for( i = 0; i < c1.length; i++) {
                x = rand.nextDouble();
                if(x > 0.5) {
                    temp = c1[i];
                    c1[i] = c2[i];
                    c2[i] = temp;
                }
            }
        }
}
