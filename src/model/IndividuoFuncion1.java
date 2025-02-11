package model;

import java.util.Random;

public class IndividuoFuncion1 extends Individuo<Boolean>{

    private final Random rand ;
    public double[] min;
    public double[] max;
    public double errorValue;
    public double totalSize;

    public IndividuoFuncion1(){
        this.rand = new Random();
        this.genesSize = new int[2];
        this.min = new double[2];
        this.max = new double[2];
        this.min[0] = -3.000;
        this.min[1] = 4.100;
        this.max[0] = 12.100;
        this.max[1] = 5.800;
        this.genesSize[0] = this.tamGen(this.errorValue, min[0], max[0]);
        this.genesSize[1] = this.tamGen(this.errorValue, min[1], max[1]);
        int totalSize = genesSize[0] + genesSize[1];
        this.chromosome = new Boolean[totalSize];
        for(int i = 0; i < totalSize; i++) this.chromosome[i] = this.rand.nextBoolean();
    }

    public int tamGen(double precision, double min, double max) {
        return (int) (Math.log10(((max - min) / precision) + 1) / Math.log10(2));
    }

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
        double[] phenotypes = new double[2];
        phenotypes[0] = this.getPhenotype(0);
        phenotypes[1] = this.getPhenotype(1);
        return phenotypes;
    }

    @Override
    public double getFitness() {
        return 0;
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
