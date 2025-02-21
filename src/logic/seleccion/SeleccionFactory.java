package logic.seleccion;

public class SeleccionFactory {
    public static Seleccion getMetodoSeleccion(String seleccionType, Seleccionable[] list, int tamPoblacion, boolean min, double mejor){
        switch (seleccionType){
            case "Ruleta":
                return new SeleccionRuleta(list, tamPoblacion, min);
            case "Torneo Deterministico":
                return new SeleccionTorneoDeterministico(list, tamPoblacion, min);
            case "Torneo Probabilistico":
                return new SeleccionTorneoProbabilistico(list, tamPoblacion, min);
            case "Estocastico Universal":
                return new SeleccionEstocasticoUniversal(list, tamPoblacion, min);
                case "Truncamiento":
                return new SeleccionTruncamiento(list, tamPoblacion, min, mejor);
            default:
                return null;
        }

    }
}
