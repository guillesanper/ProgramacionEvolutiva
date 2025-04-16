package logic.inicializacion;

import model.Individuo;
import model.Tree;

public class GrowthInitialization implements InitializationMethod {
    @Override
    public void initialize_population(Individuo<Tree>[] population) {
        for (int i = 0; i < population.length; i++) {
            initialize_individual(population[i], population[i].chromosome.getMaxDepth());
        }
    }

    @Override
    public void initialize_individual(Individuo<Tree> individual, int depth) {
        Tree tree = individual.chromosome;
        tree.setRoot(tree.growExpression(0, depth));
    }
}