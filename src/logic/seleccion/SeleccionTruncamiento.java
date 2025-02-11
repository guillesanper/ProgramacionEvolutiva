package logic.seleccion;

import java.util.Comparator;
import java.util.stream.IntStream;

public class SeleccionTruncamiento extends Seleccion {

    private double trunc; // Proporción de la población seleccionada (ej. 0.5 o 0.1)

    public SeleccionTruncamiento(double[] fitness, int tamPoblacion, boolean min, double trunc) {
        this.fitness = fitness;
        this.tamPoblacion = tamPoblacion;
        this.trunc = trunc;
        this.seleccion = new int[this.tamPoblacion];
        this.min = min;
    }

    @Override
    public int[] getSeleccion() {
        int numSeleccionados = (int) (trunc * tamPoblacion); // Número de individuos que sobreviven
        int repeticiones = tamPoblacion / numSeleccionados;  // Cuántas veces se repite cada uno

        int[] indicesOrdenados = ordenarIndicesPorFitness();
        int pos = 0;

        // Llenar la selección con los mejores individuos según trunc
        for (int i = 0; i < numSeleccionados; i++) {
            for (int j = 0; j < repeticiones; j++) {
                this.seleccion[pos++] = indicesOrdenados[i];
            }
        }
        return this.seleccion;
    }

    private int[] ordenarIndicesPorFitness() {
        // Ordenar índices por fitness (ascendente si minimizamos, descendente si maximizamos)
        return IntStream.range(0, tamPoblacion)
                .boxed()
                .sorted(Comparator.comparingDouble(i -> min ? fitness[i] : -fitness[i])) // Ordena por fitness
                .mapToInt(i -> i)
                .toArray();
    }
}
