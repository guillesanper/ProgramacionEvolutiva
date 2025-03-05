package logic.cruce;

import java.util.Random;

public class CruceBLXalpha extends Cruce<Double> {

    private double alpha = 0.3;
    private Random rand = new Random();

    public CruceBLXalpha(int tamCromosoma) {
        super(tamCromosoma);
    }

    /*
     * Para dos genes x e y:
     * - Se define I = max(x,y) - min(x,y)
     * - El intervalo extendido es: [min - I*alpha, max + I*alpha]
     * - Su ancho es I * (1 + 2*alpha)
     * Se genera cada nuevo gen con:
     *    valor = min - I*alpha + rand.nextDouble() * (I*(1+2*alpha))
     */
    @Override
    public void cross(Double[] c1, Double[] c2) {
        double min, max, I;
        for (int i = 0; i < this.tamCromosoma; i++) {
            min = Math.min(c1[i], c2[i]);
            max = Math.max(c1[i], c2[i]);
            I = max - min;
            if (I != 0) {
                double range = I * (1 + 2 * alpha);
                double lowerBound = min - I * alpha;
                c1[i] = lowerBound + rand.nextDouble() * range;
                c2[i] = lowerBound + rand.nextDouble() * range;
            }
        }
    }
}
