package model;

public class IndividuoTree extends Individuo<Tree> {


    public IndividuoTree(int maxDepth, int rows, int columns, int initialization_method_index) {
        this.chromosome = new Tree(maxDepth, rows, columns);

        // Inicializar el árbol según el metodo especificado
        switch (initialization_method_index) {
            case 0:
                chromosome.createFullTree(maxDepth);
                break;
            case 1:
                chromosome.createRampedHalfAndHalfTree(maxDepth);
                break;
            default:
                chromosome.createGrowTree(maxDepth);
        }

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
