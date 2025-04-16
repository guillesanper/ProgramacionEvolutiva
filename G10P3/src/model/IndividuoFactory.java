package model;

import logic.evaluacion.FitnessFunction;
import logic.evaluacion.FitnessFunctionFactory;
import utils.Pair;

public class IndividuoFactory {


    public static Individuo<?> createIndividuo(int min_depth) {
        FitnessFunction fn = FitnessFunctionFactory.getInstance().getFunction(min_depth);
        return new IndividuoTree(min_depth,Mapa.getRows(), Mapa.getCols());
    }

    public static Pair<Double, Double> getInterval() {
        return FitnessFunctionFactory.getInstance().getInterval();
    }
}
