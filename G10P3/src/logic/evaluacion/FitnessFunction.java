package logic.evaluacion;

import model.Mapa;
import model.Tree;
import utils.Pair;

public interface FitnessFunction {
    double calculateFitness(Tree chromosome);
    Pair<Double,Double> getLimits();
}
