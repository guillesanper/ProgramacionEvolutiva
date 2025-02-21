package logic.seleccion;

public class Seleccionable {
    public int ind;
    public double fitness;
    public double prob;
    public double accProb;

    public Seleccionable(int ind, double fitness, double prob, double accProb) {
        this.ind = ind;
        this.fitness = fitness;
        this.prob = prob;
        this.accProb = accProb;
    }

    public int getInd() {
        return ind;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public double getAccProb() {
        return accProb;
    }

    public void setAccProb(double accProb) {
        this.accProb = accProb;
    }
}
