package logic.seleccion;

public class SeleccionFactory {
    public static Seleccion getMetodoSeleccion(String seleccionType, double[] fitness, int tamPoblacion, boolean min, double mejor){
        switch (seleccionType){
            case "ruleta":
                return new SeleccionRuleta(fitness,tamPoblacion,min,mejor);
            case "torneo-det":
                return new SeleccionTorneoDeterministico(fitness, tamPoblacion, min);
            case "torneo-prob":
                return new SeleccionTorneoProbabilistico(fitness, tamPoblacion, min);
            case "truncamiento":
                return new SeleccionTruncamiento(fitness,tamPoblacion,min,mejor);
            default:
                return null;
        }

    }
}
