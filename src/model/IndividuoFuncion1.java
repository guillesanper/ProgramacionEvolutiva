package model;

public class IndividuoFuncion1 extends Individuo<Boolean>{



    public IndividuoFuncion1(){
        this.tamGenes = new int[2];
        this.min = new double[2];
        this.max = new double[2];
        this.min[0] = -3.000;
        this.min[1] = 4.100;
        this.max[0] = 12.100;
        this.max[1] = 5.800;
        this.tamGenes[0] = this.tamGen(this.valorError, min[0], max[0]);
        this.tamGenes[1] = this.tamGen(this.valorError, min[1], max[1]);
        int tamTotal = tamGenes[0] + tamGenes[1];
        this.cromosoma = new Boolean[tamTotal];
        for(int i = 0; i < tamTotal; i++) this.cromosoma[i] = this.rand.nextBoolean();
    }

    @Override
    public double evaluateFitness() {
        return 0;
    }
}
