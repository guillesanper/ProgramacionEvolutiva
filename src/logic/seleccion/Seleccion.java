package logic.seleccion;

import java.util.Random;

public abstract class Seleccion {
    // Problema de maximizacion(false) o minimizacion(true)
    boolean min;

    Random rand;

    public Seleccion(boolean min) {
        this.min = min;
        this.rand = new Random();
    }

    public abstract int[] getSeleccion(Seleccionable[] list, int tamPoblacion);
}
