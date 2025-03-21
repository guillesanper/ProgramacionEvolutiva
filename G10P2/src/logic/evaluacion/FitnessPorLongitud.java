package logic.evaluacion;

import model.Mapa;
import utils.Pair;

public class FitnessPorLongitud implements FitnessFunction {

    private Mapa mapa;

    public FitnessPorLongitud(Mapa map) {
        this.mapa = map;
    }

    @Override
    public double calculateFitness(Integer[] chromosome) {
        double total = 0.0;
        double segmentDistance;

        segmentDistance = mapa.calcularRuta(mapa.getBase(), mapa.getHabitacion(chromosome[0]));
        if (segmentDistance == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
        total += segmentDistance;

        for (int i = 0; i < chromosome.length - 1; i++) {
            segmentDistance = mapa.calcularRuta(mapa.getHabitacion(chromosome[i]), mapa.getHabitacion(chromosome[i + 1]));
            if (segmentDistance == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
            total += segmentDistance;
        }

        segmentDistance = mapa.calcularRuta(mapa.getHabitacion(chromosome[chromosome.length - 1]), mapa.getBase());
        if (segmentDistance == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
        total += segmentDistance;

        return total;
    }

    @Override
    public Pair<Double, Double> getLimits() {
        return new Pair<>(50.00,300.00);
    }
}
