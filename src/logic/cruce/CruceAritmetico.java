package logic.cruce;

public class CruceAritmetico extends Cruce<Double> {

    private final double alpha = 0.6;

    public CruceAritmetico(int tamCromosoma) {
        super(tamCromosoma);
    }

    @Override
    public void cross(Double[] c1, Double[] c2) {
        double hijo1, hijo2;
        for (int i = 0; i < c1.length; i++) {
            hijo1 = alpha * c1[i] + (1 - alpha) * c2[i];
            hijo2 = alpha * c2[i] + (1 - alpha) * c1[i];
            c1[i] = hijo1;
            c2[i] = hijo2;
        }
    }

}
