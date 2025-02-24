package logic;

import logic.cruce.Cruce;
import logic.cruce.CruceFactory;
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

import java.util.*;

public class AlgoritmoGenetico<T> {

    private Individuo<T>[] population;
    private double totalFitness;
    private int populationSize;

    private Individuo<T> best;
    private int best_pos;

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
        if (funcIndex != 0)
            elitQ = new PriorityQueue<>(Collections.reverseOrder(comparator));
        else elitQ = new PriorityQueue<>(comparator);

        this.selection = SeleccionFactory.getMetodoSeleccion(selectionType);
        this.seleccionables = new Seleccionable[this.populationSize];

        this.cross = (Cruce<T>) CruceFactory.getCruceType(crossType, isFunc5(), dimension);
        this.mutacion = new Mutacion(probMutacion, eliteSize);

        // Almacena el progreso de las generaciones
        generationProgress = new double[3][generations + 1];
        currentGeneration = 0;

        evaluate_population();

        while (generations-- != 0) {
            // Seleccion
            selec = this.select();

            // Cruce
            cross_population(selec);

            // Mutación
            mutacion.mut_population(population);

            // Recalcular el mejor después de la mutación
            for (Individuo<T> ind : population) {
                if (compare(ind.getFitness(), totalBest)) {
                    totalBest = ind.getFitness();
                    best = ind;
                }
            }


            // elitism
            while (elitQ.size() != 0) {
                population[populationSize - elitQ.size()] = elitQ.poll().getIndividuo();
            }

            // Evaluamos poblacion
            evaluate_population();
        }

        controlPanel.update_graph(generationProgress, graphIntervals, best, true);
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

        if(chosen_size%2 == 1){
            chosen_size--;
        }

        // Aplicar cruce en la copia para evitar sobrescribir individuos seleccionados múltiples veces
        for (int i = 0; i < chosen_size - 1; i += 2) {
                reproduce(chosen_for_cross[i],chosen_for_cross[i + 1], selection);
        }

        this.population = selection;
    }

    /**
     * Copia la población actual para evitar modificaciones no deseadas
     */
    private Individuo<T>[] copyPopulation(int[] selec) {
        Individuo<T>[] copy = new Individuo[populationSize];
        for (int i = 0; i < populationSize; i++) {
            copy[i] = (Individuo<T>) IndividuoFactory.createIndividuo(funcIndex, errorValue, dimension);
            copy[i].chromosome = Arrays.copyOf(population[selec[i]].chromosome, population[i].chromosome.length);
        }
        return copy;
    }

    private void evaluate_population() {
        totalFitness = 0;

        double best_gen = !isMin() ? Double.MIN_VALUE : Double.MAX_VALUE;
        double worst_gen = population[1].fitness;
        Individuo<T> bestGenInd = population[0];

        double fit;
        for (int i = 0; i < populationSize; i++) {
            fit = population[i].getFitness();
            population[i].fitness = fit;
            totalFitness += fit;

            // Agregar a la cola de elitismo si es necesario
            if (elitQ.size() < eliteSize)
                elitQ.add(new NodoIndividuo(fit, population[i]));
            else if (eliteSize != 0) {
                compareAndReplaceElite(population[i]);
            }

            //Actualizamos mejor o peor de la generacion si es necesario
            if (compare(fit, best_gen)) {
                best_gen = fit;
                bestGenInd = population[i];
            }
            worst_gen = compare(fit, worst_gen) ? worst_gen : fit;

        }

        //Actualizamos el mejor global si es necesario
        if (compare(best_gen, totalBest)) {
            totalBest = best_gen;
            best = bestGenInd;
        }

        // Actualizamos las variables para la grafica
        generationProgress[0][currentGeneration] = totalBest;   // Mejor total
        generationProgress[1][currentGeneration] = best_gen;    // Mejor de la generacion
        generationProgress[2][currentGeneration++] = totalFitness / populationSize; //MediaS
    }

    /*private int[] selElite(int numElit) {
        this.elite = new Individuo[numElit];
        int temp;
        int[]posElit = new int[numElit];
        for(int i = 0; i < numElit; i++) {
            this.elite[i] = (Individuo<T>) IndividuoFactory.getIndivType(this.ind, this.valErr, this.d);
            this.elite[i].setCromosoma(this.poblacion[i].getCromosoma());
            posElit[i] = i;
        }
        for(int i = numElit; i < this.tamPoblacion; i++) {
            temp = 0;
            for(int j = 1; j < numElit; j++) {
                if(!this.min) {
                    if(this.elite[temp].getFitness() < this.elite[j].getFitness())
                        temp = j;
                }
                else {
                    if(this.elite[temp].getFitness() > this.elite[j].getFitness())
                        temp = j;
                }
            }
            if(!this.min){
                if (this.elite[temp].getFitness() < this.poblacion[i].getFitness()) {
                    this.elite[temp].setCromosoma(this.poblacion[i].getCromosoma());
                    posElit[temp] = i;
                }
            }
            else {
                if(this.elite[temp].getFitness() > this.poblacion[i].getFitness()) {
                    this.elite[temp].setCromosoma(this.poblacion[i].getCromosoma());
                    posElit[temp] = i;
                }
            }
        }
        return posElit;
    }*/

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
            prob = this.seleccionables[i].getFitness()/this.totalFitness;
            accProb += prob;

            this.seleccionables[i].setProb(prob);
            this.seleccionables[i].setAccProb(accProb);
        }

        // Seleccionar
        return selection.getSeleccion(this.seleccionables, this.populationSize);
    }

    private void reproduce(int pos1, int pos2, Individuo<T>[] populationCopy) {
        T[] c1 = Arrays.copyOf(population[pos1].chromosome, population[pos1].chromosome.length);
        T[] c2 = Arrays.copyOf(population[pos2].chromosome, population[pos2].chromosome.length);
        this.cross.cross(c1, c2);
        populationCopy[pos1].chromosome = c1;
        populationCopy[pos2].chromosome = c2;
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
    }

    private boolean comprueba_valores() {
        return true;
    }

    private boolean isMin() {
        return this.funcIndex != 0;
    }

    private boolean isFunc5() {
        return this.funcIndex == 4;
    }
}
