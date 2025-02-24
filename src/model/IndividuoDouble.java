package model;

import java.util.Random;

public abstract class IndividuoDouble extends Individuo<Double> {

    public double[] min;
    public double[] max;

    public IndividuoDouble(int numGens) {
        this.rand = new Random();
        this.numGens = numGens;
    }

    public void initGens(double[] mins, double[] maxs) {
        this.min = mins;
        this.max = maxs;

        this.chromosome = new Double[numGens];

        for (int i = 0; i < numGens; i++) {
            this.chromosome[i] = min[i] + rand.nextDouble() * (max[i] - min[i]); // Generación en el rango [min[i], max[i]]
        }

    }

    @Override
    public double getPhenotype(int n) {
        return this.chromosome[n];
    }

    @Override
    public void mutate(double p) {
        for (int i = 0; i < numGens; i++) {
            if (this.rand.nextDouble() < p) {
                this.chromosome[i] = min[i] + rand.nextDouble() * (max[i] - min[i]); // Generación en el rango [min[i], max[i]]
            }
        }
    }
}
