package logic.seleccion;

public class SeleccionEstocasticoUniversal extends Seleccion{
    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];

        // Generar un valor aleatorio entre 0 y 1/tamPoblacion
        double r = this.rand.nextDouble() / tamPoblacion;

        // Para cada punto de selección
        for (int i = 0; i < tamPoblacion; i++) {
            // Calcular el punto de selección actual
            double punto = r + ((double) i / tamPoblacion);

            // Encontrar el individuo correspondiente a este punto
            int j = 0;
            while (j < tamPoblacion && punto > list[j].getAccProb()) {
                j++;
            }

            // Almacenar el índice del individuo seleccionado
            seleccion[i] = list[j].getIndex();
        }

        return seleccion;
    }

}
