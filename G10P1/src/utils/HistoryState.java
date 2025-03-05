package utils;

import model.Individuo;

public class HistoryState {
    private double[][] vals;
    Pair<Double, Double> interval;
    Individuo best;

    public double[][] getVals() {
        return vals;
    }

    public Pair<Double, Double> getInterval() {
        return interval;
    }

    public Individuo getBest() {
        return best;
    }

    public HistoryState(double[][] vals, Pair<Double, Double> interval, Individuo best) {
        this.vals = vals;
        this.interval = interval;
        this.best = best;
    }
}
