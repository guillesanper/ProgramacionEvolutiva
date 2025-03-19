package logic.cruce;

public class CruceCX extends Cruce<Integer> {

    public CruceCX(int tamCromosoma) {
        super(tamCromosoma);
    }

    @Override
    public void cross(Integer[] padre1, Integer[] padre2) {
        this.tamCromosoma = padre1.length;

        // Creamos copias de los padres para no modificarlos directamente
        Integer[] p1Copy = new Integer[tamCromosoma];
        Integer[] p2Copy = new Integer[tamCromosoma];
        System.arraycopy(padre1, 0, p1Copy, 0, tamCromosoma);
        System.arraycopy(padre2, 0, p2Copy, 0, tamCromosoma);

        // Realizamos el cruce de ciclos
        Integer[] hijo1 = new Integer[tamCromosoma];
        Integer[] hijo2 = new Integer[tamCromosoma];

        // Determinamos los ciclos
        boolean[] visitado = new boolean[tamCromosoma];
        int numVisitados = 0;

        // Mientras no hayamos visitado todas las posiciones
        while (numVisitados < tamCromosoma) {
            // Encontramos la primera posición no visitada
            int posicionInicial = 0;
            while (posicionInicial < tamCromosoma && visitado[posicionInicial]) {
                posicionInicial++;
            }

            // Si hemos visitado todas las posiciones, salimos
            if (posicionInicial >= tamCromosoma) {
                break;
            }

            // Comenzamos un nuevo ciclo
            int posicionActual = posicionInicial;
            do {
                // Marcamos la posición como visitada
                visitado[posicionActual] = true;
                numVisitados++;

                // El primer hijo toma los valores del primer padre en el ciclo
                hijo1[posicionActual] = p1Copy[posicionActual];
                // El segundo hijo toma los valores del segundo padre en el ciclo
                hijo2[posicionActual] = p2Copy[posicionActual];

                // Buscamos el valor del segundo padre en la posición actual
                int valorABuscar = p2Copy[posicionActual];

                // Buscamos la posición de ese valor en el primer padre
                posicionActual = buscarIndice(p1Copy, valorABuscar);

            } while (posicionActual != posicionInicial && !visitado[posicionActual]);
        }

        // Completamos los hijos con los valores del otro padre
        // para las posiciones que no forman parte de ningún ciclo
        for (int i = 0; i < tamCromosoma; i++) {
            if (hijo1[i] == null) {
                hijo1[i] = p2Copy[i];
            }
            if (hijo2[i] == null) {
                hijo2[i] = p1Copy[i];
            }
        }

        // Copiamos los hijos a los padres
        System.arraycopy(hijo1, 0, padre1, 0, tamCromosoma);
        System.arraycopy(hijo2, 0, padre2, 0, tamCromosoma);
    }

    /**
     * Busca el índice en el array donde se encuentra el valor especificado.
     *
     * @param array El array en el que buscar.
     * @param valor El valor a buscar.
     * @return El índice donde se encuentra el valor o -1 si no se encuentra.
     */
    private int buscarIndice(Integer[] array, int valor) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].equals(valor)) {
                return i;
            }
        }
        return -1;
    }
}