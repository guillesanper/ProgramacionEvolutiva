package logic.cruce;

import logic.evaluacion.FitnessFunctionFactory;
import model.Mapa;

import java.lang.reflect.Field;

public class CruceFactory {
    public static Cruce<?> getCruceType(String cruceType, int chromosomeSize, boolean invMejorado) {
        switch (cruceType) {
            case "PMX":
                return new CrucePMX(chromosomeSize);
            case "OX":
                return new CruceOX(chromosomeSize);
            case "CX":
                return new CruceCX(chromosomeSize);
            case "OXPP":
                return new CruceOXPP(chromosomeSize);
            case "CO":
                return new CruceCO(chromosomeSize);
            case "INV":
                return new CruceInvencion(chromosomeSize, FitnessFunctionFactory.getMap(),invMejorado);
            case "ERX":
                return new CruceERX(chromosomeSize);
            default:
                return null;
        }
    }
}