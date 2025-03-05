package utils;

import model.Individuo;

public class HistoryState {
    double[][] vals;
    Pair<Double, Double> interval;
    Individuo best;
    int crossed;
    int muted;

    public double[][] getVals() {
        return vals;
    }

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

    public HistoryState(double[][] vals, Pair<Double, Double> interval, Individuo best, int crossed, int muted) {
        this.vals = vals;
        this.interval = interval;
        this.best = best;
        this.crossed = crossed;
        this.muted = muted;
    }
}
