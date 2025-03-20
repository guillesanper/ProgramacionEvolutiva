package logic.evaluacion;

import model.Mapa;

import java.util.HashMap;

public class FitnessFunctionFactory {

    public FitnessFunctionFactory(){
        map = new Mapa();
    }

    private static Mapa map;

    public static Mapa getMap() {
        return map;
    }

    public FitnessFunction getFunction(Integer index){
        switch (index){
            case 0:
                return new FitnessPorLongitud(map);
            case 1:
                return new FitnessPorProximidadObstaculos(map);
            default:
                return new FitnessPorLongitud(map);
        }
    }
}
