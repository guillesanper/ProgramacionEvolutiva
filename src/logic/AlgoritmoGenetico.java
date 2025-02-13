package logic;

import logic.cruce.Cruce;
import logic.seleccion.Seleccion;
import logic.seleccion.SeleccionFactory;
import model.Individuo;
import model.Valores;
import model.factoria.IndividuoFactory;
import view.Controls;

public class AlgoritmoGenetico<T> {
    private int populationSize;
    private Individuo<T>[] population, elite;
    private double[] fitness;
    private double probCruce;
    private double probMutacion;
    private double maxMin;
    private Individuo<T> best;
    private int best_pos;
    private Seleccion selection;
    private Cruce<T> cross;
    private double aptitudMedia;
    private String selectionType;
    private int funcIndex;
    private double errorValue;
    private Controls controlPanel;
    private int d;
    double elit;


    public AlgoritmoGenetico(Controls controlPanel) {
        this.controlPanel = controlPanel;
    }

    public void ejecuta(Valores valores) {
        this.setValues(valores);

    }

    private void initialize_population(int func_index, double errorValue){
        this.population = new Individuo[populationSize];
        this.best = (Individuo<T>) IndividuoFactory.createIndividuo(func_index,errorValue);
        for(int i = 0; i < this.populationSize; i++) {
            this.population[i] = (Individuo<T>) IndividuoFactory.createIndividuo(func_index, this.errorValue);
        }
    }

    private void setValues(Valores valores) {
        this.selectionType = valores.selectionType;
        this.populationSize = valores.populationSize;
        this.probCruce = valores.prob_cruce;
        this.probMutacion = valores.prob_mut;
        this.funcIndex = valores.funcIndex;
        this.errorValue = valores.error;
        this.d = valores.num_genes;
        this.elit = valores.elitismo;


    }

    private boolean isMin(){
        return this.funcIndex == 0;
    }
}
