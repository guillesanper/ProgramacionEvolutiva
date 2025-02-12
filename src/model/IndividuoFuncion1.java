package model;

public class IndividuoFuncion1 extends IndividuoBoolean {
    public IndividuoFuncion1(double errorValue){
        super(errorValue, 2);

        double[] mins = new double[]{-3, 4.1};
        double[] maxs = new double[]{12.1, 5.8};
        this.initGens(mins, maxs);
    }

    @Override
    public double getFitness() {
        return 0;
    }
}
