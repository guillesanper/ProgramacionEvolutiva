package model;

import java.util.Random;

public abstract class IndividuoDouble extends Individuo<Double> {

    public IndividuoDouble(int numGens) {
        this.rand = new Random();
        this.numGens = numGens;
    }


    public void initGens(double[] mins, double[] maxs) {
        this.min = new double[numGens];
        this.max = new double[numGens];

        for (int i = 0; i < this.numGens; i++) {
            this.min[i] = mins[i];
            this.max[i] = maxs[i];
        }

        this.chromosome = new Double[this.numGens];

        for(int i = 0; i < this.numGens; i++)
            this.chromosome[i] = randomRange(this.min[i], this.max[i]);
    }

    @Override
    public double getPhenotype(int n) {
        return this.chromosome[n];
    }

    @Override
    public void mutate(double p) {
        // MUTACION UNIFORME

        for (int i = 0; i < this.numGens; i++) {
            if (this.rand.nextDouble() < p) {
                this.chromosome[i] = randomRange(this.min[i], this.max[i]);
            }
        }
    }

    private double randomRange(double lb, double ub) {
        return lb + this.rand.nextDouble()*(ub-lb);
    }
}
