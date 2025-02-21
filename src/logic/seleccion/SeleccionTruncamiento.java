package logic.seleccion;

import java.util.Comparator;
import java.util.stream.IntStream;

public class SeleccionTruncamiento extends Seleccion {

    private final double trunc; // Proporción de la población seleccionada (ej. 0.5 o 0.1)

    public SeleccionTruncamiento(double trunc) {
        this.trunc = trunc;
    }

    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];

        int numSeleccionados = (int) (trunc * tamPoblacion); // Número de individuos que sobreviven
        int repeticiones = tamPoblacion / numSeleccionados;  // Cuántas veces se repite cada uno

        int[] indicesOrdenados = ordenarIndicesPorFitness(list, tamPoblacion);
        int pos = 0;

        // Llenar la selección con los mejores individuos según trunc
        for (int i = 0; i < numSeleccionados; i++) {
            for (int j = 0; j < repeticiones; j++) {
                seleccion[pos++] = indicesOrdenados[i];
            }
        }
        return seleccion;
    }

    private int[] ordenarIndicesPorFitness(Seleccionable[] list, int tamPoblacion) {
        // Ordenar índices por fitness (ascendente si minimizamos, descendente si maximizamos)
        return IntStream.range(0, tamPoblacion)
                .boxed()
                .sorted(Comparator.comparingDouble(i -> -1*list[i].getFitness())) // Ordena por fitness
                .mapToInt(i -> i)
                .toArray();
    }
}
