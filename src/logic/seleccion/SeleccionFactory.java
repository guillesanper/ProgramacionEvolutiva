package logic.seleccion;

public class SeleccionFactory {
    public static Seleccion getMetodoSeleccion(String seleccionType, double[] fitness, int tamPoblacion, boolean min, double mejor){
        switch (seleccionType){
            case "Ruleta":
                return new SeleccionRuleta(fitness,tamPoblacion,min,mejor);
            case "Torneo Deterministico":
                return new SeleccionTorneoDeterministico(fitness, tamPoblacion, min);
            case "Torneo Probabilistico":
                return new SeleccionTorneoProbabilistico(fitness, tamPoblacion, min);
            case "Estocastico Universal":
                return new SeleccionEstocasticoUniversal();
                case "Truncamiento":
                return new SeleccionTruncamiento(fitness,tamPoblacion,min,mejor);
            default:
                return null;
        }

    }
}
