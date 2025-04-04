package model;

import logic.evaluacion.FitnessFunctionFactory;
import model.symbol.Expression;

public class IndividuoTree extends Individuo<Expression> {

    public Tree tree;
    public double fitness;

    public IndividuoTree(int maxDepth, int rows, int columns, int funcindex) {
        tree = new Tree(maxDepth, rows, columns);

        // Inicializar el árbol según el metodo especificado
        switch (funcindex) {
            case 0:
                tree.createFullTree(maxDepth);
                break;
            case 1:
                tree.createRampedHalfAndHalfTree(maxDepth);
                break;
            default:
                tree.createGrowTree(maxDepth);
        }

        fitness = 0; // Inicialmente no hay fitness calculado
    }

    // Constructor de copia
    public IndividuoTree(IndividuoTree other) {
        this.tree = other.tree.copy();
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
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }


}
