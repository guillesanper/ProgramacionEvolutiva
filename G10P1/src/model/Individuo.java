package model;

import java.util.Random;

public abstract class Individuo<T> {
    protected Random rand ;

    public T[] chromosome;



    public int numGens;

    public double[] min;
    public double[] max;

    public double fitness;

    public abstract double getPhenotype(int n);

    public abstract double getFitness();

    public abstract void mutate(double p);

    public double[] getPhenotypes() {
        double[] phenotypes = new double[this.numGens];
        for (int i = 0; i < this.numGens; i++) {
            phenotypes[i] = this.getPhenotype(i);
        }

        return phenotypes;
    }


}
