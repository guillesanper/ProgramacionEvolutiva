package model;

import java.awt.Point;
import java.util.*;

public class Mapa {
    public static final int FILAS = 15;
    public static final int COLS = 15;
    private String[][] grid;
    private Point base;
    private Map<Integer, Point> habitaciones;

    public Mapa() {
        grid = new String[FILAS][COLS];
        inicializarGrid();
        definirBase();
        definirHabitaciones();
        definirObstaculos();
    }

    private void inicializarGrid() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = " "; // zona transitable
            }
        }
    }

    private void definirBase() {
        // Base en (7,7)
        base = new Point(7, 7);
        grid[7][7] = "B";
    }

    private void definirHabitaciones() {
        // Se definen las 20 habitaciones con sus coordenadas fijas
        habitaciones = new HashMap<>();
        habitaciones.put(1, new Point(2, 2));    // Sala
        habitaciones.put(2, new Point(2, 12));   // Cocina
        habitaciones.put(3, new Point(12, 2));   // Dormitorio
        habitaciones.put(4, new Point(12, 12));  // Baño
        habitaciones.put(5, new Point(2, 7));    // Comedor
        habitaciones.put(6, new Point(7, 2));    // Oficina
        habitaciones.put(7, new Point(7, 12));   // Estudio
        habitaciones.put(8, new Point(12, 7));   // Lavandería
        habitaciones.put(9, new Point(0, 7));    // Terraza
        habitaciones.put(10, new Point(7, 0));   // Garaje
        habitaciones.put(11, new Point(14, 7));  // Jardín
        habitaciones.put(12, new Point(7, 14));  // Sótano
        habitaciones.put(13, new Point(0, 0));   // Balcón
        habitaciones.put(14, new Point(0, 14));  // Despacho
        habitaciones.put(15, new Point(14, 0));  // Vestíbulo
        habitaciones.put(16, new Point(14, 14)); // Cuarto de lavado
        habitaciones.put(17, new Point(4, 4));   // Sala de estar
        habitaciones.put(18, new Point(4, 12));  // Sala de juegos
        habitaciones.put(19, new Point(10, 4));  // Sala de TV
        habitaciones.put(20, new Point(10, 12)); // Sala de reuniones

        // Colocar los símbolos en el grid según la especificación:
        // Habitaciones 1-9 muestran su dígito y 10-20 se representan con letras (A a K)
        for (Map.Entry<Integer, Point> entry : habitaciones.entrySet()) {
            Point p = entry.getValue();
            int id = entry.getKey();
            String simbolo;
            if (id < 10) {
                simbolo = Integer.toString(id);
            } else {
                simbolo = String.valueOf((char) ('A' + (id - 10)));
            }
            grid[p.x][p.y] = simbolo;
        }
    }

    private void definirObstaculos() {
        // Obstáculo: pared horizontal en fila 5, columnas 5 a 9
        for (int col = 5; col <= 9; col++) {
            grid[5][col] = "■";
        }
        // Obstáculo: pared vertical en columna 10, filas 8 a 12
        for (int fila = 8; fila <= 12; fila++) {
            grid[fila][10] = "■";
        }
        // Obstáculos individuales: (10,3) y (11,4)
        grid[10][3] = "■";
        grid[11][4] = "■";
        // Obstáculo adicional – pared vertical: columna 6, filas 10 a 13
        for (int fila = 10; fila <= 13; fila++) {
            grid[fila][6] = "■";
        }
        // Obstáculo adicional – pared horizontal: fila 8, columnas 1 a 4
        for (int col = 1; col <= 4; col++) {
            grid[8][col] = "■";
        }
        // Obstáculos en la esquina superior derecha: (0,13) y (1,13)
        grid[0][13] = "■";
        grid[1][13] = "■";
        // Obstáculo adicional – pared horizontal: fila 3, columnas 8 a 11
        for (int col = 8; col <= 11; col++) {
            grid[3][col] = "■";
        }
    }

    public List<Point> calcularRutaCompleta(Individuo individuo) {
        List<Point> rutaCompleta = new ArrayList<>();
        // Obtener la base
        Point basePoint = getBase();
        // Suponemos que el cromosoma es de tipo Integer[]
        Integer[] orden = (Integer[]) individuo.chromosome.clone();
        if (orden == null || orden.length == 0) {
            return rutaCompleta;
        }

        // Desde la base a la primera habitación
        Point primeraHab = getHabitacion(orden[0]);
        List<Point> segmento = calcularRutaCompleta(basePoint, primeraHab);
        if (segmento != null) {
            rutaCompleta.addAll(segmento);
        }

        // Para cada par consecutivo de habitaciones
        for (int i = 0; i < orden.length - 1; i++) {
            Point inicio = getHabitacion(orden[i]);
            Point fin = getHabitacion(orden[i + 1]);
            segmento = calcularRutaCompleta(inicio, fin);
            if (segmento != null) {
                // Evitar duplicar el punto de unión
                if (!rutaCompleta.isEmpty() && !segmento.isEmpty()) {
                    segmento.remove(0);
                }
                rutaCompleta.addAll(segmento);
            }
        }

        // Desde la última habitación de vuelta a la base
        Point ultimaHab = getHabitacion(orden[orden.length - 1]);
        segmento = calcularRutaCompleta(ultimaHab, basePoint);
        if (segmento != null) {
            if (!rutaCompleta.isEmpty() && !segmento.isEmpty()) {
                segmento.remove(0);
            }
            rutaCompleta.addAll(segmento);
        }
        return rutaCompleta;
    }

    /**
     * Implementa el algoritmo A* para calcular la ruta (distancia) entre dos puntos.
     * Retorna la distancia mínima en pasos o -1 si no existe un camino.
     */
    public double calcularRuta(Point inicio, Point fin) {
        // Validación: si el punto de inicio o fin no es transitable, retorna -1.
        if (!esTransitable(inicio.x, inicio.y) || !esTransitable(fin.x, fin.y)) {
            return -1;
        }

        // Inicialización de estructuras para A*
        PriorityQueue<Nodo> openSet = new PriorityQueue<>(Comparator.comparingDouble(a -> a.f));
        boolean[][] cerrados = new boolean[FILAS][COLS];
        double[][] gScore = new double[FILAS][COLS];
        for (int i = 0; i < FILAS; i++) {
            Arrays.fill(gScore[i], Double.POSITIVE_INFINITY);
        }

        Nodo inicioNodo = new Nodo(inicio.x, inicio.y, 0, heuristica(inicio.x, inicio.y, fin.x, fin.y), null);
        openSet.add(inicioNodo);
        gScore[inicio.x][inicio.y] = 0;

        // Direcciones: arriba, abajo, izquierda, derecha
        int[][] direcciones = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        while (!openSet.isEmpty()) {
            Nodo actual = openSet.poll();

            // Si se llegó al destino, retorna el costo acumulado
            if (actual.x == fin.x && actual.y == fin.y) {
                return actual.g;
            }

            cerrados[actual.x][actual.y] = true;

            // Explorar vecinos
            for (int[] dir : direcciones) {
                int nx = actual.x + dir[0];
                int ny = actual.y + dir[1];

                if (nx < 0 || nx >= FILAS || ny < 0 || ny >= COLS) {
                    continue;
                }
                if (!esTransitable(nx, ny) || cerrados[nx][ny]) {
                    continue;
                }

                double costoTentativo = actual.g + 1; // Costo de moverse a una celda adyacente

                if (costoTentativo < gScore[nx][ny]) {
                    gScore[nx][ny] = costoTentativo;
                    double h = heuristica(nx, ny, fin.x, fin.y);
                    Nodo vecino = new Nodo(nx, ny, costoTentativo, h, actual);
                    openSet.add(vecino);
                }
            }
        }

        // Si no se encontró camino, retorna -1.
        return -1;
    }

    public List<Point> calcularRutaCompleta(Point inicio, Point fin) {
        if (!esTransitable(inicio.x, inicio.y) || !esTransitable(fin.x, fin.y)) {
            return null;
        }

        PriorityQueue<Nodo> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));
        boolean[][] cerrados = new boolean[FILAS][COLS];
        double[][] gScore = new double[FILAS][COLS];
        for (int i = 0; i < FILAS; i++) {
            Arrays.fill(gScore[i], Double.POSITIVE_INFINITY);
        }

        Nodo inicioNodo = new Nodo(inicio.x, inicio.y, 0, heuristica(inicio.x, inicio.y, fin.x, fin.y), null);
        openSet.add(inicioNodo);
        gScore[inicio.x][inicio.y] = 0;

        int[][] direcciones = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        Nodo destinoNodo = null;
        while (!openSet.isEmpty()) {
            Nodo actual = openSet.poll();
            if (actual.x == fin.x && actual.y == fin.y) {
                destinoNodo = actual;
                break;
            }

            cerrados[actual.x][actual.y] = true;

            for (int[] dir : direcciones) {
                int nx = actual.x + dir[0];
                int ny = actual.y + dir[1];

                // Validaciones más estrictas
                if (nx < 0 || nx >= FILAS || ny < 0 || ny >= COLS)
                    continue;

                // Solo permitir moverse a celdas transitables que no estén cerradas
                if (!esTransitable(nx, ny) || cerrados[nx][ny])
                    continue;

                double costoTentativo = actual.g + 1;
                if (costoTentativo < gScore[nx][ny]) {
                    gScore[nx][ny] = costoTentativo;
                    double h = heuristica(nx, ny, fin.x, fin.y);
                    Nodo vecino = new Nodo(nx, ny, costoTentativo, h, actual);
                    openSet.add(vecino);
                }
            }
        }

        if (destinoNodo == null) {
            return null; // no se encontró camino
        }

        // Reconstruir el camino retrocediendo desde el destino.
        List<Point> camino = new ArrayList<>();
        Nodo current = destinoNodo;
        while (current != null) {
            camino.add(new Point(current.x, current.y));
            current = current.padre;
        }
        Collections.reverse(camino);
        return camino;
    }

    /**
     * Dado un individuo representado como una lista de puntos (el orden de nodos a visitar),
     * calcula el recorrido completo entre cada par de puntos usando A* y devuelve el grid
     * que incluye el recorrido marcado (con el símbolo ".") sin sobrescribir base, habitaciones u obstáculos.
     */
    public String[][] obtenerGridConRecorridoIndividuo(List<Point> puntosIndividuo) {
        String[][] gridConRecorrido = new String[FILAS][COLS];
        // Copiar el grid original
        for (int i = 0; i < FILAS; i++) {
            System.arraycopy(grid[i], 0, gridConRecorrido[i], 0, COLS);
        }

        List<Point> rutaCompleta = new ArrayList<>();
        // Calcular el camino entre cada par de puntos consecutivos
        for (int i = 0; i < puntosIndividuo.size() - 1; i++) {
            Point inicio = puntosIndividuo.get(i);
            Point fin = puntosIndividuo.get(i + 1);
            List<Point> caminoSegmento = calcularRutaCompleta(inicio, fin);
            if (caminoSegmento == null) {
                // Podrías manejar el caso de no encontrar camino según tu lógica
                continue;
            }
            // Para evitar marcar dos veces el punto de unión, eliminamos el primer punto
            if (i > 0 && !caminoSegmento.isEmpty()) {
                caminoSegmento.remove(0);
            }
            rutaCompleta.addAll(caminoSegmento);
        }

        // Marcar el recorrido en el grid (sin sobrescribir símbolos ya presentes)
        for (Point p : rutaCompleta) {
            if (gridConRecorrido[p.x][p.y].equals(" ")) {
                gridConRecorrido[p.x][p.y] = "·";
            }
        }

        return gridConRecorrido;
    }

    // Método para validar si una celda es transitable (no es obstáculo).
    private boolean esTransitable(int x, int y) {
        // Verificar primero que las coordenadas estén dentro de los límites del grid
        if (x < 0 || x >= FILAS || y < 0 || y >= COLS) {
            return false;
        }

        // Considerar como intransitable si es un obstáculo explícito
        return !grid[x][y].equals("■");
    }

    // Heurística Manhattan para A*
    private double heuristica(int x, int y, int xf, int yf) {
        return Math.abs(x - xf) + Math.abs(y - yf);
    }

    // Clase interna para representar un nodo en la búsqueda A*
    private class Nodo {
        int x, y;
        double g;      // Costo desde el inicio hasta este nodo.
        double h;      // Estimación (heurística) desde este nodo al destino.
        double f;      // f = g + h.
        Nodo padre;

        public Nodo(int x, int y, double g, double h, Nodo padre) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.padre = padre;
        }
    }

    public Point getBase() {
        return base;
    }

    public Point getHabitacion(int id) {
        return habitaciones.get(id);
    }

    public String[][] getGrid() {
        return grid;
    }
}
