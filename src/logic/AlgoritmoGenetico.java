package logic;

import logic.cruce.Cruce;
import logic.cruce.CruceFactory;
import logic.mutacion.Mutacion;
import logic.seleccion.Seleccion;
import logic.seleccion.SeleccionFactory;
import model.Individuo;
import model.Valores;
import model.IndividuoFactory;
import utils.NodoIndividuo;
import utils.Pair;
import view.Controls;

import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class AlgoritmoGenetico<T> {

    private Individuo<T>[] population;
    private double[] fitness;
    private double totalFitness;
    private int populationSize;

    private Individuo<T> best;
    private int best_pos;

    private double probCruce;
    private double probMutacion;
    private int generations;

    private Seleccion selection;
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
    private Pair<Double,Double> graphIntervals;

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


        Comparator<NodoIndividuo> comparator= Comparator.comparingDouble(NodoIndividuo::getValue);
        if(funcIndex!=0)
            elitQ=new PriorityQueue<>(Collections.reverseOrder(comparator));
        else elitQ=new PriorityQueue<>(comparator);

        this.selection = SeleccionFactory.getMetodoSeleccion(selectionType, fitness, populationSize, isMin(), best.getFitness());
        this.cross = (Cruce<T>) CruceFactory.getCruceType(crossType, isFunc5(), dimension);
        this.mutacion = new Mutacion(probMutacion, eliteSize);

        // Almacena el progreso de las generaciones
        generationProgress = new double[3][generations + 1];
        currentGeneration = 0;

        evaluate_population();

        while (generations-- != 0) {
            // Seleccion
            selec = selection.getSeleccion();

            // Cruce
            cross_population(selec);

            // Mutación
            mutacion.mut_population(population);

            // elitism
            while(elitQ.size()!=0) {
                population[populationSize-elitQ.size()]=elitQ.poll().getIndividuo();
            }

            // Evaluamos poblacion
            evaluate_population();
        }

        controlPanel.update_graph(generationProgress,graphIntervals,best,true);
    }



    private void cross_population(int[] selec){
        // **Crear copia de la población antes del cruce**
        Individuo<T>[] populationCopy = copyPopulation();

        // Aplicar cruce en la copia para evitar sobrescribir individuos seleccionados múltiples veces
        for (int i = 0; i < populationSize - 1; i += 2) {
            reproduce(selec[i], selec[i + 1], populationCopy);
        }

        this.population = populationCopy;
    }

    /**
     * Copia la población actual para evitar modificaciones no deseadas
     */
    private Individuo<T>[] copyPopulation() {
        Individuo<T>[] copy = new Individuo[populationSize];
        for (int i = 0; i < populationSize; i++) {
            copy[i] = (Individuo<T>) IndividuoFactory.createIndividuo(funcIndex, errorValue, dimension);
            copy[i].chromosome = population[i].chromosome;
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
        generationProgress[2][currentGeneration++] = totalFitness / populationSize; //Media

        //  TODO Desplazamiento para eliminar fitness negativos aqui o en metodo de seleccion
        double acum = 0;

    }

    /**
     * Compara el individuo evaluado con el peor de los mejores y si es mejor lo sustituye
     */
    private void compareAndReplaceElite( Individuo<T> newInd) {
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

    private void reproduce(int pos1, int pos2, Individuo<T>[] populationCopy) {
        T[] c1 = population[pos1].chromosome;
        T[] c2 = population[pos2].chromosome;
        this.cross.cross(c1, c2);
        populationCopy[pos1].chromosome = c1;
        populationCopy[pos2].chromosome = c2;
    }

    private void initialize_population(int func_index, double errorValue) {
        this.population = new Individuo[populationSize];
        this.fitness = new double[populationSize];
        for (int i = 0; i < this.populationSize; i++) {
            this.population[i] = (Individuo<T>) IndividuoFactory.createIndividuo(func_index, this.errorValue, dimension);
            this.fitness[i] = this.population[i].getFitness();
        }
        this.best = this.population[0];
        this.totalBest = best.getFitness();
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
        if (funcIndex != 4) {
            if (funcIndex == 2) {
                controlPanel.update_error("No hay Cruce\n- Aritmetico\nen individuos Binarios");
                return false;
            }
        }
        return true;
    }

    private boolean isMin() {
        return this.funcIndex != 0;
    }

    private boolean isFunc5() {
        return this.funcIndex == 4;
    }
}
