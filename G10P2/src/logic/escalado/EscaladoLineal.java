package logic.escalado;

import model.Individuo;

public class EscaladoLineal implements Escalado {
    @Override
    public void escalarFitness(Individuo<?>[] poblacion) {
        double maxFitness = Double.MIN_VALUE;
        double minFitness = Double.MAX_VALUE;

        for (Individuo<?> ind : poblacion) {
            double fit = ind.getFitness();
            if (fit > maxFitness) maxFitness = fit;
            if (fit < minFitness) minFitness = fit;
        }

        double a = 1.5, b = 0.5;
        double gmin = 0; // Valor mínimo permitido para evitar fitness negativos

        for (Individuo<?> ind : poblacion) {
            double fit = ind.getFitness();
            double nuevoFitness = Math.max(gmin, a * fit + b);
            ind.fitness = nuevoFitness;
        }
    }
}
