package logic.inicializacion;

import model.Individuo;
import model.Tree;

public class RampedAndHalfInitialization implements InitializationMethod {
    // Inicialización de los métodos de inicialización
    private InitializationMethod growthInit;
    private InitializationMethod completeInit;

    public RampedAndHalfInitialization(){
        // Inicializar los métodos de inicialización
        this.growthInit = InitializationFactory.getInstance().getInitializationMethod(0);
        this.completeInit = InitializationFactory.getInstance().getInitializationMethod(1);
    }

    @Override
    public void initialize_population(Individuo<Tree>[] population) {

        // La profundidad máxima D es obtenida del árbol
        int maxDepth = population[0].chromosome.getMaxDepth();

        // Número de grupos = D - 1
        int groups = maxDepth - 1;

        // Tamaño de cada grupo
        int groupSize = population.length / groups;

        // Elementos sobrantes
        int remainder = population.length % groups;

        int index = 0;

        // Para cada profundidad de 2 a D
        for (int depth = 2; depth <= maxDepth; depth++) {
            int currentGroupSize = groupSize;

            // Distribuir los elementos sobrantes
            if (remainder > 0) {
                currentGroupSize++;
                remainder--;
            }

            // La mitad del grupo usa el método creciente (Growth)
            int halfSize = currentGroupSize / 2;
            for (int i = 0; i < halfSize; i++) {
                if (index < population.length) {
                    growthInit.initialize_individual(population[index], depth);
                    index++;
                }
            }

            // La otra mitad usa el metodo completo (Complete)
            for (int i = 0; i < currentGroupSize - halfSize; i++) {
                if (index < population.length) {
                    completeInit.initialize_individual(population[index], depth);
                    index++;
                }
            }
        }
    }

    @Override
    public void initialize_individual(Individuo<Tree> individual, int depth) {
        // Para un solo individuo, elegimos aleatoriamente entre Growth y Complete
        boolean useGrowth = Math.random() < 0.5;

        if (useGrowth) {
            growthInit.initialize_individual(individual, depth);
        } else {
            completeInit.initialize_individual(individual, depth);
        }
    }
}