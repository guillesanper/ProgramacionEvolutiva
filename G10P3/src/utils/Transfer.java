package utils;

import model.Individuo;

public class Transfer {
    public Transfer(double[][] vals, Pair<Double, Double> interval, Individuo best, boolean save, int crossed, int muted, double worstFitness) {
        this.vals = vals;
        this.interval = interval;
        this.best = best;
        this.save = save;
        this.crossed = crossed;
        this.muted = muted;
        this.worstFitness = worstFitness;
    }

    public double[][] vals;
    public Pair<Double, Double> interval;
    public Individuo best;
    public boolean save;
    public int crossed;
    public int muted;
    public double worstFitness;
}