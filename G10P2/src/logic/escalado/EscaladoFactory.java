package logic.escalado;

import java.util.HashMap;

public class EscaladoFactory {
    private static final HashMap<String, Escalado> mapa_escalados = new HashMap<>();

    static {
        mapa_escalados.put("Sigma", new EscaladoSigma());
        mapa_escalados.put("Boltzmann", new EscaladoBoltzmann(1.0));
        mapa_escalados.put("Lineal", new EscaladoLineal());
    }

    public static Escalado getEscalado(String tipo) {
        return mapa_escalados.getOrDefault(tipo, new EscaladoLineal());
    }

}
