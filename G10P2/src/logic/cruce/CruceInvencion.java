package logic.cruce;

import model.Mapa;

import java.util.*;

public class CruceInvencion extends Cruce<Integer> {

    private Mapa mapa;
    private Random random;
    private boolean invMejorado;

    public CruceInvencion(int tamCromosoma, Mapa mapa, boolean invMejorado) {
        super(tamCromosoma);
        this.mapa = mapa;
        this.random = new Random();
        this.invMejorado = invMejorado;
    }

    @Override
    public void cross(Integer[] padre1, Integer[] padre2) {
        Integer[] hijo1 = crearHijo(padre1, padre2);
        Integer[] hijo2 = crearHijo(padre2, padre1);

        // Copiar hijos a los arrays de los padres
        System.arraycopy(hijo1, 0, padre1, 0, padre1.length);
        System.arraycopy(hijo2, 0, padre2, 0, padre2.length);
    }

    private Integer[] crearHijo(Integer[] padre1, Integer[] padre2) {
        int n = padre1.length;
        Integer[] hijo = new Integer[n];
        boolean[] usado = new boolean[n + 1]; // +1 porque los valores van de 1 a n

        // Crear mapa de adyacencia combinado de ambos padres
        Map<Integer, Map<Integer, Double>> mapaAdyacencia = crearMapaAdyacencia(padre1, padre2);

        // Elegir el punto de inicio
        int actual = seleccionarInicio(padre1, padre2);
        hijo[0] = actual;
        usado[actual] = true;

        // Construir el resto del recorrido
        for (int i = 1; i < n; i++) {
            int siguiente = seleccionarSiguiente(actual, mapaAdyacencia, usado);
            hijo[i] = siguiente;
            usado[siguiente] = true;
            actual = siguiente;
        }

        // Aplicar optimización local para mejorar el resultado
        if (invMejorado) {
            optimizacionLocal(hijo);
        }
        return hijo;
    }

    private Map<Integer, Map<Integer, Double>> crearMapaAdyacencia(Integer[] padre1, Integer[] padre2) {
        int n = padre1.length;
        Map<Integer, Map<Integer, Double>> adyacencia = new HashMap<>();

        // Inicializar mapa para cada habitación
        for (int i = 1; i <= n; i++) {
            adyacencia.put(i, new HashMap<>());
        }

        // Procesar adyacencias del primer padre
        for (int i = 0; i < n; i++) {
            int ciudad = padre1[i];
            int vecino1 = padre1[(i + 1) % n];
            int vecino2 = padre1[(i - 1 + n) % n];

            double distancia1 = mapa.calcularRuta(mapa.getHabitacion(ciudad), mapa.getHabitacion(vecino1));
            double distancia2 = mapa.calcularRuta(mapa.getHabitacion(ciudad), mapa.getHabitacion(vecino2));

            adyacencia.get(ciudad).put(vecino1, distancia1);
            adyacencia.get(ciudad).put(vecino2, distancia2);
        }

        // Procesar adyacencias del segundo padre
        for (int i = 0; i < n; i++) {
            int ciudad = padre2[i];
            int vecino1 = padre2[(i + 1) % n];
            int vecino2 = padre2[(i - 1 + n) % n];

            double distancia1 = mapa.calcularRuta(mapa.getHabitacion(ciudad), mapa.getHabitacion(vecino1));
            double distancia2 = mapa.calcularRuta(mapa.getHabitacion(ciudad), mapa.getHabitacion(vecino2));

            // Si ya existe el vecino, promediamos las distancias y lo marcamos como común
            if (adyacencia.get(ciudad).containsKey(vecino1)) {
                double distanciaPromedio = (adyacencia.get(ciudad).get(vecino1) + distancia1) / 2;
                // Favorecemos conexiones comunes poniendo una distancia artificialmente baja
                adyacencia.get(ciudad).put(vecino1, distanciaPromedio * 0.8);
            } else {
                adyacencia.get(ciudad).put(vecino1, distancia1);
            }

            if (adyacencia.get(ciudad).containsKey(vecino2)) {
                double distanciaPromedio = (adyacencia.get(ciudad).get(vecino2) + distancia2) / 2;
                adyacencia.get(ciudad).put(vecino2, distanciaPromedio * 0.8);
            } else {
                adyacencia.get(ciudad).put(vecino2, distancia2);
            }
        }

        return adyacencia;
    }

    private int seleccionarInicio(Integer[] padre1, Integer[] padre2) {
        int n = padre1.length;

        // Estrategia 1: Elegir la ciudad más cercana a la base
        if (random.nextDouble() < 0.6) {
            int mejorCiudad = -1;
            double menorDistancia = Double.POSITIVE_INFINITY;

            for (int i = 1; i <= n; i++) {
                double distancia = mapa.calcularRuta(mapa.getBase(), mapa.getHabitacion(i));
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    mejorCiudad = i;
                }
            }
            return mejorCiudad;
        }

        // Estrategia 2: Elegir aleatoriamente entre padres
        if (random.nextDouble() < 0.5) {
            return padre1[random.nextInt(n)];
        } else {
            return padre2[random.nextInt(n)];
        }
    }

    private int seleccionarSiguiente(int actual, Map<Integer, Map<Integer, Double>> adyacencia, boolean[] usado) {
        Map<Integer, Double> vecinos = adyacencia.get(actual);

        // No hay vecinos disponibles
        if (vecinos == null || vecinos.isEmpty()) {
            return seleccionarPrimerNoUsado(usado);
        }

        List<Integer> candidatos = new ArrayList<>();
        List<Double> probabilidades = new ArrayList<>();
        double sumaProbabilidades = 0.0;

        // Calcular probabilidades inversamente proporcionales a la distancia
        for (Map.Entry<Integer, Double> entrada : vecinos.entrySet()) {
            int vecino = entrada.getKey();
            if (!usado[vecino]) {
                double distancia = entrada.getValue();
                // Evitar división por cero y manejar distancias infinitas
                double probabilidad = (distancia == Double.POSITIVE_INFINITY) ? 0.0 : 1.0 / (distancia + 0.1);
                candidatos.add(vecino);
                probabilidades.add(probabilidad);
                sumaProbabilidades += probabilidad;
            }
        }

        // Si no hay vecinos disponibles
        if (candidatos.isEmpty()) {
            return seleccionarPrimerNoUsado(usado);
        }

        // Selección por ruleta
        double ruleta = random.nextDouble() * sumaProbabilidades;
        double acumulado = 0.0;

        for (int i = 0; i < candidatos.size(); i++) {
            acumulado += probabilidades.get(i);
            if (acumulado >= ruleta) {
                return candidatos.get(i);
            }
        }

        // Si llegamos aquí, seleccionar el último candidato
        return candidatos.get(candidatos.size() - 1);
    }

    private int seleccionarPrimerNoUsado(boolean[] usado) {
        for (int i = 1; i < usado.length; i++) {
            if (!usado[i]) {
                return i;
            }
        }
        return -1; // No debería llegar aquí
    }

    private void optimizacionLocal(Integer[] tour) {
        int n = tour.length;
        boolean mejorado = true;
        int iteraciones = 0;
        int maxIteraciones = 50;

        while (mejorado && iteraciones < maxIteraciones) {
            mejorado = false;
            iteraciones++;

            // Buscar todas las posibles mejoras 2-opt
            for (int i = 0; i < n - 2; i++) {
                for (int j = i + 2; j < n; j++) {
                    if (j - i == n - 1) continue; // Evitar invertir el tour completo

                    // Calcular el cambio en la distancia
                    if (evaluarMejora2Opt(tour, i, j)) {
                        aplicar2Opt(tour, i, j);
                        mejorado = true;
                    }
                }
            }
        }
    }

    private boolean evaluarMejora2Opt(Integer[] tour, int i, int j) {
        int n = tour.length;
        int i1 = tour[i];
        int i2 = tour[(i + 1) % n];
        int j1 = tour[j];
        int j2 = tour[(j + 1) % n];

        double distanciaActual = mapa.calcularRuta(mapa.getHabitacion(i1), mapa.getHabitacion(i2)) +
                mapa.calcularRuta(mapa.getHabitacion(j1), mapa.getHabitacion(j2));

        double distanciaNueva = mapa.calcularRuta(mapa.getHabitacion(i1), mapa.getHabitacion(j1)) +
                mapa.calcularRuta(mapa.getHabitacion(i2), mapa.getHabitacion(j2));

        // Retornar true si la nueva distancia es menor
        return distanciaNueva < distanciaActual;
    }

    private void aplicar2Opt(Integer[] tour, int i, int j) {
        // Invertir el segmento entre i+1 y j
        int inicio = i + 1;
        int fin = j;
        while (inicio < fin) {
            int temp = tour[inicio];
            tour[inicio] = tour[fin];
            tour[fin] = temp;
            inicio++;
            fin--;
        }
    }
}