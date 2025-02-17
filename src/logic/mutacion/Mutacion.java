package logic.mutacion;

import model.Individuo;
import model.IndividuoBoolean;

public class Mutacion {

    private double prob;
    private int elite_size;

    public Mutacion(double p, int elite_size) {
        this.prob = p;
        this.elite_size = elite_size;
    }

    public Individuo[] mut_Population(Individuo[] population) {
        int population_size = population.length;
        for (int i = 0; i < population_size - elite_size; i++) {
            population[i].mutate(prob);
        }
        return population;
    }


}
