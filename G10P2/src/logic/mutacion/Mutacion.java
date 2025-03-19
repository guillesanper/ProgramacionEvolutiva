package logic.mutacion;

import model.Individuo;
import utils.NodoIndividuo;

import java.util.PriorityQueue;
import java.util.Random;

public class Mutacion {

    private double prob;
    private Mutate mutate_method;

    public Mutacion(double p, int mut_index) {
        this.prob = p;
        this.mutate_method = MutacionFactory.getMutation(mut_index);
    }

    public Integer mut_population(Individuo[] population, PriorityQueue<NodoIndividuo> eliteQ) {
        int population_size = population.length;
        int mutated = 0;
        Random rand = new Random();
        for (int i = 0; i < population_size; i++) {
            if (!isElite(i, eliteQ, population)) {
                if (rand.nextDouble() < prob) {
                    mutate_method.mutate((Integer[]) population[i].chromosome);
                    mutated++;
                }
            }
        }
        return mutated;
    }

    private boolean isElite(int index, PriorityQueue<NodoIndividuo> eliteQ, Individuo[] population) {
        for (NodoIndividuo nodo : eliteQ) {
            if (nodo.getIndividuo() == population[index]) {
                return true;
            }
        }
        return false;
    }
}
