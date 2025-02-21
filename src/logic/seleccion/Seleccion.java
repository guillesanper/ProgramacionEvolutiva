package logic.seleccion;

import java.util.Random;

public abstract class Seleccion {
    // Guardamos los indices de los individuos seleccionados en la poblacion
    int[] seleccion;

    // Almacena los valores de fitness de los individuos en la población
    double[] fitness;

    // Almacena las probabilidades asociadas a cada individuo
    double[] probabilidad;

    //Define el tamaño de la poblacion
    int tamPoblacion;

    // Problema de maximizacion(false) o minimizacion(true)
    boolean min;

    Random rand = new Random();

    public abstract int[] getSeleccion();


}
