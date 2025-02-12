package model;

public class IndividuoFuncion2 extends IndividuoBoolean {

    public IndividuoFuncion2(double errorValue) {
        super(errorValue, 2);
        double[] mins = new double[]{-10.0, -6.5}; // Rango de x1 y x2
        double[] maxs = new double[]{0.0, 0.0};   // Rango de x1 y x2

        this.initGens(mins, maxs);
    }

    @Override
    public double getFitness() {
        return 0;
    }
}
