package model;

import logic.evaluacion.FitnessFunctionFactory;
import model.symbol.Expression;

public class IndividuoTree extends Individuo<Tree> {


    public IndividuoTree(int maxDepth, int rows, int columns) {
        this.chromosome = new Tree(maxDepth, rows, columns);

        fitness = 0; // Inicialmente no hay fitness calculado
    }

    // Constructor de copia
    public IndividuoTree(IndividuoTree other) {
        this.chromosome = other.chromosome.copy();
        this.fitness = other.fitness;
    }

    @Override
    public double getPhenotype(int n) {
        return 0;
    }

    public double getFitness() {

        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Tree getTree() {
        return chromosome;
    }

    public void setTree(Tree tree) {
        this.chromosome = tree;
    }


}
