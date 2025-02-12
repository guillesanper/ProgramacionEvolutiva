package model;

public class IndividuoFuncion3 extends IndividuoBoolean{

    public IndividuoFuncion3(double errorValue) {
        super(errorValue, 2);

        double[] mins = new double[]{-10, -10};
        double[] maxs = new double[]{10, 10};

        this.initGens(mins, maxs);
    }

    @Override
    public double getFitness() {
        return 0;
    }
}
