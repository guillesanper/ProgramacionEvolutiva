package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class HouseView extends JPanel {
    private final int CELL_SIZE = 20; // Reduced cell size to fit 32x32 grid
    private final int ROWS = 32;
    private final int COLS = 32;
    private String[][] grid;
    // Lista de puntos que representa la ruta de la hormiga (en coordenadas de celda: columna, fila)
    private List<Point> antPath;
    // Dirección actual de la hormiga (0=Este, 1=Sur, 2=Oeste, 3=Norte)
    private int antDirection = 0;
    // Posición actual de la hormiga
    private Point antPosition = new Point(0, 0);
    // Contador de alimentos encontrados
    private int foodFound = 0;

    /*
        Las celdas en el grid representan:
        " " : Espacio vacío
        "F" : Comida (Food)
        "A" : Hormiga (Ant)
        "·" : Ruta de la hormiga
    */
    public HouseView() {
        initGUI();
    }

    private void initGUI() {
        setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        setGrid(createEmptyGrid());
        loadSantaFeTrail();
    }

    /**
     * Crea un grid vacío 32x32
     */
    private String[][] createEmptyGrid() {
        String[][] emptyGrid = new String[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                emptyGrid[i][j] = " ";
            }
        }
        return emptyGrid;
    }

    /**
     * Carga el rastro de Santa Fe con 89 bocados de comida
     */
    /**
     * Carga el rastro de Santa Fe con 89 bocados de comida
     * (versión estándar usada habitualmente en ejemplos de Genetic Programming).
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

        // Colocar la hormiga en la posición inicial (por ejemplo, en (0,0))
        grid[0][0] = "A";
    }


    /**
     * Setter para actualizar la grid que se debe dibujar.
     */
    public void setGrid(String[][] grid) {
        this.grid = grid;
        repaint();
    }

    /**
     * Setter para la ruta de la hormiga.
     */
    public void setAntPath(List<Point> path) {
        this.antPath = path;
        repaint();
    }

    /**
     * Actualiza la posición y orientación de la hormiga.
     */
    public void updateAntPosition(Point newPosition, int direction) {
        // Limpiar posición anterior
        if (grid[antPosition.x][antPosition.y].equals("A")) {
            grid[antPosition.x][antPosition.y] = "·"; // Marcar el camino recorrido
        }

        // Actualizar posición considerando que el tablero es toroidal
        newPosition.x = (newPosition.x + ROWS) % ROWS;
        newPosition.y = (newPosition.y + COLS) % COLS;

        // Verificar si hay comida
        if (grid[newPosition.x][newPosition.y].equals("F")) {
            foodFound++;
        }

        // Colocar la hormiga en la nueva posición
        antPosition = newPosition;
        antDirection = direction;
        grid[antPosition.x][antPosition.y] = "A";

        repaint();
    }

    /**
     * Obtiene el número de alimentos encontrados.
     */
    public int getFoodFound() {
        return foodFound;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Si la grid no ha sido asignada, no se dibuja nada.
        if (grid == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Recorremos la grid
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;
                String cell = grid[row][col];

                // Fondo de la celda
                g2.setColor(Color.WHITE);
                g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Dibujar contenido según el tipo de celda
                if (cell.equals("F")) {
                    // Comida - círculo marrón
                    g2.setColor(new Color(139, 69, 19)); // Marrón
                    g2.fillOval(x + 3, y + 3, CELL_SIZE - 6, CELL_SIZE - 6);
                } else if (cell.equals("A")) {
                    // Hormiga - triángulo rojo que indica la dirección
                    g2.setColor(Color.RED);
                    int[] xPoints = new int[3];
                    int[] yPoints = new int[3];

                    // Calcular puntos del triángulo según la dirección
                    switch (antDirection) {
                        case 0: // Este
                            xPoints[0] = x + CELL_SIZE - 4;
                            yPoints[0] = y + CELL_SIZE / 2;
                            xPoints[1] = x + 4;
                            yPoints[1] = y + 4;
                            xPoints[2] = x + 4;
                            yPoints[2] = y + CELL_SIZE - 4;
                            break;
                        case 1: // Sur
                            xPoints[0] = x + CELL_SIZE / 2;
                            yPoints[0] = y + CELL_SIZE - 4;
                            xPoints[1] = x + 4;
                            yPoints[1] = y + 4;
                            xPoints[2] = x + CELL_SIZE - 4;
                            yPoints[2] = y + 4;
                            break;
                        case 2: // Oeste
                            xPoints[0] = x + 4;
                            yPoints[0] = y + CELL_SIZE / 2;
                            xPoints[1] = x + CELL_SIZE - 4;
                            yPoints[1] = y + 4;
                            xPoints[2] = x + CELL_SIZE - 4;
                            yPoints[2] = y + CELL_SIZE - 4;
                            break;
                        case 3: // Norte
                            xPoints[0] = x + CELL_SIZE / 2;
                            yPoints[0] = y + 4;
                            xPoints[1] = x + CELL_SIZE - 4;
                            yPoints[1] = y + CELL_SIZE - 4;
                            xPoints[2] = x + 4;
                            yPoints[2] = y + CELL_SIZE - 4;
                            break;
                    }

                    g2.fillPolygon(xPoints, yPoints, 3);
                } else if (cell.equals("·")) {
                    // Ruta recorrida - punto magenta
                    g2.setColor(Color.MAGENTA);
                    g2.fillOval(x + CELL_SIZE/2 - 2, y + CELL_SIZE/2 - 2, 4, 4);
                }

                // Borde de la celda
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }

        // Dibuja la ruta completa de la hormiga si está disponible
        if (antPath != null && antPath.size() > 1) {
            g2.setColor(Color.MAGENTA);
            g2.setStroke(new BasicStroke(1.5f));

            for (int i = 0; i < antPath.size() - 1; i++) {
                Point p1 = antPath.get(i);
                Point p2 = antPath.get(i + 1);

                // Convertir coordenadas de celda a coordenadas de centro de celda (pixel)
                int x1 = p1.y * CELL_SIZE + CELL_SIZE / 2;
                int y1 = p1.x * CELL_SIZE + CELL_SIZE / 2;
                int x2 = p2.y * CELL_SIZE + CELL_SIZE / 2;
                int y2 = p2.x * CELL_SIZE + CELL_SIZE / 2;

                // Considerar tablero toroidal para dibujar líneas
                if (Math.abs(x2 - x1) > COLS * CELL_SIZE / 2 || Math.abs(y2 - y1) > ROWS * CELL_SIZE / 2) {
                    // Si la distancia es mayor que la mitad del tablero, se trata de un "salto" toroidal
                    // En este caso, no dibujamos una línea directa
                } else {
                    g2.drawLine(x1, y1, x2, y2);
                }
            }
        }

        // Mostrar información sobre el estado actual
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("Comida encontrada: " + foodFound + "/89", 10, ROWS * CELL_SIZE + 15);
    }

    /**
     * Método para avanzar la hormiga en la dirección actual
     */
    public void moveForward() {
        Point newPosition = new Point(antPosition);

        // Calcular nueva posición según la dirección
        switch (antDirection) {
            case 0: // Este
                newPosition.y = (newPosition.y + 1) % COLS;
                break;
            case 1: // Sur
                newPosition.x = (newPosition.x + 1) % ROWS;
                break;
            case 2: // Oeste
                newPosition.y = (newPosition.y - 1 + COLS) % COLS;
                break;
            case 3: // Norte
                newPosition.x = (newPosition.x - 1 + ROWS) % ROWS;
                break;
        }

        // Actualizar posición
        updateAntPosition(newPosition, antDirection);
    }

    /**
     * Método para girar a la derecha
     */
    public void turnRight() {
        antDirection = (antDirection + 1) % 4;
        repaint();
    }

    /**
     * Método para girar a la izquierda
     */
    public void turnLeft() {
        antDirection = (antDirection + 3) % 4;
        repaint();
    }

    /**
     * Método para verificar si hay comida adelante
     */
    public boolean isFoodAhead() {
        int x = antPosition.x;
        int y = antPosition.y;

        // Calcular posición adelante según la dirección
        switch (antDirection) {
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

        return grid[x][y].equals("F");
    }
}