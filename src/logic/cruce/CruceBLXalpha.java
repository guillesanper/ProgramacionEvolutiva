package logic.cruce;

import java.util.Random;

public class CruceBLXalpha extends Cruce<Double> {

    private double alpha = 0.5;

    public CruceBLXalpha(int tamCromosoma) {
        super(tamCromosoma);
    }

    /*
    * - Se define el intervalo I = max(xi,yi) - min(xi,yi)
    * - Se amplia el intervalo en ambos lados con alpha de la siguiente manera:
    *           nuevo_rango = [min-I*alpha, max+I*alpha]
    * La expresion se puede abreviar->(max+I⋅alpha)−(min−I⋅alpha)=I+Ialpha+Ialpha=I(1+2alpha)
    * - Como utilizamos nextDouble-> min−Ialpha+rand.nextDouble()*2Ialpha
     * */

    @Override
    public void cross(Double[] c1, Double[] c2) {
        double min, max, I;
        Random rand = new Random();
        for (int i = 0; i < this.tamCromosoma; i++) {
            min = Math.min(c1[i], c2[i]);
            max = Math.max(c1[i], c2[i]);
            I = max - min;
            if (I != 0) {
                double range = 2 * I * alpha;
                c1[i] = min - I * alpha + rand.nextDouble() * range;
                c2[i] = min - I * alpha + rand.nextDouble() * range;
            }
        }
    }
}
