package logic.inicializacion;

import model.Individuo;
import model.Tree;

public interface InitializationMethod {
    /**
     * Inicializa toda la población
     * @param population Arreglo de individuos que representa la población
     */
    void initialize_population(Individuo<Tree>[] population);

    /**
     * Inicializa un solo individuo con una profundidad específica
     * @param individual Individuo a inicializar
     * @param depth Profundidad máxima a utilizar
     */
    void initialize_individual(Individuo<Tree> individual, int depth);
}