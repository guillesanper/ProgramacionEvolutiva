package logic.seleccion;

public class Seleccionable {
    public double fitness;
    public double prob;
    public double accProb;

    public Seleccionable(double fitness) {
        this.fitness = fitness;
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
