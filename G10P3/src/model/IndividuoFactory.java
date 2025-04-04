package model;

import logic.evaluacion.FitnessFunction;
import logic.evaluacion.FitnessFunctionFactory;
import utils.Pair;

public class IndividuoFactory {


    public static Individuo<?> createIndividuo(int func_ind) {
        FitnessFunction fn = FitnessFunctionFactory.getInstance().getFunction(func_ind);
        return new IndividuoTree(4,Mapa.getRows(), Mapa.getCols(),func_ind);
    }

    public static Pair<Double, Double> getInterval() {
        return FitnessFunctionFactory.getInstance().getInterval();
    }
}
