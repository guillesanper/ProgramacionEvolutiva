package logic.evaluacion;

import model.Mapa;
import utils.Pair;

import java.util.HashMap;

public class FitnessFunctionFactory {

    public FitnessFunctionFactory(){
        map = new Mapa();
    }

    private static Mapa map;

    private Pair<Double,Double> interval;

    public static Mapa getMap() {
        return map;
    }

    public FitnessFunction getFunction(Integer index){
        FitnessFunction ff;
        switch (index){
            case 0:
                ff = new FitnessPorLongitud(map);
                interval = ff.getLimits();
                return ff;
            case 1:
                ff = new FitnessPorProximidadObstaculos(map);
                interval = ff.getLimits();
                return ff;
            case 2:
                ff = new FitnessPorGiros(map);
                interval = ff.getLimits();
                return ff;
            case 3:
                ff = new FitnessPorVelocidad(map);
                interval = ff.getLimits();
                return ff;
            case 4:
                ff = new FitnessPorDesviacionEuclidiana(map);
                interval = ff.getLimits();
                return ff;
            case 5:
                ff = new FitnessMulticriterio(map);
                interval = ff.getLimits();
                return ff;
            default:
                ff = new FitnessPorLongitud(map);
                interval = ff.getLimits();
                return ff;
        }
    }

    public Pair<Double,Double> getInterval() {
        return interval;
    }
}
