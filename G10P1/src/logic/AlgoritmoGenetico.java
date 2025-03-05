package logic;

import logic.cruce.Cruce;
import logic.cruce.CruceFactory;
import logic.escalado.Escalado;
import logic.mutacion.Mutacion;
import logic.seleccion.Seleccion;
import logic.seleccion.SeleccionFactory;
import logic.seleccion.Seleccionable;
import model.Individuo;
import model.Valores;
import model.IndividuoFactory;
import utils.NodoIndividuo;
import utils.Pair;
import view.Controls;
import logic.escalado.EscaladoFactory;

import java.util.*;

public class AlgoritmoGenetico<T> {

    private Individuo<T>[] population;
    private double totalFitness;
    private int populationSize;

    private Individuo<T> best;

    private double probCruce;
    private double probMutacion;
    private int generations;

    private Seleccion selection;
    private Seleccionable[] seleccionables;
    private String selectionType;
    private Cruce<T> cross;
    private String crossType;
    private Mutacion mutacion;
    private int funcIndex;

    private double errorValue;
    private int dimension;

    private int elitismo;
    private int eliteSize;
    private PriorityQueue<NodoIndividuo> elitQ;

    private boolean scalingActivated;
    private Escalado scaling;

    /**
     * Variables para la gráfica
     */
    private int currentGeneration;
    private double[][] generationProgress;
    private double totalBest;
    private Pair<Double, Double> graphIntervals;

    private final Controls controlPanel;


    public AlgoritmoGenetico(Controls controlPanel) {
        this.controlPanel = controlPanel;
    }


    public void ejecuta(Valores valores) {
        int[] selec;

        setValues(valores);
        if (!comprueba_valores())
            return;

        initialize_population(funcIndex, errorValue);

        Comparator<NodoIndividuo> comparator = Comparator.comparingDouble(NodoIndividuo::getValue);
        elitQ = isMin() ? new PriorityQueue<>(Collections.reverseOrder(comparator)) : new PriorityQueue<>(comparator);

        this.selection = SeleccionFactory.getMetodoSeleccion(selectionType);
        this.seleccionables = new Seleccionable[this.populationSize];

        this.cross = (Cruce<T>) CruceFactory.getCruceType(crossType, isFunc5(), dimension);
        this.mutacion = new Mutacion(probMutacion);

        // Almacena el progreso de las generaciones
        generationProgress = new double[3][generations + 1];
        currentGeneration = 0;

        evaluate_population();

        while (generations-- != 0) {
            // Escalado opcional
            if(scalingActivated)
                scaling.escalarFitness(this.population);

            // Seleccion
            selec = this.select();

            // Cruce
            cross_population(selec);

            // Mutación
            mutate_population();

            // Elitismo
            while (elitQ.size() != 0) {
                population[populationSize - elitQ.size()] = elitQ.poll().getIndividuo();
            }

            // Evaluamos poblacion
            evaluate_population();
        }

        controlPanel.update_graph(generationProgress, graphIntervals, best, true);
    }

    private void mutate_population(){
        mutacion.mut_population(population,elitQ);
    }

    private void cross_population(int[] seleccionados) {

        this.population = this.copyPopulation(seleccionados);
        int numCruce = 0;
        int[] to_cross = new int[populationSize];

        for (int i = 0; i < populationSize; i++) {
            if (this.probCruce > Math.random()) {
                to_cross[numCruce++] = i;
            }
        }

        if (numCruce % 2 != 0) {
            numCruce--;
        }

        for (int i = 0; i < numCruce; i += 2) {
            reproduce(to_cross[i],to_cross[i+1]);
        }
    }

    private void reproduce(int pos1, int pos2) {
        T[] c1 = Arrays.copyOf(population[pos1].chromosome, population[pos1].chromosome.length);
        T[] c2 = Arrays.copyOf(population[pos2].chromosome, population[pos2].chromosome.length);
        this.cross.cross(c1, c2);
        population[pos1].chromosome = c1;
        population[pos2].chromosome = c2;
    }



    private boolean isElite(int index) {
        for (NodoIndividuo nodo : elitQ) {
            if (nodo.getIndividuo() == population[index]) {
                return true;
            }
        }
        return false;
    }


    /**
     * Copia la población actual para evitar modificaciones no deseadas
     */
    private Individuo<T>[] copyPopulation(int[] selec) {
        Individuo<T>[] copy = new Individuo[populationSize];
        for (int i = 0; i < populationSize; i++) {
            // Crear un nuevo individuo para evitar referencias compartidas
            copy[i] = (Individuo<T>) IndividuoFactory.createIndividuo(funcIndex, errorValue, dimension);

            // Copiar los valores del cromosoma sin compartir la referencia
            copy[i].chromosome = Arrays.copyOf(population[selec[i]].chromosome, population[selec[i]].chromosome.length);

            // Copiar el fitness también para coherencia
            copy[i].fitness = population[selec[i]].getFitness();
        }
        return copy;
    }


    private void evaluate_population() {
        totalFitness = 0;

        // Initialize best_gen based on minimization/maximization
        double best_gen = isMin() ? Double.MAX_VALUE : Double.MIN_VALUE;
        Individuo<T> bestGenInd = null;

        double fit;
        for (int i = 0; i < populationSize; i++) {
            fit = population[i].getFitness();
            population[i].fitness = fit;
            totalFitness += fit;

            // Add to elite queue
            if (elitQ.size() < eliteSize)
                elitQ.add(new NodoIndividuo(fit, population[i]));
            else if (eliteSize != 0) {
                compareAndReplaceElite(population[i]);
            }

            // Update best individual of generation
            if (isMin() ? (fit < best_gen) : (fit > best_gen)) {
                best_gen = fit;
                // Deep copy the best individual
                bestGenInd = (Individuo<T>) IndividuoFactory.createIndividuo(funcIndex, errorValue, dimension);
                bestGenInd.chromosome = Arrays.copyOf(population[i].chromosome, population[i].chromosome.length);
                bestGenInd.fitness = fit;
            }
        }

        // Update global best
        if (isMin() ? (best_gen < totalBest) : (best_gen > totalBest)) {
            totalBest = best_gen;
            best = bestGenInd;
        }

        // Track generation progress
        generationProgress[0][currentGeneration] = totalBest;   // Best total
        generationProgress[1][currentGeneration] = best_gen;    // Best of generation
        generationProgress[2][currentGeneration++] = totalFitness / populationSize; // Average
    }

    /**
     * Compara el individuo evaluado con el peor de los mejores y si es mejor lo sustituye
     */
    private void compareAndReplaceElite(Individuo<T> newInd) {
        // Obtener el peor de la cola (depende del criterio de comparación)
        NodoIndividuo worstElite = elitQ.peek();

        if (worstElite != null && compare(newInd.getFitness(), worstElite.getValue())) {
            elitQ.poll(); // Eliminar el peor
            elitQ.add(new NodoIndividuo(newInd.getFitness(), newInd)); // Insertar el nuevo
        }
    }

    /**
     * Compara dos valores de fitness según el tipo de optimización (minimización o maximización).
     * <p>
     * true si f1 es mejor que f2, según la función.
     */
    private boolean compare(double f1, double f2) {
        return isMin() ? (f1 < f2) : (f1 > f2);
    }


    private int[] select() {
        // Inicializar variables para el desplazamiento
        double minFitness = Double.MAX_VALUE;
        double maxFitness = Double.MIN_VALUE;

        // Encontrar el mínimo y máximo fitness
        for (Individuo<T> individuo : this.population) {
            double fitness = individuo.getFitness();
            minFitness = Math.min(minFitness, fitness);
            maxFitness = Math.max(maxFitness, fitness);
        }

        // Determinar si es necesario desplazar
        boolean needsScaling = isMin() || minFitness < 0;

        // Reiniciar total de fitness
        this.totalFitness = 0;

        // Procesar cada individuo
        for (int i = 0; i < this.populationSize; i++) {
            double fitness = this.population[i].getFitness();

            // Aplicar desplazamiento si es necesario
            if (needsScaling) {
                fitness = scaleFitness(fitness, minFitness, maxFitness);
            }

            // Crear seleccionable y acumular fitness total
            this.seleccionables[i] = new Seleccionable(i, fitness);
            this.totalFitness += fitness;
        }

        // Ordenar seleccionables por fitness de forma descendente
        Arrays.sort(this.seleccionables, Collections.reverseOrder());

        // Calcular probabilidades acumuladas
        double accProb = 0;
        for (int i = 0; i < this.populationSize; i++) {
            double prob = this.seleccionables[i].getFitness() / this.totalFitness;
            accProb += prob;

            this.seleccionables[i].setProb(prob);
            this.seleccionables[i].setAccProb(accProb);
        }

        // Realizar selección
        return selection.getSeleccion(this.seleccionables, this.populationSize);
    }

    private double scaleFitness(double fitness, double minFitness, double maxFitness) {
        // Para problemas de minimización
        if (isMin()) {
            // Convertir a problema de maximización
            // Invertir la escala de fitness
            return maxFitness - fitness + minFitness;
        }

        // Para problemas de maximización con fitness negativos
        if (fitness < 0) {
            // Desplazar por encima de cero
            return fitness + Math.abs(minFitness) + 1;
        }

        // Si no se necesita escalado, devolver fitness original
        return fitness;
    }


    private void initialize_population(int func_index, double errorValue) {
        this.population = new Individuo[populationSize];
        for (int i = 0; i < this.populationSize; i++) {
            this.population[i] = (Individuo<T>) IndividuoFactory.createIndividuo(func_index, this.errorValue, dimension);
        }
        this.best = this.population[0];
        this.totalBest = isMin() ? Double.MAX_VALUE : Double.MIN_VALUE;
        this.graphIntervals = IndividuoFactory.getInterval(funcIndex);
    }

    private void setValues(Valores valores) {
        this.selectionType = valores.selectionType;
        this.populationSize = valores.populationSize;
        this.crossType = valores.cross_type;
        this.probCruce = valores.prob_cruce;
        this.probMutacion = valores.prob_mut;
        this.funcIndex = valores.funcion_idx;
        this.errorValue = valores.precision;
        this.dimension = valores.dimension;
        this.elitismo = valores.elitismo;
        this.generations = valores.generations;
        this.eliteSize = (int) (populationSize * (elitismo / 100.0));
        this.scalingActivated = valores.scaling != "Ninguno";
        if(scalingActivated)
            this.scaling = EscaladoFactory.getEscalado(valores.scaling);
    }

    private boolean comprueba_valores() {
        if (this.elitismo < 0) return false;
        if (this.generations < 0) return false;
        if (this.populationSize < 0) return false;

        return true;
    }

    private boolean isMin() {
        return this.funcIndex != 0;
    }

    private boolean isFunc5() {
        return this.funcIndex == 4;
    }
}
