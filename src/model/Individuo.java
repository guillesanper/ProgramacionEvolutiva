package model;

public abstract class Individuo<T> {

    public T[] cromosoma;

    public int[] tamGenes;

    public double fitness;

    public double[] fenotipo;

    public abstract double evaluateFitness();

}
