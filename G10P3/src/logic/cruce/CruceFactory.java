package logic.cruce;

public class CruceFactory {
    public static Cruce<?> getCruceType(String cruceType) {
        return new TreeCross();
    }
}