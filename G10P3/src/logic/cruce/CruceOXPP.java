package logic.cruce;

import java.util.Random;

/// Clase que implementa el cruce OXPP (Order-based crossover with prioritized positions).

public class CruceOXPP extends Cruce<Integer> {

    public CruceOXPP(int tamCromosoma) {
        super(tamCromosoma);
    }

    @Override
    public void cross(Integer[] padre1, Integer[] padre2) {
        Random r = new Random();
        tamCromosoma = padre1.length;
        // Definir el array de posiciones prioritarias (común para ambos individuos).
        // Por ejemplo, supongamos que para un cromosoma de 20 posiciones:
        boolean[] esPrioritaria = new boolean[tamCromosoma];
        for(int i = 0; i < tamCromosoma; i++) {
            if(r.nextBoolean())
                esPrioritaria[i] = true;
        }

        // Hijo1:
        // En posiciones prioritarias, se colocan los genes de padre2;
        // en las demás, se mantiene el orden relativo de padre1.
        Integer[] hijo1 = cruzarCromosoma(padre1, padre2, esPrioritaria);

        // Hijo2:
        // En posiciones prioritarias, se colocan los genes de padre1;
        // en las demás, se mantiene el orden relativo de padre2.
        Integer[] hijo2 = cruzarCromosoma(padre2, padre1, esPrioritaria);

        System.arraycopy(hijo1, 0, padre1, 0, tamCromosoma);
        System.arraycopy(hijo2, 0, padre2, 0, tamCromosoma);
    }

    /**
     * Realiza el cruce OXPP para un solo cromosoma.
     *
     * @param pNonPrioritario Cromosoma que aportará el orden relativo para las posiciones no prioritarias.
     * @param pPrioritario Cromosoma del cual se toman los genes para las posiciones prioritarias.
     * @param esPrioritaria Array booleano que indica cuáles posiciones son prioritarias.
     * @return Un nuevo cromosoma hijo resultante del cruce.
     */
    private Integer[] cruzarCromosoma(Integer[] pNonPrioritario, Integer[] pPrioritario, boolean[] esPrioritaria) {
        Integer[] hijo = new Integer[tamCromosoma];

        // Paso 1: En las posiciones prioritarias se intercambian los genes.
        // Es decir, se coloca en el hijo el gen correspondiente de pPrioritario.
        for (int i = 0; i < tamCromosoma; i++) {
            if (esPrioritaria[i]) {
                hijo[i] = pPrioritario[i];
            }
        }

        // Paso 2: Rellenar los huecos con el orden relativo del cromosoma pNonPrioritario,
        // omitiendo los genes que ya se han copiado (evitando duplicados).
        int indiceHijo = 0;
        for (int i = 0; i < tamCromosoma; i++) {
            int gene = pNonPrioritario[i];
            // Comprobar si el gene ya se encuentra en el hijo.
            boolean existe = false;
            for (int j = 0; j < tamCromosoma; j++) {
                if (hijo[j] != null && hijo[j].equals(gene)) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                // Buscar la siguiente posición vacía en el hijo.
                while (hijo[indiceHijo] != null) {
                    indiceHijo++;
                }
                hijo[indiceHijo] = gene;
            }
        }
        return hijo;
    }
}
