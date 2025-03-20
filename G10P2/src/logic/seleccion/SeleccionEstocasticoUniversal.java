package logic.seleccion;

public class SeleccionEstocasticoUniversal extends Seleccion {
    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];

        // Generar un punto de inicio aleatorio entre 0 y 1/tamPoblacion
        double paso = 1.0 / tamPoblacion;
        double r = 0.05;//this.rand.nextDouble() * paso;

        // Para cada posición de selección
        for (int i = 0; i < tamPoblacion; i++) {
            // Calcular la posición de la marca actual
            double posicionMarca = r + i * paso;
            if (posicionMarca >= 1.0) posicionMarca = 0.999999; // Evitar problemas con 1.0

            // Buscar el individuo correspondiente a esta marca
            int j = 0;
            while (j < list.length && list[j].getAccProb() < posicionMarca) {
                j++;
            }

            // Si llegamos al final sin encontrar, tomar el último
            if (j >= list.length) j = list.length - 1;

            seleccion[i] = j;
        }

        return seleccion;
    }
}