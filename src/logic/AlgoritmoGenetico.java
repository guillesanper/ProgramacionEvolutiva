package logic;

import logic.cruce.Cruce;
import logic.seleccion.Seleccion;
import logic.seleccion.SeleccionFactory;
import model.Individuo;
import model.Valores;
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
    private boolean min;
    private String selectionType;
    private String ind;
    private double errorValue;
    private Controls controlPanel;
    //private int d;
    //double elit;


    public AlgoritmoGenetico(Controls controlPanel) {
        this.controlPanel = controlPanel;
    }

    public void ejecuta(Valores valores) {
        this.init(valores);
    }

    private void initialize_population(int func_index, double errorValue){
        this.population = new Individuo[populationSize];
        //this.best = ();
    }

    private void init(Valores valores){
        this.selectionType = valores.se;
    }
}
