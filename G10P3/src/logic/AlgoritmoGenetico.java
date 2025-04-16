package logic;

import logic.cruce.Cruce;
import logic.cruce.CruceFactory;
import logic.escalado.Escalado;
import logic.escalado.EscaladoFactory;
import logic.inicializacion.InitializationFactory;
import logic.inicializacion.InitializationMethod;
import logic.evaluacion.FitnessFunction;
import logic.evaluacion.FitnessFunctionFactory;
import logic.mutacion.Mutacion;
import logic.mutacion.MutacionFactory;
import logic.seleccion.Seleccion;
import logic.seleccion.SeleccionFactory;
import logic.seleccion.Seleccionable;
import model.*;
import utils.Transfer;
import utils.Valores;
import utils.NodoIndividuo;
import utils.Pair;
import view.Controls;

import java.util.*;

public class AlgoritmoGenetico<T> {

    private Individuo<T>[] population;
    private double totalFitness;
    private int populationSize;
    private FitnessFunction ff;

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
    private InitializationMethod initializationMethod;
    private int funcIndex;
    private boolean bloating_controller;

    private Random rand;

    private double errorValue;
    private int min_depth;

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

    // Variables para datos relevantes
    private int crossed;
    private int mutated;
    private double worstFitness;

    private Mapa map;

    private final Controls controlPanel;


    public AlgoritmoGenetico(Controls controlPanel) {
        this.controlPanel = controlPanel;
        this.map = new Mapa();
        this.rand = new Random();
    }


    public void ejecuta(Valores valores) {
        int[] selec;

        setValues(valores);
        if (!comprueba_valores())
            return;

        this.mutacion = MutacionFactory.getMutation(0);

        initialize_population(funcIndex, errorValue);

        Comparator<NodoIndividuo> comparator = Comparator.comparingDouble(NodoIndividuo::getValue);
        elitQ = isMin() ? new PriorityQueue<>(Collections.reverseOrder(comparator)) : new PriorityQueue<>(comparator);

        this.selection = SeleccionFactory.getMetodoSeleccion(selectionType);
        this.seleccionables = new Seleccionable[this.populationSize];

        this.cross = (Cruce<T>) CruceFactory.getCruceType(crossType);

        // Almacena el progreso de las generaciones
        generationProgress = new double[3][generations + 1];
        currentGeneration = 0;
        crossed= 0;
        mutated = 0;
        worstFitness = isMin() ? Double.MIN_VALUE : Double.MAX_VALUE;


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

        Transfer t = new Transfer(generationProgress, graphIntervals,best, true,crossed,mutated, worstFitness);
        controlPanel.update_graph(t);
    }

    private void mutate_population(){
        for (Individuo i : population) {
            if (rand.nextDouble() < this.probMutacion) {
                mutacion.mutate(((IndividuoTree) i).getTree());
                mutated++;
            }
        }
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
            crossed +=2;
        }
    }

    private void reproduce(int pos1, int pos2) {
        this.cross.cross(population[pos1].chromosome, population[pos2].chromosome);
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
            copy[i] = (Individuo<T>) IndividuoFactory.createIndividuo(funcIndex);

            // Copiar los valores del cromosoma sin compartir la referencia
            copy[i].chromosome = population[selec[i]].chromosome;

            // Copiar el fitness también para coherencia
            copy[i].fitness = population[selec[i]].getFitness();
        }
        return copy;
    }


    // File: src/logic/AlgoritmoGenetico.java
// Within the evaluate_population() method, you can use parallel streams:
    private void evaluate_population() {
        totalFitness = 0;
        double bestGen = isMin() ? Double.MAX_VALUE : Double.MIN_VALUE;
        Individuo<T> bestGenInd = population[0];

        double avgSize = (double) Arrays.stream(population).reduce(0, (acc, ind) -> {
            int nodes = ((IndividuoTree) ind).getTree().getNumberOfNodes();
            return acc + nodes;
        }, Integer::sum) / populationSize;

        // Evaluate using parallel streams to speed up fitness computations
        Arrays.stream(population).parallel().forEach(ind -> {
            FitnessFunction ff = FitnessFunctionFactory.getInstance().getFunction(this.funcIndex, this.bloating_controller, avgSize);
            double f = ff.calculateFitness(((IndividuoTree)ind).getTree());
            ((IndividuoTree) ind).setFitness(f);

            double fit = ind.getFitness(); // This will call the fitness function
            ind.fitness = fit;
        });

        // Then, iterate over population for updating totals and elitism
        for (int i = 0; i < populationSize; i++) {
            double fit = population[i].fitness;
            totalFitness += fit;

            if (!compare(fit, worstFitness)) {
                worstFitness = fit;
            }

            if (elitQ.size() < eliteSize)
                elitQ.add(new NodoIndividuo(fit, population[i]));
            else if (eliteSize != 0) {
                compareAndReplaceElite(population[i]);
            }

            if (compare(fit, bestGen)) {
                bestGen = fit;
                bestGenInd = population[i];
            }
        }

        try {
            if (compare(bestGen, totalBest)) {
                best.chromosome = (T)((Tree) bestGenInd.chromosome).clone();
                best.fitness = bestGenInd.fitness;
                totalBest = bestGen;
            }
        }catch (CloneNotSupportedException c){
            System.out.println("Error al clonar el individuo");
        }

        generationProgress[0][currentGeneration] = totalBest;
        generationProgress[1][currentGeneration] = bestGen;
        generationProgress[2][currentGeneration++] = totalFitness / populationSize;
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

    private double corrige(double fitness, double fDesp) {
        if (this.isMin()) {
            double fact = fDesp >= 0 ? 1.05 : 0.95;
            return fact*fDesp - fitness;
        }
        return fitness + Math.abs(fDesp);
    }

    private int[] select() {
        this.selection = SeleccionFactory.getMetodoSeleccion(selectionType);

        // Saber si hay que desplazar y calcular fitness maximo
        double fdesp = this.population[0].getFitness();
        boolean desp = false;
        for (Individuo i : this.population) {
            if (isMin() || i.getFitness() < 0) desp = true;

            double f = i.getFitness();

            if (!compare(f, fdesp)) fdesp = i.getFitness(); // buscar maximo/minimo
        }

        // Desplazar fitness y calcular el fitness total desplazado
        double fitness;
        this.totalFitness = 0;


        for (int i = 0; i < this.populationSize; i++) {
            fitness = this.population[i].getFitness();
            if (desp) fitness = corrige(fitness, fdesp);

            this.seleccionables[i] = new Seleccionable(i, fitness);
            this.totalFitness += fitness;
        }
        Arrays.sort(this.seleccionables, Collections.reverseOrder());

        // Crear tabla
        double accProb = 0;
        double prob;

        for (int i = 0; i < this.populationSize; i++) {
            prob = this.seleccionables[i].getFitness() / this.totalFitness;
            accProb += prob;

            this.seleccionables[i].setProb(prob);
            this.seleccionables[i].setAccProb(accProb);
            Seleccionable s = seleccionables[i];
            //System.out.println("S"+i + ": (" + s.getFitness() + ", " + s.getProb() + ", " + s.getAccProb() + ")");
        }
        //System.out.println("\n-------------------\n");

        // Seleccionar
        return selection.getSeleccion(this.seleccionables, this.populationSize);
    }


    private void initialize_population(int func_index, double errorValue) {
        this.population = new Individuo[populationSize];
        for (int i = 0; i < this.populationSize; i++) {
            this.population[i] = (Individuo<T>) IndividuoFactory.createIndividuo(func_index);
        }
        this.best = this.population[0];
        this.totalBest = isMin() ? Double.MAX_VALUE : Double.MIN_VALUE;
        this.graphIntervals = IndividuoFactory.getInterval();
        this.initializationMethod = InitializationFactory.getInstance().getInitializationMethod(func_index);
        this.initializationMethod.initialize_population((Individuo<Tree>[]) population);
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
        this.bloating_controller = valores.bloating_controller;
        this.min_depth = valores.min_depth;
    }

    private boolean comprueba_valores() {
        if (this.elitismo < 0) return false;
        if (this.generations < 0) return false;
        if (this.populationSize < 0) return false;

        return true;
    }

    private boolean isMin() {
        return true;
    }

    private boolean isFunc5() {
        return this.funcIndex == 4;
    }
}
