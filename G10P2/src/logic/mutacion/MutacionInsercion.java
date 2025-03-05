package logic.mutacion;

import java.util.Random;

public class MutacionInsercion implements Mutate {
    @Override
    public void mutate(Integer[] chromosome) {
        Random rand = new Random();
        int length = chromosome.length;
        // Seleccionar una posición aleatoria en el cromosoma
        int pos = rand.nextInt(length);
        // Si pos es 0, no se puede mover a una posición anterior, por lo que salimos
        if (pos == 0) {
            return;
        }
        // Seleccionar una posición aleatoria menor que pos (en el rango [0, pos))
        int target = rand.nextInt(pos);
        // Guardar el valor a mover
        Integer valor = chromosome[pos];
        // Desplazar los elementos entre target y pos - 1 hacia la derecha
        for (int i = pos; i > target; i--) {
            chromosome[i] = chromosome[i - 1];
        }
        // Insertar el valor en la posición target
        chromosome[target] = valor;
    }
}

