package model;

import logic.evaluacion.FitnessFunction;
import logic.evaluacion.FitnessFunctionFactory;
import utils.Pair;

public class IndividuoFactory {

    public static Individuo<?> createIndividuo(int func_ind, double valError, int d,Mapa map) {
        FitnessFunctionFactory fff = new FitnessFunctionFactory();
        FitnessFunction fn = fff.getFunction(func_ind);
        return new IndividuoRobot(fn,map);
    }

    public static Pair<Double, Double> getInterval(int funcIndex) {
        switch (funcIndex) {
            default:
                return new Pair<>(0.0,300.0);
        }
    }
}
