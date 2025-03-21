package utils;

import model.Individuo;

public class HistoryState {
    double[][] vals;
    Pair<Double, Double> interval;
    Individuo best;
    int crossed;
    int muted;
    double worstFitness;

    public double[][] getVals() {
        return vals;
    }

    public double getWorstFitness() { return worstFitness; }

    public Pair<Double, Double> getInterval() {
        return interval;
    }

    public Individuo getBest() {
        return best;
    }

    public int getCrossed() {
        return crossed;
    }

    public int getMuted() {
        return muted;
    }

    public HistoryState(double[][] vals, Pair<Double, Double> interval, Individuo best, int crossed, int muted, double worstFitness) {
        this.vals = vals;
        this.interval = interval;
        this.best = best;
        this.crossed = crossed;
        this.muted = muted;
        this.worstFitness = worstFitness;
    }
}
