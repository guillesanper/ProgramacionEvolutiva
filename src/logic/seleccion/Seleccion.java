package logic.seleccion;

import java.util.Random;

public abstract class Seleccion {
    Random rand;

    public Seleccion() {
        this.rand = new Random();
    }

    public abstract int[] getSeleccion(Seleccionable[] list, int tamPoblacion);
}
