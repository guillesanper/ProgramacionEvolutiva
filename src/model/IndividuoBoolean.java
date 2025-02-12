package model;

import java.util.Random;

public abstract class IndividuoBoolean extends Individuo<Boolean> {
    protected final Random rand ;
    public double[] min;
    public double[] max;
    public double errorValue;
    public double totalSize;

    public IndividuoBoolean(double errorValue, int numGens){
        this.rand = new Random();
        this.numGens = numGens;
        this.errorValue = errorValue;
    }

    public void initGens(double[] mins, double[] maxs) {
        this.genesSize = new int[numGens];
        this.min = new double[numGens];
        this.max = new double[numGens];

        for (int i = 0; i < this.numGens; i++) {
            this.min[i] = mins[i];
            this.max[i] = maxs[i];
            this.genesSize[i] = this.tamGen(this.errorValue, this.min[i], this.max[i]);
        }
        
        int totalSize = 0;
        
        for (int size : this.genesSize) totalSize += size;

        this.chromosome = new Boolean[totalSize];

        for(int i = 0; i < totalSize; i++) this.chromosome[i] = this.rand.nextBoolean();
    }

    public int tamGen(double precision, double min, double max) {
        return (int) (Math.log10(((max - min) / precision) + 1) / Math.log10(2));
    }

    @Override
    public double getPhenotype(int n) {
        Boolean[] v = new Boolean[this.genesSize[n]];
        if (n == 0) {
            for(int i = 0; i < this.genesSize[n]; i++)
                v[i] = this.chromosome[i];
        }
        else {
            for(int i = 0; i < this.genesSize[n]; i++)
                v[i] = this.chromosome[this.genesSize[n-1]+i];
        }
        return this.min[n] + this.bin2dec(v) * ((this.max[n] - this.min[n]) / (Math.pow(2, this.genesSize[n])-1));
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
        double[] phenotypes = new double[this.numGens];
        for (int i = 0; i < this.numGens; i++) {
            phenotypes[i] = this.getPhenotype(i);
        }

        return phenotypes;
    }

    @Override
    public void mutate(double p) {
        for(int i = 0; i < this.totalSize;i++){
            if(this.rand.nextDouble()<p){
                this.chromosome[i] = !this.chromosome[i];
            }
        }
    }
}
