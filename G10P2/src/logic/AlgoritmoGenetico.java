package logic;

import logic.cruce.Cruce;
import logic.cruce.CruceFactory;
import logic.escalado.Escalado;
import logic.escalado.EscaladoFactory;
import logic.mutacion.Mutacion;
import logic.seleccion.Seleccion;
import logic.seleccion.SeleccionFactory;
import logic.seleccion.Seleccionable;
import model.Individuo;
import model.IndividuoFactory;
import model.Mapa;
import utils.Transfer;
import utils.Valores;
import utils.NodoIndividuo;
import utils.Pair;
import view.Controls;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

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

    // Variables para datos relevantes
    private int crossed;
    private int mutated;

    private Mapa map;

    private final Controls controlPanel;


    public AlgoritmoGenetico(Controls controlPanel) {
        this.controlPanel = controlPanel;
        this.map = new Mapa();
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
        this.mutacion = new Mutacion(probMutacion, valores.mut_idx);

        // Almacena el progreso de las generaciones
        generationProgress = new double[3][generations + 1];
        currentGeneration = 0;
        crossed= 0;
        mutated = 0;

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

        Transfer t = new Transfer(generationProgress, graphIntervals,best, true,crossed,mutated);
        controlPanel.update_graph(t);
    }

    private void mutate_population(){
        mutacion.mut_population(population,elitQ);
    }

    private void cross_population(int[] selec) {
        // Crear copia de la población antes del cruce
        Individuo<T>[] selection = copyPopulation(selec);
        // De los seleccionados, los indices de los que se reproduciran
        int[] chosen_for_cross = new int[populationSize];
        int chosen_size = 0;

        for (int i = 0; i < populationSize; i++) {
            if (Math.random() < probCruce) {
                chosen_for_cross[chosen_size++] = selec[i];
            }
        }

        if (chosen_size % 2 == 1) {
            chosen_size--;
        }

        // Aplicar cruce en la copia para evitar sobrescribir individuos seleccionados múltiples veces
        for (int i = 0; i < chosen_size - 1; i += 2) {
            if (!isElite(chosen_for_cross[i]) && !isElite(chosen_for_cross[i + 1])) {
                reproduce(chosen_for_cross[i], chosen_for_cross[i + 1], selection);
            }
        }

        for (int i = 0; i < chosen_size; i++) {
            if (!isElite(chosen_for_cross[i])) {
                this.population[chosen_for_cross[i]].chromosome = Arrays.copyOf(selection[chosen_for_cross[i]].chromosome, selection[chosen_for_cross[i]].chromosome.length);
                this.population[chosen_for_cross[i]].fitness = this.population[chosen_for_cross[i]].getFitness();
                crossed++;
            }
        }

    }

    private void reproduce(int pos1, int pos2, Individuo<T>[] populationCopy) {
        T[] c1 = Arrays.copyOf(population[pos1].chromosome, population[pos1].chromosome.length);
        T[] c2 = Arrays.copyOf(population[pos2].chromosome, population[pos2].chromosome.length);
        this.cross.cross(c1, c2);
        populationCopy[pos1].chromosome = c1;
        populationCopy[pos2].chromosome = c2;
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
            copy[i] = (Individuo<T>) IndividuoFactory.createIndividuo(funcIndex, errorValue, dimension,map);

            // Copiar los valores del cromosoma sin compartir la referencia
            copy[i].chromosome = Arrays.copyOf(population[selec[i]].chromosome, population[selec[i]].chromosome.length);

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

        // Evaluate using parallel streams to speed up fitness computations
        Arrays.stream(population).parallel().forEach(ind -> {
            double fit = ind.getFitness(); // This will call the fitness function
            ind.fitness = fit;
        });

        // Then, iterate over population for updating totals and elitism
        for (int i = 0; i < populationSize; i++) {
            double fit = population[i].fitness;
            totalFitness += fit;

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


        if(compare(bestGen,totalBest)){
            best.chromosome = bestGenInd.chromosome.clone();
            best.fitness = bestGenInd.fitness;
            totalBest = bestGen;
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
            this.population[i] = (Individuo<T>) IndividuoFactory.createIndividuo(func_index, this.errorValue, dimension,map);
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
        return true;
    }

    private boolean isFunc5() {
        return this.funcIndex == 4;
    }
}
