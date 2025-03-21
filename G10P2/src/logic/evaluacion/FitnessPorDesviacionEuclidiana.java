package logic.evaluacion;

import model.Mapa;
import utils.Pair;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class FitnessPorDesviacionEuclidiana implements FitnessFunction {

    private Mapa mapa;
    private FitnessFunction fitnessPorLongitud;
    // Factor para balancear penalización por desviación de la distancia euclidiana
    private static final double EUCLIDEAN_DEVIATION_FACTOR = 1.5;
    // Caché para distancias euclidianas
    private Map<String, Double> euclideanDistanceCache = new HashMap<>();

    public FitnessPorDesviacionEuclidiana(Mapa map) {
        this.mapa = map;
        this.fitnessPorLongitud = new FitnessPorLongitud(map);
    }

    @Override
    public double calculateFitness(Integer[] chromosome) {
        // Obtener el fitness por longitud (fitness original)
        double fitnessLongitud = fitnessPorLongitud.calculateFitness(chromosome);

        // Si la ruta no es válida, retornar infinito
        if (fitnessLongitud == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        // Calcular penalización por desviación euclidiana
        double totalDeviation = 0.0;

        // Añadir desviación del tramo base -> primera habitación
        Point basePoint = mapa.getBase();
        Point firstRoom = mapa.getHabitacion(chromosome[0]);
        totalDeviation += calculateSegmentDeviation(basePoint, firstRoom);

        // Añadir desviación entre habitaciones
        for (int i = 0; i < chromosome.length - 1; i++) {
            Point current = mapa.getHabitacion(chromosome[i]);
            Point next = mapa.getHabitacion(chromosome[i + 1]);
            totalDeviation += calculateSegmentDeviation(current, next);
        }

        // Añadir desviación del tramo última habitación -> base
        Point lastRoom = mapa.getHabitacion(chromosome[chromosome.length - 1]);
        totalDeviation += calculateSegmentDeviation(lastRoom, basePoint);

        // Fitness final = fitness por longitud + penalización por desviación euclidiana
        return fitnessLongitud + (totalDeviation * EUCLIDEAN_DEVIATION_FACTOR);
    }

    @Override
    public Pair<Double, Double> getLimits() {
        return new Pair<>(50.00,500.0);
    }

    // Calcula la desviación entre la distancia real y la euclidiana para un segmento
    private double calculateSegmentDeviation(Point start, Point end) {
        // Calcular la distancia real usando el método de la clase Mapa
        double actualDistance = mapa.calcularRuta(start, end);

        // Si no hay ruta posible, no hay desviación que calcular
        if (actualDistance == Double.POSITIVE_INFINITY) {
            return 0.0;
        }

        // Obtener o calcular la distancia euclidiana
        double euclideanDistance = getEuclideanDistance(start, end);

        // Calcular la desviación como la diferencia proporcional
        // Si la ruta es muy similar a la línea recta, la penalización será baja
        // Si la ruta tiene muchas vueltas o rodeos, la penalización será alta
        double deviation = (actualDistance - euclideanDistance) / euclideanDistance;

        // Asegurar que la desviación sea siempre positiva o cero
        return Math.max(0.0, deviation);
    }

    // Calcula la distancia euclidiana entre dos puntos utilizando caché
    private double getEuclideanDistance(Point p1, Point p2) {
        String key = p1.x + "," + p1.y + "-" + p2.x + "," + p2.y;

        if (!euclideanDistanceCache.containsKey(key)) {
            double dx = p2.x - p1.x;
            double dy = p2.y - p1.y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            euclideanDistanceCache.put(key, distance);
        }

        return euclideanDistanceCache.get(key);
    }
}