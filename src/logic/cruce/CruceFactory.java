package logic.cruce;

public class CruceFactory {
    public static Cruce<?> getCruceType(String cruceType, boolean func5, int chromosomeSize) {
        switch (cruceType) {
            case "monopunto":
                return func5 ? new CruceMonoPunto<Double>(chromosomeSize) : new CruceMonoPunto<Boolean>(chromosomeSize);
            case "uniforme":
                return func5 ? new CruceUniforme<Double>(chromosomeSize) : new CruceUniforme<Boolean>(chromosomeSize);
            default:
                return null;
        }
    }
}