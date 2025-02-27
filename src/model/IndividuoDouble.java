package model;

import java.util.Random;

public abstract class IndividuoDouble extends Individuo<Double> {

    public double[] min;
    public double[] max;

    public IndividuoDouble(int numGens) {
        this.rand = new Random();
        this.numGens = numGens;
    }

    // Método auxiliar que genera un double en [0,1] inclusivo.
    protected double nextDoubleInclusive() {
        long bits = (rand.nextLong() & Long.MAX_VALUE); // Valor en [0, Long.MAX_VALUE]
        return (double) bits / (double) Long.MAX_VALUE;  // Valor en [0, 1] inclusivo
    }

    public void initGens(double[] mins, double[] maxs) {
        this.min = mins;
        this.max = maxs;

        this.chromosome = new Double[numGens];

        for (int i = 0; i < numGens; i++) {
            // Ahora se usa nextDoubleInclusive() para alcanzar el máximo
            this.chromosome[i] = mins[i] + nextDoubleInclusive() * (maxs[i] - mins[i]);
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
                // También se usa nextDoubleInclusive() en la mutación
                this.chromosome[i] = min[i] + nextDoubleInclusive() * (max[i] - min[i]);
            }
        }
    }
}
