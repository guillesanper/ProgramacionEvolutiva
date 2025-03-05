package logic.seleccion;

public class SeleccionFactory {
    public static Seleccion getMetodoSeleccion(String seleccionType){
        switch (seleccionType){
            case "Ruleta":
                return new SeleccionRuleta();
            case "Torneo Deterministico":
                return new SeleccionTorneoDeterministico();
            case "Torneo Probabilistico":
                return new SeleccionTorneoProbabilistico();
            case "Estocastico Universal":
                return new SeleccionEstocasticoUniversal();
            case "Truncamiento":
                return new SeleccionTruncamiento();
            case "Restos":
                return new SeleccionRestos();
            case "Ranking":
                return new SeleccionRanking();
            default:
                return new SeleccionRuleta();
        }

    }
}
