package logic.evaluacion;

import model.Mapa;
import utils.Pair;

import java.awt.Point;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class FitnessPorGiros implements FitnessFunction {

    private Mapa mapa;
    private FitnessFunction fitnessPorLongitud;
    // Factor para balancear penalización por giros
    private static final double TURN_PENALTY_FACTOR = 2.0;
    // Caché para rutas completas
    private Map<String, List<Point>> pathCache = new HashMap<>();

    public FitnessPorGiros(Mapa map) {
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

        // Calcular penalización por giros en la ruta
        double totalTurnPenalty = 0.0;

        // Añadir penalización del tramo base -> primera habitación
        Point basePoint = mapa.getBase();
        Point firstRoom = mapa.getHabitacion(chromosome[0]);
        totalTurnPenalty += calculatePathTurnPenalty(basePoint, firstRoom);

        // Añadir penalización entre habitaciones
        for (int i = 0; i < chromosome.length - 1; i++) {
            Point current = mapa.getHabitacion(chromosome[i]);
            Point next = mapa.getHabitacion(chromosome[i + 1]);
            totalTurnPenalty += calculatePathTurnPenalty(current, next);
        }

        // Añadir penalización del tramo última habitación -> base
        Point lastRoom = mapa.getHabitacion(chromosome[chromosome.length - 1]);
        totalTurnPenalty += calculatePathTurnPenalty(lastRoom, basePoint);

        // Fitness final = fitness por longitud + penalización por giros
        return fitnessLongitud + (totalTurnPenalty * TURN_PENALTY_FACTOR);
    }


    // Obtiene la ruta completa entre dos puntos usando caché
    private List<Point> getPath(Point start, Point end) {
        String key = start.x + "," + start.y + "-" + end.x + "," + end.y;
        if (!pathCache.containsKey(key)) {
            List<Point> path = mapa.calcularRutaCompleta(start, end);
            pathCache.put(key, path);
        }
        return pathCache.get(key);
    }

    // Calcula la penalización por giros para una ruta
    private double calculatePathTurnPenalty(Point start, Point end) {
        List<Point> path = getPath(start, end);
        if (path == null || path.size() < 3) {
            return 0.0;  // No hay ruta o es muy corta para tener giros
        }

        int turns = 0;
        int currentDirection = -1; // -1 representa 'sin dirección definida aún'

        for (int i = 1; i < path.size(); i++) {
            Point current = path.get(i);
            Point previous = path.get(i - 1);

            // Determinar la dirección actual (0: horizontal, 1: vertical, 2: diagonal)
            int direction;
            if (current.x == previous.x) {
                direction = 0; // Movimiento horizontal
            } else if (current.y == previous.y) {
                direction = 1; // Movimiento vertical
            } else {
                direction = 2; // Movimiento diagonal (aunque A* básico no genera diagonales)
            }

            // Si ya teníamos una dirección y ha cambiado, incrementamos los giros
            if (currentDirection != -1 && direction != currentDirection) {
                turns++;
            }

            currentDirection = direction;
        }

        return turns;
    }

    @Override
    public Pair<Double, Double> getLimits() {
        return new Pair<>(0.00,600.0);
    }

}