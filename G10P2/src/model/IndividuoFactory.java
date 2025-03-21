package model;

import logic.evaluacion.FitnessFunction;
import logic.evaluacion.FitnessFunctionFactory;
import utils.Pair;

public class IndividuoFactory {

    private static FitnessFunctionFactory fff ;

    public static Individuo<?> createIndividuo(int func_ind, double valError, int d,Mapa map) {
        fff = new FitnessFunctionFactory();
        FitnessFunction fn = fff.getFunction(func_ind);
        return new IndividuoRobot(fn);
    }

    public static Pair<Double, Double> getInterval(int funcIndex) {
        return fff.getInterval();
    }
}
