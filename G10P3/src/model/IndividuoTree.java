package model;

public class IndividuoTree {

    public Tree tree;
    public double fitness;
    public double fitnessNormalized;
    public double fitnessAccumulated;
    public double probability;

    public IndividuoTree(Tree tree) {
        this.tree = tree;
        this.fitness = 0;
        this.fitnessNormalized = 0;
        this.fitnessAccumulated = 0;
        this.probability = 0;
    }

    public IndividuoTree(Tree tree, double fitness) {
        this.tree = tree;
        this.fitness = fitness;
        this.fitnessNormalized = 0;
        this.fitnessAccumulated = 0;
        this.probability = 0;
    }

}
