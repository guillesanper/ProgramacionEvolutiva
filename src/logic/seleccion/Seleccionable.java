package logic.seleccion;

public interface Seleccionable {
    double getFitness();
    double getProb();
    double getAccProb();

    void setFitness(double fitness);
    void setProb(double prob);
    void setAccProb(double accProb);

}
