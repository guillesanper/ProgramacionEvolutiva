package logic.cruce;

public class CruceFactory {
    public static Cruce<?> getCruceType(String cruceType, boolean func5, int chromosomeSize) {
        switch (cruceType) {
            case "Mono-Punto":
                return func5 ? new CruceMonoPunto<Double>(chromosomeSize) : new CruceMonoPunto<Boolean>(chromosomeSize);
            case "Uniforme":
                return func5 ? new CruceUniforme<Double>(chromosomeSize) : new CruceUniforme<Boolean>(chromosomeSize);
            case "Aritmetico":
                return new CruceAritmetico(chromosomeSize);
            case "BLX-Alfa":
                return new CruceBLXalpha(chromosomeSize);
            case "PMX":
                return new CrucePMX(chromosomeSize);
            default:
                return null;
        }
    }
}