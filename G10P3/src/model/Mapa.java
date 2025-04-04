package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa el entorno de la hormiga y rastrea su estado.
 * Simula el comportamiento que tendría la vista HouseView pero sin componentes gráficos
 * para poder ejecutar la evaluación de fitness de manera eficiente.
 */
public class Mapa {
    private static final int ROWS = 32;
    private static final int COLS = 32;

    // Matriz que representa el estado del tablero
    private String[][] grid;

    // Posición actual de la hormiga (fila, columna)
    private Point position;

    // Dirección actual de la hormiga (0=Este, 1=Sur, 2=Oeste, 3=Norte)
    private int direction;

    // Contador de alimentos encontrados
    private int foodEaten;

    // Lista de posiciones visitadas para análisis posterior si es necesario
    private List<Point> path;

    /**
     * Constructor que inicializa el tablero con el rastro de Santa Fe.
     */
    public Mapa() {
        // Inicializar la matriz
        grid = new String[ROWS][COLS];

        // Llenar con espacios vacíos
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = " ";
            }
        }

        // Cargar el rastro de comida de Santa Fe
        loadSantaFeTrail();

        // Inicializar posición y dirección de la hormiga
        position = new Point(0, 0);
        direction = 0; // Este
        foodEaten = 0;

        // Inicializar la lista de ruta
        path = new ArrayList<>();
        path.add(new Point(position));
    }

    /**
     * Carga el rastro de Santa Fe con 89 bocados de comida
     * (idéntico al presente en HouseView).
     */
    private void loadSantaFeTrail() {
        // Rastro "Santa Fe" (89 posiciones):
        int[][] santaFeTrail = {
                {0,0}, {1,0}, {2,0}, {3,0},        // Parte superior inicial
                {3,1}, {3,2}, {3,3}, {3,4}, {3,5},
                {4,5}, {5,5}, {6,5},
                {8,5}, {9,5}, {10,5}, {11,5}, {12,5},
                {12,6}, {12,7}, {12,8}, {12,9}, {12,10},
                {12,12}, {12,13}, {12,14}, {12,15},
                {12,18}, {12,19}, {12,20}, {12,21},{12,22},{12,23},
                {11,24}, {10,24}, {9,24},{8,24},{7,24},                 // Extremo derecho
                {4,24},{3,24},{1,25},{1,26},{1,27},{1,28},
                {2,30},{3,30},{4,30},{5,30},
                {7,29},{7,28},
                {8,27},{9,27},{10,27},{11,27},{12,27},{13,27},{14,27},
                {16,26},{16,25},{16,24},{16,21},{16,19},{16,18},{16,17},
                {17,16},{20,15},{20,14},{20,11},{20,10},{20,9},{20,8},{21,5},{22,5},{24,4},{24,3},{25,2},
                {26,2},{27,2},{29,3},{29,4},{29,6},{29,9},{29,12},{28,14},{27,14},{26,14},{24,18},{27,19},
                {26,22},{23,24}// Parte superior final
        };

        // Colocar la comida en el grid según las coordenadas anteriores
        for (int[] coord : santaFeTrail) {
            grid[coord[1]][coord[0]] = "F";  // Recuerda: coord[0] = x (col), coord[1] = y (fila)
        }

        // La posición inicial ya no tiene comida, es donde está la hormiga
        grid[0][0] = "A";
    }

    /**
     * Mueve la hormiga hacia adelante en la dirección actual.
     * Si hay comida en esa celda, la consume.
     */
    public void moveForward() {
        // Calcular nueva posición según la dirección
        int newX = position.x;
        int newY = position.y;

        switch (direction) {
            case 0: // Este
                newY = (newY + 1) % COLS;
                break;
            case 1: // Sur
                newX = (newX + 1) % ROWS;
                break;
            case 2: // Oeste
                newY = (newY - 1 + COLS) % COLS;
                break;
            case 3: // Norte
                newX = (newX - 1 + ROWS) % ROWS;
                break;
        }

        // Marcar la posición actual como parte del camino
        grid[position.x][position.y] = "·";

        // Actualizar posición
        position = new Point(newX, newY);

        // Registrar el movimiento en el camino
        path.add(new Point(position));

        // Verificar si hay comida en la nueva posición
        if ("F".equals(grid[newX][newY])) {
            foodEaten++;
            // Marcar que la comida ha sido consumida
            grid[newX][newY] = "A";
        } else {
            // Si no hay comida, simplemente marcar la posición de la hormiga
            grid[newX][newY] = "A";
        }
    }

    /**
     * Gira la hormiga 90 grados a la derecha.
     */
    public void turnRight() {
        direction = (direction + 1) % 4;
    }

    /**
     * Gira la hormiga 90 grados a la izquierda.
     */
    public void turnLeft() {
        direction = (direction + 3) % 4;
    }

    /**
     * Verifica si hay comida en la celda adelante.
     * @return true si hay comida adelante, false en caso contrario
     */
    public boolean isFoodAhead() {
        int x = position.x;
        int y = position.y;

        switch (direction) {
            case 0: // Este
                y = (y + 1) % COLS;
                break;
            case 1: // Sur
                x = (x + 1) % ROWS;
                break;
            case 2: // Oeste
                y = (y - 1 + COLS) % COLS;
                break;
            case 3: // Norte
                x = (x - 1 + ROWS) % ROWS;
                break;
        }

        return "F".equals(grid[x][y]);
    }

    /**
     * Obtiene la cantidad de alimentos encontrados/consumidos.
     * @return número de alimentos consumidos
     */
    public int getFoodEaten() {
        return foodEaten;
    }

    /**
     * Obtiene la lista de posiciones visitadas por la hormiga.
     * @return lista de puntos que representan el camino recorrido
     */
    public List<Point> getPath() {
        return path;
    }

    /**
     * Obtiene una copia del estado actual del tablero.
     * @return matriz que representa el estado actual del tablero
     */
    public String[][] getGrid() {
        String[][] copy = new String[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, COLS);
        }
        return copy;
    }

    /**
     * Obtiene la posición actual de la hormiga.
     * @return punto con la posición actual
     */
    public Point getPosition() {
        return new Point(position);
    }

    /**
     * Obtiene la dirección actual de la hormiga.
     * @return entero que representa la dirección (0=Este, 1=Sur, 2=Oeste, 3=Norte)
     */
    public int getDirection() {
        return direction;
    }

    public static int getRows(){
        return ROWS;
    }

    public static int getCols(){
        return COLS;
    }
}