package logic.seleccion;

import java.util.Random;

public abstract class Seleccion {
    // Guardamos los indices de los individuos seleccionados en la poblacion
    int[] seleccion;

    Seleccionable[] list;

    // Define el tamaño de la poblacion
    int tamPoblacion;

    // Problema de maximizacion(false) o minimizacion(true)
    boolean min;

    Random rand;

    public Seleccion(Seleccionable[] list, int tamPoblacion, boolean min) {
        this.seleccion = new int[tamPoblacion];
        this.list = list;

        this.tamPoblacion = tamPoblacion;

        this.min = min;
        this.rand = new Random();
    }

    public abstract int[] getSeleccion();



}
