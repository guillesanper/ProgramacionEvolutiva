package logic.evaluacion;

import logic.evaluacion.FitnessFunction;
import model.Mapa;
import java.awt.Point;
import java.util.List;

public class FitnessPorProximidadObstaculos implements FitnessFunction {

    private Mapa mapa;
    // Constant weight to penalize starting from a room other than the closest.
    private static final double PENALTY_WEIGHT = 10.0;

    public FitnessPorProximidadObstaculos(Mapa map) {
        this.mapa = map;
    }

    @Override
    public double calculateFitness(Integer[] chromosome) {
        double total = 0.0;
        double segmentDistance;
        double proximityPenalty = 0.0;

        // Calculate extra penalty if the first room is not the one closest to the base.
        double bestBaseDistance = Double.POSITIVE_INFINITY;
        int bestRoomId = -1;
        // There are 20 fixed rooms (IDs 1 to 20)
        for (int i = 1; i <= 20; i++) {
            double d = mapa.calcularRuta(mapa.getBase(), mapa.getHabitacion(i));
            if (d < bestBaseDistance) {
                bestBaseDistance = d;
                bestRoomId = i;
            }
        }
        // Insert extra penalty proportional to how much farther the chosen first room is.
        segmentDistance = mapa.calcularRuta(mapa.getBase(), mapa.getHabitacion(chromosome[0]));
        if (segmentDistance == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
        if (chromosome[0] != bestRoomId) {
            double extraPenalty = segmentDistance - bestBaseDistance;
            total += extraPenalty * PENALTY_WEIGHT;
        }
        total += segmentDistance;
        proximityPenalty += calculateProximityPenalty(mapa.getBase(), mapa.getHabitacion(chromosome[0]));

        for (int i = 0; i < chromosome.length - 1; i++) {
            segmentDistance = mapa.calcularRuta(mapa.getHabitacion(chromosome[i]), mapa.getHabitacion(chromosome[i + 1]));
            if (segmentDistance == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
            total += segmentDistance;
            proximityPenalty += calculateProximityPenalty(mapa.getHabitacion(chromosome[i]), mapa.getHabitacion(chromosome[i + 1]));
        }

        segmentDistance = mapa.calcularRuta(mapa.getHabitacion(chromosome[chromosome.length - 1]), mapa.getBase());
        if (segmentDistance == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
        total += segmentDistance;
        proximityPenalty += calculateProximityPenalty(mapa.getHabitacion(chromosome[chromosome.length - 1]), mapa.getBase());

        return total + proximityPenalty;
    }

    // Modified method: returns 0 penalty if no valid path is found.
    private double calculateProximityPenalty(Point start, Point end) {
        List<Point> path = mapa.calcularRutaCompleta(start, end);
        if (path == null || path.isEmpty()) {
            return 0.0;
        }
        double penalty = 0.0;
        for (Point p : path) {
            penalty += getObstacleProximity(p);
        }
        return penalty;
    }

    private double getObstacleProximity(Point p) {
        int proximity = Integer.MAX_VALUE;
        for (int i = 0; i < Mapa.FILAS; i++) {
            for (int j = 0; j < Mapa.COLS; j++) {
                if (mapa.getGrid()[i][j].equals("■")) {
                    int distance = Math.abs(p.x - i) + Math.abs(p.y - j);
                    if (distance < proximity) {
                        proximity = distance;
                    }
                }
            }
        }
        return 1.0 / (proximity + 1); // Higher penalty for closer obstacles
    }
}