package logic.evaluacion;

import model.Tree;
import utils.Pair;

public class FitnessHormiga implements FitnessFunction{
    @Override
    public double calculateFitness(Tree chromosome) {
        return 0;
    }

    @Override
    public Pair<Double, Double> getLimits() {
        return null;
    }
}
