package model;

public class IndividuoFuncion5 extends Individuo<Double> {
    public IndividuoFuncion5(double errorValue) {
    }

    @Override
    public double[] getPhenotypes() {
        return new double[0];
    }

    @Override
    public double getPhenotype(int n) {
        return 0;
    }

    @Override
    public double getFitness() {
        return 0;
    }

    @Override
    public void mutate(double p) {

    }
}
