package logic.evaluacion;

import model.Mapa;

public interface FitnessFunction {
    double calculateFitness(Integer[] chromosome);
}
