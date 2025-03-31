package logic.cruce;

import java.util.*;

public class CruceERX extends Cruce<Integer> {
    Random rand;
    private List<Set<Integer>> conectividades;

    public CruceERX(int tamCromosoma) {
        super(tamCromosoma);
        this.rand = new Random();
    }

    // Inicializar conectividades con el tamaño correcto
    private void inicializarConectividades() {
        conectividades = new ArrayList<>(tamCromosoma);
        for (int i = 0; i < tamCromosoma; i++) {
            conectividades.add(new HashSet<>());
        }
    }

    private void conectar(Integer[] c, int o, int d) {
        int index1 = c[o] - 1;
        int index2 = c[d] - 1;

        // Verificar que los índices sean válidos
        if (index1 >= 0 && index1 < conectividades.size() &&
                index2 >= 0 && index2 < conectividades.size()) {
            conectividades.get(index1).add(c[d]);
            conectividades.get(index2).add(c[o]);
        }
    }

    private Integer escoger(int num, Set<Integer> met) {
        // Verificar que el índice sea válido
        if (num <= 0 || num > conectividades.size()) {
            return null;
        }

        Set<Integer> ady = conectividades.get(num - 1);

        // Si no hay adyacentes no visitados, retornar null
        List<Integer> adyNoVisitados = new ArrayList<>();
        for (Integer el : ady) {
            if (!met.contains(el)) {
                adyNoVisitados.add(el);
            }
        }

        if (adyNoVisitados.isEmpty()) {
            return null;
        }

        // Entre los adyacentes no visitados, buscar el que tenga menos conexiones
        int min = Integer.MAX_VALUE;
        List<Integer> mins = new ArrayList<>();

        for (Integer el : adyNoVisitados) {
            int size = 0;
            // Contar solo las conexiones a nodos no visitados
            for (Integer vecino : conectividades.get(el - 1)) {
                if (!met.contains(vecino)) {
                    size++;
                }
            }

            if (size < min) {
                min = size;
                mins.clear();
                mins.add(el);
            } else if (size == min) {
                mins.add(el);
            }
        }

        // Seleccionar un elemento aleatorio de los que tienen mínimas conexiones
        if (!mins.isEmpty()) {
            return mins.get(rand.nextInt(mins.size()));
        }

        return null;
    }

    private void crearTabla(Integer[] c1, Integer[] c2) {
        inicializarConectividades();

        // Crear conexiones para ambos cromosomas
        for (int i = 0; i < tamCromosoma - 1; i++) {
            conectar(c1, i, i + 1);
            conectar(c2, i, i + 1);
        }

        // Conectar el último con el primero (circuito cerrado)
        conectar(c1, tamCromosoma - 1, 0);
        conectar(c2, tamCromosoma - 1, 0);
    }

    private Integer escogerAleatorio(Set<Integer> met) {
        // Crear una lista de todos los valores posibles que no estén en met
        List<Integer> disponibles = new ArrayList<>();
        for (int i = 1; i <= tamCromosoma; i++) {
            if (!met.contains(i)) {
                disponibles.add(i);
            }
        }

        // Si no hay elementos disponibles, retornar null
        if (disponibles.isEmpty()) {
            return null;
        }

        // Seleccionar un elemento aleatorio
        return disponibles.get(rand.nextInt(disponibles.size()));
    }

    private Integer[] crearHijo(Integer[] c) {
        Integer[] hijo = new Integer[tamCromosoma];
        Set<Integer> met = new HashSet<>();

        // Comenzamos con el primer elemento del padre
        hijo[0] = c[0];
        met.add(c[0]);

        // Construir el resto del hijo
        for (int i = 1; i < tamCromosoma; i++) {
            Integer ultimo = hijo[i-1];
            Integer siguiente = escoger(ultimo, met);

            // Si no hay un siguiente adecuado según el criterio ERX, elegir uno aleatorio
            if (siguiente == null) {
                siguiente = escogerAleatorio(met);
            }

            // Agregar el siguiente elemento al hijo y marcarlo como usado
            hijo[i] = siguiente;
            met.add(siguiente);
        }

        return hijo;
    }

    @Override
    public void cross(Integer[] c1, Integer[] c2) {
        tamCromosoma = c1.length;

        crearTabla(c1, c2);

        Integer[] h1 = crearHijo(c1);
        Integer[] h2 = crearHijo(c2);

        // Verificar que los hijos sean permutaciones válidas
        if (!esPermutacionValida(h1)) {
            h1 = generarPermutacionValida(tamCromosoma);
        }

        if (!esPermutacionValida(h2)) {
            h2 = generarPermutacionValida(tamCromosoma);
        }

        System.arraycopy(h1, 0, c1, 0, tamCromosoma);
        System.arraycopy(h2, 0, c2, 0, tamCromosoma);
    }

    private boolean esPermutacionValida(Integer[] cromosoma) {
        Set<Integer> valores = new HashSet<>();

        for (int i = 0; i < tamCromosoma; i++) {
            Integer valor = cromosoma[i];

            // Verificar que cada valor está dentro del rango válido
            if (valor == null || valor < 1 || valor > tamCromosoma) {
                return false;
            }

            // Verificar que no hay duplicados
            if (valores.contains(valor)) {
                return false;
            }

            valores.add(valor);
        }

        // Verificar que todos los valores del 1 al tamCromosoma están presentes
        return valores.size() == tamCromosoma;
    }
    

    private Integer[] generarPermutacionValida(int tamaño) {
        Integer[] permutacion = new Integer[tamaño];
        List<Integer> valores = new ArrayList<>();

        // Crear lista de valores del 1 al tamaño
        for (int i = 1; i <= tamaño; i++) {
            valores.add(i);
        }

        // Mezclar aleatoriamente
        Collections.shuffle(valores, rand);

        // Copiar a la permutación
        for (int i = 0; i < tamaño; i++) {
            permutacion[i] = valores.get(i);
        }

        return permutacion;
    }
}