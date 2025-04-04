package logic.cruce;

import logic.evaluacion.FitnessFunctionFactory;
import model.Mapa;

import java.lang.reflect.Field;

public class CruceFactory {
    public static Cruce<?> getCruceType(String cruceType) {
        switch (cruceType) {

            default:
                return null;
        }
    }
}