package model;

import java.util.Random;

public abstract class IndividuoDouble extends Individuo<Double> {

    public IndividuoDouble(int numGens) {
        this.rand = new Random();
        this.numGens = numGens;
    }

    public void initGens(double[] mins, double[] maxs) {
    }
    @Override
    public double getPhenotype(int n) {
        return this.chromosome[n];
    }

    @Override
    public void mutate(double p) {
    }
}
