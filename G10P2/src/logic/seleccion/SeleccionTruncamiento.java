package logic.seleccion;

import java.util.Arrays;
import java.util.Comparator;

public class SeleccionTruncamiento extends Seleccion {

    private final double trunc; // Proporción de la población seleccionada (ej. 0.5 o 0.1)

    public SeleccionTruncamiento() {
        this.trunc = 0.6; // 50%
    }

    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];

        // Ordenar los individuos por fitness en orden descendente (mejor fitness primero)
        Arrays.sort(list, Comparator.comparingDouble(Seleccionable::getFitness).reversed());

        // Determinar el número de individuos seleccionables según trunc
        int numSeleccionables = (int) (list.length * this.trunc);
        numSeleccionables = Math.max(numSeleccionables, 1); // Asegurar al menos 1 individuo seleccionado

        // Cantidad de veces que cada seleccionado se debe repetir
        int repeticiones = tamPoblacion / numSeleccionables;
        int resto = tamPoblacion % numSeleccionables; // Restantes a distribuir

        int index = 0;
        for (int i = 0; i < numSeleccionables; i++) {
            for (int j = 0; j < repeticiones; j++) {
                seleccion[index++] = list[i].getIndex();
            }
        }

        // Distribuir el resto equitativamente a los primeros seleccionados
        for (int i = 0; i < resto; i++) {
            seleccion[index++] = list[i].getIndex();
        }

        return seleccion;
    }

}
