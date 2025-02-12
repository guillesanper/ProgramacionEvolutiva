
package model;

public class Valores {
    public int populationSize;
    public int generations;
    public String selectionType;
    public int cruce_idx;
    public double prob_cruce;
    public int mut_idx;
    public double prob_mut;
    public double precision;
    public int funcion_idx;
    public int num_genes;
    public int elitismo;


    public Valores(int populationSize, int generations, String selectionType,
                   int cruce_idx, double prob_cruce, int mut_idx, double prob_mut,
                   double precision, int funcion_idx, int num_genes, int elitismo) {

        this.populationSize = populationSize;
        this.generations = generations;
        this.selectionType = selectionType;
        this.cruce_idx = cruce_idx;
        this.prob_cruce = prob_cruce;
        this.mut_idx = mut_idx;
        this.prob_mut = prob_mut;
        this.precision = precision;
        this.funcion_idx = funcion_idx;
        this.num_genes = num_genes;
        this.elitismo = elitismo;
    }
}
