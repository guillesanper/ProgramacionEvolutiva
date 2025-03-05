// Escalado Sigma
package logic.escalado;

import model.Individuo;

public class EscaladoSigma implements Escalado {
    @Override
    public void escalarFitness(Individuo<?>[] poblacion) {
        double media = 0, desviacion = 0;
        int n = poblacion.length;

        for (Individuo<?> ind : poblacion) {
            media += ind.getFitness();
        }
        media /= n;

        for (Individuo<?> ind : poblacion) {
            desviacion += Math.pow(ind.getFitness() - media, 2);
        }
        desviacion = Math.sqrt(desviacion / n);

        for (Individuo<?> ind : poblacion) {
            double nuevoFitness = Math.max(0, ind.getFitness() - media + 2 * desviacion);
            ind.fitness = nuevoFitness;
        }
    }
}