package logic.seleccion;

public class SeleccionFactory {
    public static Seleccion getMetodoSeleccion(String seleccionType, boolean min, double mejor){
        switch (seleccionType){
            case "Ruleta":
                return new SeleccionRuleta(min);
            case "Torneo Deterministico":
                return new SeleccionTorneoDeterministico(min);
            case "Torneo Probabilistico":
                return new SeleccionTorneoProbabilistico(min);
            case "Estocastico Universal":
                return new SeleccionEstocasticoUniversal(min);
            case "Truncamiento":
                return new SeleccionTruncamiento(min, mejor);
            default:
                return null;
        }

    }
}
