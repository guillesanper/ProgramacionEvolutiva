package logic.evaluacion;

import model.Mapa;
import java.awt.Point;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class FitnessPorProximidadObstaculos implements FitnessFunction {

    private Mapa mapa;
    private FitnessFunction fitnessPorLongitud;
    // Factor para balancear penalización por proximidad a obstáculos
    private static final double OBSTACLE_PROXIMITY_FACTOR = 5.0;
    // Caché para rutas completas
    private Map<String, List<Point>> pathCache = new HashMap<>();
    // Caché para proximidad a obstáculos
    private Map<Point, Double> obstacleProximityCache = new HashMap<>();

    public FitnessPorProximidadObstaculos(Mapa map) {
        this.mapa = map;
        this.fitnessPorLongitud = new FitnessPorLongitud(map);
        precalculateObstacleGrid();
    }

    // Precalcula la distancia a obstáculos para cada celda del mapa
    private void precalculateObstacleGrid() {
        for (int i = 0; i < Mapa.FILAS; i++) {
            for (int j = 0; j < Mapa.COLS; j++) {
                Point p = new Point(i, j);
                obstacleProximityCache.put(p, calculatePointObstacleProximity(p));
            }
        }
    }

    @Override
    public double calculateFitness(Integer[] chromosome) {
        // Obtener el fitness por longitud (fitness original)
        double fitnessLongitud = fitnessPorLongitud.calculateFitness(chromosome);

        // Si la ruta no es válida, retornar infinito
        if (fitnessLongitud == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        // Calcular penalización por proximidad a obstáculos
        double totalProximityPenalty = 0.0;

        // Añadir penalización del tramo base -> primera habitación
        Point basePoint = mapa.getBase();
        Point firstRoom = mapa.getHabitacion(chromosome[0]);
        totalProximityPenalty += calculatePathProximityPenalty(basePoint, firstRoom);

        // Añadir penalización entre habitaciones
        for (int i = 0; i < chromosome.length - 1; i++) {
            Point current = mapa.getHabitacion(chromosome[i]);
            Point next = mapa.getHabitacion(chromosome[i + 1]);
            totalProximityPenalty += calculatePathProximityPenalty(current, next);
        }

        // Añadir penalización del tramo última habitación -> base
        Point lastRoom = mapa.getHabitacion(chromosome[chromosome.length - 1]);
        totalProximityPenalty += calculatePathProximityPenalty(lastRoom, basePoint);

        // Fitness final = fitness por longitud + penalización por proximidad
        return fitnessLongitud + (totalProximityPenalty * OBSTACLE_PROXIMITY_FACTOR);
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

    // Calcula la penalización por proximidad a obstáculos para una ruta
    private double calculatePathProximityPenalty(Point start, Point end) {
        List<Point> path = getPath(start, end);
        if (path == null || path.isEmpty()) {
            return 0.0;  // No hay ruta, penalización cero (consistente con calcularRuta = INFINITY)
        }

        double penalty = 0.0;
        for (Point p : path) {
            penalty += obstacleProximityCache.getOrDefault(p, 0.0);
        }
        return penalty;
    }

    // Calcula la proximidad a obstáculos para un punto específico
    private double calculatePointObstacleProximity(Point p) {
        int proximity = Integer.MAX_VALUE;

        // Busca el obstáculo más cercano
        for (int i = 0; i < Mapa.FILAS; i++) {
            for (int j = 0; j < Mapa.COLS; j++) {
                if (mapa.getGrid()[i][j].equals("■")) {
                    int distance = Math.abs(p.x - i) + Math.abs(p.y - j);
                    proximity = Math.min(proximity, distance);

                    // Optimización: si encontramos un obstáculo adyacente, ya no busquemos más
                    if (proximity <= 1) {
                        break;
                    }
                }
            }
            // Si ya encontramos un obstáculo adyacente, salimos del bucle exterior también
            if (proximity <= 1) {
                break;
            }
        }

        // Función de penalización por proximidad ajustada
        // Penalización alta para obstáculos cercanos, casi nula para lejanos
        return proximity == Integer.MAX_VALUE ? 0.0 : 1.0 / (proximity + 1);
    }
}