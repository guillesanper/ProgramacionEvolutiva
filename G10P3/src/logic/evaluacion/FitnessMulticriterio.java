package logic.evaluacion;

import model.Mapa;
import utils.Pair;

import java.awt.Point;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class FitnessMulticriterio implements FitnessFunction {

    private Mapa mapa;
    private FitnessFunction fitnessPorLongitud;
    private FitnessFunction fitnessPorProximidadObstaculos;
    private FitnessFunction fitnessPorGiros;

    // Factores de ponderación para cada criterio
    private static final double LONG_WEIGHT = 1.0;    // Peso de la longitud
    private static final double OBST_WEIGHT = 0.3;    // Peso de la proximidad a obstáculos
    private static final double TURN_WEIGHT = 0.5;    // Peso de los giros
    private static final double EUCL_WEIGHT = 0.2;    // Peso de la desviación de la ruta ideal

    // Para cálculo de distancia euclidiana
    private Map<String, Double> euclideanCache = new HashMap<>();

    public FitnessMulticriterio(Mapa map) {
        this.mapa = map;
        this.fitnessPorLongitud = new FitnessPorLongitud(map);
        this.fitnessPorProximidadObstaculos = new FitnessPorProximidadObstaculos(map);
        this.fitnessPorGiros = new FitnessPorGiros(map);
    }

    @Override
    public double calculateFitness(Integer[] chromosome) {
        // Fitness básico (longitud)
        double fitnessLongitud = fitnessPorLongitud.calculateFitness(chromosome);

        // Si la ruta no es válida, retornar infinito
        if (fitnessLongitud == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        // Obtener los otros fitness
        double fitnessObstaculos = fitnessPorProximidadObstaculos.calculateFitness(chromosome);
        double fitnessGiros = fitnessPorGiros.calculateFitness(chromosome);

        // Calcular la penalización por desviación respecto a la ruta euclidiana ideal
        double penalizacionEuclidiana = calcularPenalizacionEuclidiana(chromosome);

        // Para normalizar, restamos los valores base (longitud) de los fitness específicos
        double obstaculosNormalizado = fitnessObstaculos - fitnessLongitud;
        double girosNormalizado = fitnessGiros - fitnessLongitud;

        // Calcular el fitness combinado con la ponderación de cada criterio
        double fitnessCombinado =
                (fitnessLongitud * LONG_WEIGHT) +
                        (obstaculosNormalizado * OBST_WEIGHT) +
                        (girosNormalizado * TURN_WEIGHT) +
                        (penalizacionEuclidiana * EUCL_WEIGHT);

        return fitnessCombinado;
    }

    @Override
    public Pair<Double, Double> getLimits() {
        return new Pair<>(50.00, 500.00);
    }

    // Calcula la penalización basada en la desviación de la ruta con respecto a la distancia euclidiana
    private double calcularPenalizacionEuclidiana(Integer[] chromosome) {
        double totalPenalizacion = 0.0;

        // Tramo base -> primera habitación
        Point basePoint = mapa.getBase();
        Point firstRoom = mapa.getHabitacion(chromosome[0]);
        totalPenalizacion += calcularDesviacionEuclidiana(basePoint, firstRoom);

        // Entre habitaciones
        for (int i = 0; i < chromosome.length - 1; i++) {
            Point current = mapa.getHabitacion(chromosome[i]);
            Point next = mapa.getHabitacion(chromosome[i + 1]);
            totalPenalizacion += calcularDesviacionEuclidiana(current, next);
        }

        // Tramo última habitación -> base
        Point lastRoom = mapa.getHabitacion(chromosome[chromosome.length - 1]);
        totalPenalizacion += calcularDesviacionEuclidiana(lastRoom, basePoint);

        return totalPenalizacion;
    }

    // Calcula la desviación entre la ruta real y la distancia euclidiana
    private double calcularDesviacionEuclidiana(Point start, Point end) {
        String key = start.x + "," + start.y + "-" + end.x + "," + end.y;

        // Obtener la distancia euclidiana (en línea recta) entre los puntos
        double distanciaEuclidiana;
        if (euclideanCache.containsKey(key)) {
            distanciaEuclidiana = euclideanCache.get(key);
        } else {
            distanciaEuclidiana = Math.sqrt(
                    Math.pow(end.x - start.x, 2) +
                            Math.pow(end.y - start.y, 2)
            );
            euclideanCache.put(key, distanciaEuclidiana);
        }

        // Obtener la distancia real de la ruta
        double distanciaReal = mapa.calcularRuta(start, end);

        // Si no hay ruta válida, no hay penalización
        if (distanciaReal == Double.POSITIVE_INFINITY) {
            return 0.0;
        }

        // Calcular la desviación como proporción respecto a la distancia ideal
        double desviacion = (distanciaReal - distanciaEuclidiana) / distanciaEuclidiana;

        // Limitar la penalización para que no sea excesiva
        return Math.min(desviacion, 5.0);
    }
}