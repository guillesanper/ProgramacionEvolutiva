package logic.evaluacion;

import model.Mapa;
import utils.Pair;

import java.awt.Point;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Función de fitness que evalúa rutas basadas en el tiempo estimado de recorrido,
 * considerando que el robot puede moverse a diferentes velocidades según:
 * - Si está en tramo recto o girando
 * - Si está cerca de obstáculos
 * - Si está en tramos largos donde puede acelerar más
 */
public class FitnessPorVelocidad implements FitnessFunction {

    private Mapa mapa;
    private FitnessFunction fitnessPorLongitud;

    // Parámetros de velocidad (unidades/segundo)
    private static final double VELOCIDAD_BASE = 1.0;            // Velocidad base normal
    private static final double VELOCIDAD_GIRO = 0.5;            // Velocidad reducida en giros
    private static final double VELOCIDAD_CERCA_OBSTACULO = 0.6; // Velocidad reducida cerca de obstáculos
    private static final double ACELERACION = 0.1;               // Incremento de velocidad en tramos rectos largos
    private static final double VELOCIDAD_MAXIMA = 1.5;          // Límite máximo de velocidad

    // Distancias para considerar proximidad a obstáculos
    private static final int DIST_PELIGRO_OBSTACULO = 2;

    // Caché para rutas completas
    private Map<String, List<Point>> pathCache = new HashMap<>();
    // Caché para proximidad a obstáculos
    private Map<Point, Integer> obstacleProximityCache = new HashMap<>();

    public FitnessPorVelocidad(Mapa map) {
        this.mapa = map;
        this.fitnessPorLongitud = new FitnessPorLongitud(map);
        precalculateObstacleGrid();
    }

    /**
     * Precalcula la distancia al obstáculo más cercano para cada celda del mapa
     */
    private void precalculateObstacleGrid() {
        for (int i = 0; i < Mapa.FILAS; i++) {
            for (int j = 0; j < Mapa.COLS; j++) {
                Point p = new Point(i, j);
                obstacleProximityCache.put(p, calculateObstacleDistance(p));
            }
        }
    }

    /**
     * Calcula la distancia al obstáculo más cercano para un punto dado
     */
    private int calculateObstacleDistance(Point p) {
        int minDistance = Integer.MAX_VALUE;
        String[][] grid = mapa.getGrid();

        for (int i = 0; i < Mapa.FILAS; i++) {
            for (int j = 0; j < Mapa.COLS; j++) {
                if (grid[i][j].equals("■")) {
                    int distance = Math.abs(p.x - i) + Math.abs(p.y - j);
                    minDistance = Math.min(minDistance, distance);

                    // Optimización: si encontramos un obstáculo muy cercano, no seguimos buscando
                    if (minDistance <= 1) {
                        return minDistance;
                    }
                }
            }
        }

        return minDistance == Integer.MAX_VALUE ? 99 : minDistance;
    }

    @Override
    public double calculateFitness(Integer[] chromosome) {
        // Verificar si la ruta es válida usando el fitness por longitud
        double distanciaTotal = fitnessPorLongitud.calculateFitness(chromosome);

        if (distanciaTotal == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY; // Ruta inválida
        }

        // Calcular tiempo total estimado de recorrido
        double tiempoTotal = 0.0;

        // Añadir tiempo del tramo base -> primera habitación
        Point basePoint = mapa.getBase();
        Point firstRoom = mapa.getHabitacion(chromosome[0]);
        tiempoTotal += calculateSegmentTime(basePoint, firstRoom);

        // Añadir tiempo entre habitaciones
        for (int i = 0; i < chromosome.length - 1; i++) {
            Point current = mapa.getHabitacion(chromosome[i]);
            Point next = mapa.getHabitacion(chromosome[i + 1]);
            tiempoTotal += calculateSegmentTime(current, next);
        }

        // Añadir tiempo del tramo última habitación -> base
        Point lastRoom = mapa.getHabitacion(chromosome[chromosome.length - 1]);
        tiempoTotal += calculateSegmentTime(lastRoom, basePoint);

        return tiempoTotal;
    }

    @Override
    public Pair<Double, Double> getLimits() {
        return new Pair<>(50.00, 500.0);
    }

    /**
     * Calcula el tiempo estimado para recorrer un segmento entre dos puntos
     */
    private double calculateSegmentTime(Point start, Point end) {
        List<Point> path = getPath(start, end);
        if (path == null || path.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }

        double tiempoSegmento = 0.0;
        double velocidadActual = VELOCIDAD_BASE;
        int direccionActual = -1; // -1 = sin dirección definida aún
        int tramos_rectos = 0;    // Contador de pasos en la misma dirección

        for (int i = 1; i < path.size(); i++) {
            Point actual = path.get(i);
            Point anterior = path.get(i - 1);

            // Determinar dirección del movimiento actual (0: horizontal, 1: vertical)
            int direccion;
            if (actual.x == anterior.x) {
                direccion = 0; // Movimiento horizontal
            } else {
                direccion = 1; // Movimiento vertical
            }

            // Detectar si hay giro (cambio de dirección)
            boolean hayGiro = (direccionActual != -1 && direccion != direccionActual);

            // Actualizar contador de tramos rectos
            if (hayGiro) {
                tramos_rectos = 0; // Reiniciar contador si hay giro
            } else {
                tramos_rectos++; // Incrementar contador si seguimos recto
            }

            // Determinar velocidad según las condiciones
            velocidadActual = VELOCIDAD_BASE;

            // Ajuste por giro
            if (hayGiro) {
                velocidadActual = VELOCIDAD_GIRO;
            }
            // Ajuste por cercanía a obstáculos
            else if (obstacleProximityCache.get(actual) <= DIST_PELIGRO_OBSTACULO) {
                velocidadActual = VELOCIDAD_CERCA_OBSTACULO;
            }
            // Ajuste por aceleración en tramos rectos largos
            else {
                // Aumentar velocidad si llevamos varios tramos rectos (aceleración)
                velocidadActual = Math.min(
                        VELOCIDAD_MAXIMA,
                        VELOCIDAD_BASE + (ACELERACION * Math.min(tramos_rectos, 5))
                );
            }

            // Acumular tiempo para este paso (tiempo = distancia/velocidad)
            if (velocidadActual != 0) {
                tiempoSegmento += 1.0 / velocidadActual;
            }

            // Actualizar dirección actual para el siguiente paso
            direccionActual = direccion;
        }

        return tiempoSegmento;
    }

    /**
     * Obtiene la ruta completa entre dos puntos usando caché
     */
    private List<Point> getPath(Point start, Point end) {
        String key = start.x + "," + start.y + "-" + end.x + "," + end.y;
        if (!pathCache.containsKey(key)) {
            List<Point> path = mapa.calcularRutaCompleta(start, end);
            pathCache.put(key, path);
        }
        return pathCache.get(key);
    }
}