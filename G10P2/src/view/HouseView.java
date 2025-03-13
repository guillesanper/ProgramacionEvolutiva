package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class HouseView extends JPanel {
    private final int CELL_SIZE = 40;
    private final int ROWS = 15;
    private final int COLS = 15;
    private String[][] grid;
    // Lista de puntos que representa la ruta (en coordenadas de celda: columna, fila)
    private List<Point> path;

    /*
        Las celdas en blanco representan zonas transitables.
        Los círculos muestran la ubicación de la base y las habitaciones.
        B : Base central (inicio/fin)
        1-20 : Habitaciones numeradas (o letras, según el caso)
        ■ : Obstáculos/paredes
        (La ruta ya no se marca como fondo magenta en cada celda, sino con una línea)
    */
    public HouseView() {
        initGUI();
    }

    private void initGUI() {
        setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        setGrid(empty_grid);
    }

    /**
     * Setter para actualizar la grid que se debe dibujar.
     */
    public void setGrid(String[][] grid) {
        this.grid = grid;
        repaint();
    }

    /**
     * Setter para la ruta. La ruta es una lista de puntos (coordenadas de celda)
     * que se conectarán con una línea magenta, partiendo y regresando a la base.
     */
    public void setPath(List<Point> path) {
        this.path = path;
        repaint();
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

        // Recorremos la grid (se asume que la primera dimensión son las filas)
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;
                String cell = grid[row][col];
                String content = String.valueOf(cell);

                if (cell.equals("■")) {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                } else {
                    // Fondo blanco para zona transitable.
                    g2.setColor(Color.WHITE);
                    g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                    // Se ignoran los marcadores de ruta ("·"), pues la ruta se dibujará con la línea.
                    if (!cell.equals(" ") && !cell.equals("·")) {
                        // Si es la base, se dibuja en rojo; si no, se asume que es una habitación.
                        if (cell.equals("B")) {
                            g2.setColor(Color.RED); // Base en rojo
                        } else {
                            g2.setColor(Color.CYAN); // Habitaciones en cian
                        }
                        int circleDiameter = CELL_SIZE - 10;
                        int circleX = x + (CELL_SIZE - circleDiameter) / 2;
                        int circleY = y + (CELL_SIZE - circleDiameter) / 2;
                        g2.fillOval(circleX, circleY, circleDiameter, circleDiameter);
                        g2.setColor(Color.BLACK);
                        g2.drawOval(circleX, circleY, circleDiameter, circleDiameter);

                        // Dibujar el identificador centrado sobre el círculo
                        FontMetrics fm = g2.getFontMetrics();
                        int textWidth = fm.stringWidth(content);
                        int textHeight = fm.getAscent();
                        int textX = x + (CELL_SIZE - textWidth) / 2;
                        int textY = y + (CELL_SIZE - fm.getHeight()) / 2 + textHeight;
                        g2.drawString(content, textX, textY);
                    }
                }
                // Dibujar el borde de la celda.
                g2.setColor(Color.GRAY);
                g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }

        // Dibuja la ruta como una línea magenta conectando los puntos (si se ha establecido)
        if (path != null && path.size() > 1) {
            g2.setColor(Color.MAGENTA);

            for (int i = 0; i < path.size() - 1; i++) {
                Point p1 = path.get(i);
                Point p2 = path.get(i + 1);
                // Convertir coordenadas de celda a coordenadas de centro de celda (pixel)
                int x1 = p1.y * CELL_SIZE + CELL_SIZE / 2; // Nota: intercambio x e y
                int y1 = p1.x * CELL_SIZE + CELL_SIZE / 2; // Por la forma en que se guardan los puntos
                int x2 = p2.y * CELL_SIZE + CELL_SIZE / 2;
                int y2 = p2.x * CELL_SIZE + CELL_SIZE / 2;
                drawArrow(g2, x1, y1, x2, y2);
            }
        }
    }

    /**
     * Dibuja una flecha desde (x1, y1) hasta (x2, y2) con cabezales.
     */
    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        // Línea principal
        g2.drawLine(x1, y1, x2, y2);

        // Configuración para los cabezales de la flecha
        double phi = Math.toRadians(20);
        int barb = 10;
        double dy = y2 - y1;
        double dx = x2 - x1;
        double theta = Math.atan2(dy, dx);
        double rho = theta + phi;
        for (int j = 0; j < 2; j++) {
            int x = (int) (x2 - barb * Math.cos(rho));
            int y = (int) (y2 - barb * Math.sin(rho));
            g2.drawLine(x2, y2, x, y);
            rho = theta - phi;
        }
    }

    // Grid inicial de ejemplo
    private String[][] empty_grid = {
            {"13", " ", " ", " ", " ", " ", " ", "9", " ", " ", " ", " ", " ", "■", "14"},
            {" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "■", " "},
            {" ", " ", "1", " ", " ", " ", " ", "5", " ", " ", " ", " ", "2", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", "■", "■", "■", "■", " ", " ", " "},
            {" ", " ", " ", " ", "17", " ", " ", " ", " ", " ", " ", " ", "18", " ", " "},
            {" ", " ", " ", " ", " ", "■", "■", "■", "■", "■", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
            {"10", " ", "6", " ", " ", " ", " ", "B", " ", " ", " ", " ", "7", " ", "12"},
            {" ", "■", "■", "■", "■", " ", " ", " ", " ", " ", "■", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "■", " ", " ", " ", " "},
            {" ", " ", " ", "■", "19", " ", "■", " ", " ", " ", "■", " ", "20", " ", " "},
            {" ", " ", " ", " ", "■", " ", "■", " ", " ", " ", "■", " ", " ", " ", " "},
            {" ", " ", "3", " ", " ", " ", "■", "8", " ", " ", "■", " ", "4", " ", " "},
            {" ", " ", " ", " ", " ", " ", "■", " ", " ", " ", " ", " ", " ", " ", " "},
            {"15", " ", " ", " ", " ", " ", " ", "11", " ", " ", " ", " ", " ", " ", "16"}
    };
}
