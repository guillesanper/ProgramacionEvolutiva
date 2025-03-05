package logic.cruce;

public class CruceFactory {
    public static Cruce<?> getCruceType(String cruceType, boolean func5, int chromosomeSize) {
        switch (cruceType) {
            case "PMX":
                return new CrucePMX(chromosomeSize);
            default:
                return null;
        }
    }
}