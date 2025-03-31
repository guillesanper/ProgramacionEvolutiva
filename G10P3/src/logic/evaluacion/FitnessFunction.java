package logic.evaluacion;

import model.Mapa;
import utils.Pair;

public interface FitnessFunction {
    double calculateFitness(Integer[] chromosome);
    Pair<Double,Double> getLimits();
}
