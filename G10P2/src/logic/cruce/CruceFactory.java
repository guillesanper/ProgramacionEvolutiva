package logic.cruce;

public class CruceFactory {
    public static Cruce<?> getCruceType(String cruceType, boolean func5, int chromosomeSize) {
        switch (cruceType) {
            case "PMX":
                return new CrucePMX(chromosomeSize);
            case "OX":
                return new CruceOX(chromosomeSize);
            case "CX":
                return new CruceCX(chromosomeSize);
            case "OXPP":
                return new CruceOXPP(chromosomeSize);
            default:
                return null;
        }
    }
}