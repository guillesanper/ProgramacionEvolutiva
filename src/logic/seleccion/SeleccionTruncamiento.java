package logic.seleccion;

import java.util.Arrays;

public class SeleccionTruncamiento extends Seleccion {

    private final double trunc; // Proporción de la población seleccionada (ej. 0.5 o 0.1)

    public SeleccionTruncamiento() {
        this.trunc = 0.5; // 50%
    }

    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];

        // Número de individuos a seleccionar (al menos 1)
        int numSeleccionados = Math.max(1, (int) (trunc * tamPoblacion));
        int repeticiones = tamPoblacion / numSeleccionados; // Cuántas veces se repite cada uno

        // Ordenar los índices en base al fitness (de mayor a menor)
        Seleccionable[] ordenados = Arrays.copyOf(list, tamPoblacion);
        Arrays.sort(ordenados, (a, b) -> Double.compare(b.getFitness(), a.getFitness()));


        int pos = 0;
        for (int i = 0; i < numSeleccionados; i++) {
            for (int j = 0; j < repeticiones && pos < tamPoblacion; j++) {
                seleccion[pos++] = Arrays.asList(list).indexOf(ordenados[i]); // Obtener índice original
            }
        }


        return seleccion;
    }
}
