package logic.evaluacion;

import model.Mapa;

public class FitnessPorLongitud implements FitnessFunction {

    private Mapa mapa;

    public FitnessPorLongitud(Mapa map) {
        this.mapa = map;
    }

    @Override
    public double calculateFitness(Integer[] chromosome) {
        // El recorrido comienza y termina en la base (7,7)
        // Se suma la distancia desde la base a la primera habitación, entre cada par consecutivo,
        // y desde la última habitación de regreso a la base.
        // La función de búsqueda (por ejemplo, A*) se invoca para cada segmento.
        double total = 0.0;
        // Obtiene la ruta desde la base a la primera habitación
        total += mapa.calcularRuta(mapa.getBase(), mapa.getHabitacion(chromosome[0]));
        // Rutas intermedias
        for (int i = 0; i < chromosome.length - 1; i++) {
            total += mapa.calcularRuta(mapa.getHabitacion(chromosome[i]), mapa.getHabitacion(chromosome[i + 1]));
        }
        // Ruta de la última habitación de regreso a la base
        total += mapa.calcularRuta(mapa.getHabitacion(chromosome[chromosome.length - 1]), mapa.getBase());
        // Si algún segmento es inviable, se puede asignar una penalización alta.
        // Por ejemplo:
        if(total < 0) total += 10000;

        return  total;
    }
}
