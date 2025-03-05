package logic.mutacion;

import model.Individuo;
import utils.NodoIndividuo;

import java.util.PriorityQueue;

public class Mutacion {

    private double prob;

    public Mutacion(double p) {
        this.prob = p;
    }

    public void mut_population(Individuo[] population, PriorityQueue<NodoIndividuo> eliteQ) {
        int population_size = population.length;
        for (int i = 0; i < population_size ; i++) {
            if(!isElite(i,eliteQ,population)) {
                population[i].mutate(prob);
            }
        }
    }

    private boolean isElite(int index,PriorityQueue<NodoIndividuo> eliteQ,Individuo[] population) {
        for (NodoIndividuo nodo : eliteQ) {
            if (nodo.getIndividuo() == population[index]) {
                return true;
            }
        }
        return false;
    }
}
