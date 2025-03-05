
// Escalado de Boltzmann
package logic.escalado;

import model.Individuo;

import java.util.Arrays;

public class EscaladoBoltzmann implements Escalado {
    private double temperatura;

    public EscaladoBoltzmann(double temperatura) {
        this.temperatura = temperatura;
    }

    @Override
    public void escalarFitness(Individuo<?>[] poblacion) {
        int n = poblacion.length;
        double meanExp = Arrays.stream(poblacion)
                .mapToDouble(i -> Math.exp(i.getFitness() / temperatura))
                .average().orElse(0);

        for (Individuo<?> ind : poblacion) {
            double scaledFitness = Math.exp(ind.getFitness() / temperatura) / meanExp;
            ind.fitness = scaledFitness;
        }

        temperatura *= 0.99; // Reducir la temperatura en cada iteración
    }
}