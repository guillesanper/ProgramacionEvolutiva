package logic.cruce;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;


/// Clase que implementa el cruce de ciclos (CX).

public class CruceCX extends Cruce<Integer>{

    public CruceCX(int tamCromosoma) {
        super(tamCromosoma);
    }

    @Override
    public void cross(Integer[] padre1, Integer[] padre2) {
        this.tamCromosoma = padre1.length;
        // Se generan dos hijos utilizando el cruce por ciclos.
        Integer[][] hijos = cruzarCromosomaCycle(padre1, padre2);


        System.arraycopy(hijos[0], 0, padre1, 0, tamCromosoma);
        System.arraycopy(hijos[1], 0, padre2, 0, tamCromosoma);
    }

    private Integer[][] cruzarCromosomaCycle(Integer[] p1, Integer[] p2) {
        Integer[] hijo1 = new Integer[tamCromosoma];
        Integer[] hijo2 = new Integer[tamCromosoma];
        boolean[] visitado = new boolean[tamCromosoma];

        int pos = p1[0];



        return new Integer[][] { hijo1, hijo2 };
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
            if (array[i].equals(valor)) {
                return i;
            }
        }
        return -1;
    }

}
