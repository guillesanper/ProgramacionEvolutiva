package model;

public abstract class Individuo<T> {

    public T[] chromosome;

    public int[] genesSize;

    public int numGens;

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
