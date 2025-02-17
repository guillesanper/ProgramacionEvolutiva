package utils;

import model.Individuo;

public class NodoIndividuo {

    private double value;

    private Individuo individuo;

    public NodoIndividuo(double value, Individuo individuo) {
        this.value = value;
        this.individuo = individuo;
    }

    public double getValue() {

        return value;
    }

    public Individuo getIndividuo() {
        return individuo;
    }


}
