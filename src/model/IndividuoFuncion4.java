package model;

public class IndividuoFuncion4 extends IndividuoBoolean {

    public IndividuoFuncion4(double errorValue, int d) {
        super(errorValue, d);
        double[] mins = new double[d];
        double[] maxs = new double[d];

        for (int i = 0; i < d; i++) {
            mins[i] = 0;
            maxs[i] = Math.PI;
        }

        this.initGens(mins, maxs);
    }

    @Override
    public double getFitness() {
        return 0;
    }
}
