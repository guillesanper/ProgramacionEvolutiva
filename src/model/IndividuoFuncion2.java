package model;

import java.util.Random;

public class IndividuoFuncion2 extends Individuo<Boolean> {

    private final Random rand;
    public double[] min;
    public double[] max;
    public double errorValue;
    public int[] genesSize;
    public int totalSize;

    public IndividuoFuncion2() {
        this.rand = new Random();
        this.genesSize = new int[2];
        this.min = new double[]{-10.0, -6.5}; // Rango de x1 y x2
        this.max = new double[]{0.0, 0.0};   // Rango de x1 y x2
        this.errorValue = 1e-6; // Precisión

        this.genesSize[0] = tamGen(this.errorValue, min[0], max[0]);
        this.genesSize[1] = tamGen(this.errorValue, min[1], max[1]);

        this.totalSize = genesSize[0] + genesSize[1];
        this.chromosome = new Boolean[this.totalSize];

        for (int i = 0; i < this.totalSize; i++) {
            this.chromosome[i] = this.rand.nextBoolean();
        }
    }

    public int tamGen(double precision, double min, double max) {
        return (int) (Math.log10(((max - min) / precision) + 1) / Math.log10(2));
    }

    public double getPhenotype(int n) {
        Boolean[] v = new Boolean[this.genesSize[n]];
        int offset = (n == 0) ? 0 : this.genesSize[0];

        for (int i = 0; i < this.genesSize[n]; i++) {
            v[i] = this.chromosome[offset + i];
        }

        return this.min[n] + bin2dec(v) * ((this.max[n] - this.min[n]) / (Math.pow(2, this.genesSize[n]) - 1));
    }

    private long bin2dec(Boolean[] gen) {
        long result = 0;
        for (boolean bit : gen) {
            result = result * 2 + (bit ? 1 : 0);
        }
        return result;
    }

    @Override
    public double[] getPhenotypes() {
        return new double[]{getPhenotype(0), getPhenotype(1)};
    }

    @Override
    public double getFitness() {
        double x1 = getPhenotype(0);
        double x2 = getPhenotype(1);

        // Función de Mishra Bird
        return Math.sin(x2) * Math.exp(Math.pow(1 - Math.cos(x1), 2))
                + Math.cos(x1) * Math.exp(Math.pow(1 - Math.sin(x2), 2))
                + Math.pow(x1 - x2, 2);
    }

    @Override
    public void mutate(double p) {
        for (int i = 0; i < this.totalSize; i++) {
            if (this.rand.nextDouble() < p) {
                this.chromosome[i] = !this.chromosome[i];
            }
        }
    }
}
