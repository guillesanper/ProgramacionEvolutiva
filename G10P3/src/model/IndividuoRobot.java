package model;

import logic.evaluacion.FitnessFunction;

import java.util.Arrays;
import java.util.Random;

public class IndividuoRobot extends Individuo<Integer> implements Comparable<Individuo>  {

    private static final int ROOMS = 20;

    FitnessFunction fitnessFunction;

    public IndividuoRobot(FitnessFunction fn) {
        fitnessFunction = fn;
        this.chromosome = new Integer[ROOMS];
        // Inicializar el cromosoma con la permutación de 1 a 20
        for (int i = 0; i < 20; i++) {
            this.chromosome[i] = i + 1;
        }
        shuffle();
    }

    // Metodo para mezclar el cromosoma
    private void shuffle() {
        Random rnd = new Random();
        for (int i = chromosome.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int temp = chromosome[index];
            chromosome[index] = chromosome[i];
            chromosome[i] = temp;
        }
    }

    // Metodo para calcular el fitness (distancia total)
    public double calcularFitness() {
        return this.fitnessFunction.calculateFitness(this.chromosome);
    }

    @Override
    public double getPhenotype(int n) {
        return chromosome[n];
    }

    public double getFitness() {
        return calcularFitness();
    }

    @Override
    public int compareTo(Individuo other) {
        return Double.compare(this.fitness, other.fitness);
    }

    @Override
    public String toString() {
        return "Fitness: " + fitness + "\nRuta: " + Arrays.toString(chromosome);
    }
}
