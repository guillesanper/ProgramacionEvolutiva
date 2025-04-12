package logic.evaluacion;

import model.Tree;
import utils.Pair;

import java.util.Random;

public class TarpeianBloating implements FitnessFunction {
    private final FitnessFunction decorated;
    private final double averagePopSize;
    private static final int N = 2;

    public TarpeianBloating(FitnessFunction decorated, double averagePopSize) {
        this.decorated = decorated;
        this.averagePopSize = averagePopSize;
    }

    @Override
    public double calculateFitness(Tree chromosome) {
        int randomInt = new Random().nextInt(N)+1;
        // En los apuntes se usa el operador módulo
        // Pero esto da la misma probabilidad
        if (chromosome.getNumberOfNodes() > this.averagePopSize && randomInt == N) {
            return Double.MAX_VALUE; // Número muy alto
        }
        return this.decorated.calculateFitness(chromosome);
    }

    @Override
    public Pair<Double, Double> getLimits() {
        return this.decorated.getLimits();
    }
}
