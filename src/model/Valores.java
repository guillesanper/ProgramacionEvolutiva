
package model;

public class Valores {
    public int populationSize;
    public int generations;
    public String selectionType;
    public String cross_type;
    public double prob_cruce;
    public int mut_idx;
    public double prob_mut;
    public double precision;
    public int funcion_idx;
    public int elitismo;
    public int dimension;
    public String scaling;


    public Valores(int populationSize, int generations, String selectionType,
                   String cross_type, double prob_cruce, int mut_idx, double prob_mut,
                   double precision, int funcion_idx, int elitismo, int dimension,String scaling ) {

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
    }
}
