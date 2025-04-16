package utils;

public class Valores {
    public int populationSize;
    public int generations;
    public String selectionType;
    public String cross_type;
    public double prob_cruce;
    public int mutationType;
    public double prob_mut;
    public double precision;
    public int funcion_idx;
    public int elitismo;
    public int dimension;
    public String scaling;
    public int min_depth;  // Nuevo campo para la profundidad mínima
    public boolean invMejorado; // Nueva propiedad para INV mejorado
    public boolean bloating_controller;


    public Valores(int populationSize, int generations, String selectionType,
                   String cross_type, double prob_cruce, int mut_idx, double prob_mut,
                   double precision, int funcion_idx, int elitismo, int dimension, String scaling) {

        this(populationSize, generations, selectionType, cross_type, prob_cruce, mut_idx,
                prob_mut, precision, funcion_idx, elitismo, dimension, scaling, false);
    }

    // Constructor sobrecargado que incluye el parámetro invMejorado
    public Valores(int populationSize, int generations, String selectionType,
                   String cross_type, double prob_cruce, int mut_idx, double prob_mut,
                   double precision, int funcion_idx, int elitismo, int dimension, String scaling,
                   boolean invMejorado) {

        this.populationSize = populationSize;
        this.generations = generations;
        this.selectionType = selectionType;
        this.cross_type = cross_type;
        this.prob_cruce = prob_cruce;
        this.mutationType = mutationType;
        this.prob_mut = prob_mut;
        this.precision = precision;
        this.funcion_idx = funcion_idx;
        this.elitismo = elitismo;
        this.dimension = dimension;
        this.scaling = scaling;
        this.invMejorado = invMejorado;
    }

    // Constructor sobrecargado que incluye el parámetro invMejorado
    public Valores(int populationSize, int generations, String selectionType,
                   String cross_type, double prob_cruce, int mut_idx, double prob_mut,
                   double precision, int funcion_idx, int elitismo, int dimension, String scaling,
                   boolean invMejorado, boolean bloating_controller) {

        this.populationSize = populationSize;
        this.generations = generations;
        this.selectionType = selectionType;
        this.cross_type = cross_type;
        this.prob_cruce = prob_cruce;
        this.mut_idx = mut_idx;
        this.prob_mut = prob_mut;
        this.precision = precision;
        this.funcion_idx = funcion_idx;
        this.elitismo = elitismo;
        this.dimension = dimension;
        this.scaling = scaling;
        this.invMejorado = invMejorado;
        this.bloating_controller = bloating_controller;
    }
}