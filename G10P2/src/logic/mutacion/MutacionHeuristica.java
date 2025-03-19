package logic.mutacion;

import logic.evaluacion.FitnessFunction;
import logic.evaluacion.FitnessFunctionFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MutacionHeuristica implements Mutate {

    @Override
    public void mutate(Integer[] chromosome) {
        FitnessFunctionFactory factory = new FitnessFunctionFactory();
        FitnessFunction fn = factory.getFunction(0);
        List<Integer> allelesToChange = new ArrayList<>();
        Random rand = new Random();

        // Selecciona aleatoriamente un 5% de los índices para mutar
        for (int i = 0; i < chromosome.length; i++){
            if(rand.nextDouble() < 0.05){
                allelesToChange.add(i);
            }
        }
        // Si no se selecciona ningún alelo, no se realiza mutación
        if(allelesToChange.isEmpty()){
            return;
        }

        List<Integer[]> permutations = getAllPermutation(chromosome, allelesToChange);

        // Crear lista de candidatos (permuta y fitness)
        List<Candidate> candidateList = new ArrayList<>();
        for (Integer[] permutation : permutations) {
            double fitness = fn.calculateFitness(permutation);
            candidateList.add(new Candidate(fitness, permutation));
        }

        // Ordenar la lista de candidatos según el fitness (de menor a mayor)
        Collections.sort(candidateList, Comparator.comparingDouble(c -> c.fitness));

        // Seleccionar aleatoriamente uno de los tres mejores candidatos
        int topCount = Math.min(3, candidateList.size());
        int selectedIndex = new Random().nextInt(topCount);
        Integer[] selectedPermutation = candidateList.get(selectedIndex).permutation;

        // Actualizar el cromosoma original con la permutación seleccionada
        for (int i = 0; i < chromosome.length; i++){
            chromosome[i] = selectedPermutation[i];
        }
    }

    /**
     * Genera todas las permutaciones posibles de los valores ubicados en los índices indicados por allelesToChange,
     * y las aplica al cromosoma original.
     */
    public List<Integer[]> getAllPermutation(Integer[] originalChromosome, List<Integer> allelesToChange) {
        List<Integer[]> permutations = new ArrayList<>();
        Integer[] chromosome = originalChromosome.clone();
        List<Integer> valuesToPermute = new ArrayList<>();

        // Extraer los valores de los índices a permutar
        for (Integer index : allelesToChange) {
            valuesToPermute.add(chromosome[index]);
        }

        List<List<Integer>> permutedValues = new ArrayList<>();
        generatePermutations(valuesToPermute,    0, permutedValues);

        // Aplicar cada permutación en los índices especificados
        for (List<Integer> permuted : permutedValues) {
            Integer[] newChromosome = chromosome.clone();
            for (int i = 0; i < allelesToChange.size(); i++) {
                newChromosome[allelesToChange.get(i)] = permuted.get(i);
            }
            permutations.add(newChromosome);
        }

        return permutations;
    }

    /**
     * Método recursivo para generar todas las permutaciones de una lista.
     */
    private void generatePermutations(List<Integer> list, int index, List<List<Integer>> permutations) {
        if (index == list.size() - 1) {
            permutations.add(new ArrayList<>(list));
            return;
        }

        for (int i = index; i < list.size(); i++) {
            swap(list, index, i);
            generatePermutations(list, index + 1, permutations);
            swap(list, index, i); // Deshacer el cambio
        }
    }

    /**
     * Intercambia dos elementos en la lista.
     */
    private void swap(List<Integer> list, int i, int j) {
        Integer temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    /**
     * Clase interna para almacenar una permutación junto a su fitness.
     */
    private static class Candidate {
        double fitness;
        Integer[] permutation;

        Candidate(double fitness, Integer[] permutation) {
            this.fitness = fitness;
            this.permutation = permutation;
        }
    }
}
