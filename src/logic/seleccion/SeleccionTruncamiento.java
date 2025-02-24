package logic.seleccion;

public class SeleccionTruncamiento extends Seleccion {

    private final double trunc; // Proporción de la población seleccionada (ej. 0.5 o 0.1)

    public SeleccionTruncamiento() {
        this.trunc = 0.5; // 50%
    }

    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];

        int numSeleccionados = (int) (trunc * tamPoblacion); // Número de individuos que sobreviven
        int repeticiones = tamPoblacion / numSeleccionados;  // Cuántas veces se repite cada uno

        int metidos = 0;

        // Llenar la selección con los mejores individuos según trunc
        for (int i = 0; i < numSeleccionados; i++) {
            for (int j = 0; j < repeticiones; j++) {
                seleccion[metidos++] = list[i].getIndex();
            }
        }
        return seleccion;
    }
}
