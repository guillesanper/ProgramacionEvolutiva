package model;

public abstract class Individuo<T> {

    public T[] chromosome;

    public int[] genesSize;

    public double fitness;

    public abstract double[] getPhenotypes();

    public abstract double getFitness();

    public abstract void mutate(double p);

}
