/**
 *
 */
package view;

import org.math.plot.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import logic.AlgoritmoGenetico;
import model.Individuo;
import model.Valores;
import utils.HistoryGraphic;
import utils.HistoryState;
import utils.Pair;

public class Controls extends JPanel {

    private static final long serialVersionUID = 1L;

    private AlgoritmoGenetico algoritmoGenetico;
    private JButton run_button;
    private JTextField tam_poblacion;
    private JTextField generaciones;
    private JTextField prob_cruce;
    private JTextField prob_mut;
    private JTextField precision;
    private JTextField elitismo;
    private JComboBox<String> funcion_CBox;
    private JComboBox<String> seleccion_CBox;
    private JComboBox<String> cruce_CBox;
    private JComboBox<String> mutacion_CBox;
    private JComboBox<String> escalado_CBox;
    private JSpinner dimensionSpinner;
    private Plot2DPanel plot2D;
    private Valores valores;
    private HistoryGraphic historyGraphic;
    private JTextArea textArea;


    /**
     * Constructor de la clase.
     */
    public Controls() {
        this.tam_poblacion = new JTextField("100", 15);
        this.generaciones = new JTextField("100", 15);
        this.prob_cruce = new JTextField("0.6", 15);
        this.prob_mut = new JTextField("0.05", 15);
        this.precision = new JTextField("0.001", 15);
        this.elitismo = new JTextField("0", 15);
        this.dimensionSpinner = new JSpinner();
        this.historyGraphic = new HistoryGraphic();

        init_GUI();
    }

    private void init_GUI() {
        setLayout(new BorderLayout());
        JPanel leftPanel = crea_panel_izquiedo();
        JPanel mediumPanel = crea_panel_central();
        JPanel historyPanel = crea_panel_datos();
        add(leftPanel, BorderLayout.WEST);
        add(mediumPanel, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.EAST);
    }

    private JPanel crea_panel_izquiedo() {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(335, 600));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Más separación
        String[] funciones = {"F1: Calibracion y Prueba",
                "F2: Mishra Bird",
                "F3: Schubert",
                "F4: Michalewicz (Binaria)",
                "F5: Michalewicz (Real)"
        };
        String[] seleccion = {"Ruleta",
                "Torneo Deterministico",
                "Torneo Probabilistico",
                "Estocastico Universal",
                "Truncamiento",
                "Ranking",
                "Restos",
        };
        String[] cruce = {"Mono-Punto",
                "Uniforme"};
        String[] mutacion = {"Básica"};
        String[] escalados={"Ninguno","Lineal","Sigma","Boltzmann"};

        funcion_CBox = new JComboBox<>(funciones);
        funcion_CBox.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                int selectedIndex = funcion_CBox.getSelectedIndex();
                dimensionSpinner.setEnabled(selectedIndex == 3 || selectedIndex == 4);
                if (selectedIndex != 3 && selectedIndex != 4) dimensionSpinner.setValue(2);

                // Actualizar opciones de cruce dinámicamente
                cruce_CBox.removeAllItems();
                cruce_CBox.addItem("Mono-Punto");
                cruce_CBox.addItem("Uniforme");

                if (selectedIndex == 4) { // Solo para la última función
                    cruce_CBox.addItem("Aritmetico");
                    cruce_CBox.addItem("BLX-Alfa");
                }
            }
        });

        seleccion_CBox = new JComboBox<>(seleccion);
        cruce_CBox = new JComboBox<>(cruce);
        mutacion_CBox = new JComboBox<>(mutacion);
        escalado_CBox = new JComboBox<>(escalados);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(2, 1, 10, 1);
        dimensionSpinner.setModel(spinnerModel);
        dimensionSpinner.setEnabled(false);

        funcion_CBox.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                int selectedIndex = funcion_CBox.getSelectedIndex();
                dimensionSpinner.setEnabled(selectedIndex == 3 || selectedIndex == 4);
                if (selectedIndex != 3 && selectedIndex != 4) dimensionSpinner.setValue(2);
            }
        });

        run_button = new JButton();
        run_button.setToolTipText("Run button");
        ImageIcon icon = load_image("icons/run.png", 20, 20);
        run_button.setIcon(icon);
        run_button.addActionListener(e -> {
            int tmp = Integer.parseInt(elitismo.getText());
            if (tmp < 0 || tmp > 100) update_error("Porcentaje de elitismo incorrecto");
            else run();
        });

        // Panel para botones de historial
        JPanel historyButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centrar botones
        JButton undoButton = new JButton("Deshacer");
        JButton redoButton = new JButton("Rehacer");

        undoButton.addActionListener(e -> {
            if (historyGraphic.undo()) {
                HistoryState state = historyGraphic.getState();
                textArea.setText("Mejor Individuo: " + printIndividuo(state.getBest()) + "\n");
                update_graph(state.getVals(), state.getInterval(), state.getBest(),false);
                plot2D.revalidate();
                plot2D.repaint();
            }
        });

        redoButton.addActionListener(e -> {
            if (historyGraphic.redo()) {
                HistoryState state = historyGraphic.getState();
                textArea.setText("Mejor Individuo: " +printIndividuo(state.getBest()) + "\n");
                update_graph(state.getVals(), state.getInterval(), state.getBest(),false);
            }
        });

        historyButtonsPanel.add(undoButton);
        historyButtonsPanel.add(redoButton);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;


        leftPanel.add(new JLabel("  Tam. Poblacion:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Num. Generaciones:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Metodo de Seleccion:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Metodo de Cruce:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Prob. Cruce:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Metodo de Mutacion:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Prob. Mutacion:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Precision:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Funcion:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Escalado"),gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  d:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Elitismo:"), gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy++;

        leftPanel.add(new JLabel("Historial de ejecuciones:  "), gbc);
        gbc.anchor = GridBagConstraints.WEST;


        gbc.gridy = 0;
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hace que ocupen todo el ancho disponible
        gbc.weightx = 1.0; // Permite que se expandan horizontalmente


        leftPanel.add(tam_poblacion, gbc);
        gbc.gridy++;
        leftPanel.add(generaciones, gbc);
        gbc.gridy++;
        leftPanel.add(seleccion_CBox, gbc);
        gbc.gridy++;
        leftPanel.add(cruce_CBox, gbc);
        gbc.gridy++;
        leftPanel.add(prob_cruce, gbc);
        gbc.gridy++;
        leftPanel.add(mutacion_CBox, gbc);
        gbc.gridy++;
        leftPanel.add(prob_mut, gbc);
        gbc.gridy++;
        leftPanel.add(precision, gbc);
        gbc.gridy++;
        leftPanel.add(funcion_CBox, gbc);
        gbc.gridy++;
        leftPanel.add(escalado_CBox, gbc);
        gbc.gridy++;
        leftPanel.add(dimensionSpinner, gbc);
        gbc.gridy++;
        leftPanel.add(elitismo, gbc);

        JPanel runAndHistoryPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcRun = new GridBagConstraints();
        gbcRun.gridx = 0;
        gbcRun.gridy = 0;
        runAndHistoryPanel.add(historyButtonsPanel, gbcRun); // Botones arriba

        gbcRun.gridy = 1;
        runAndHistoryPanel.add(run_button, gbcRun); // Botón Run abajo

        // Añadir el panel combinado al panel izquierdo
        gbc.gridy++;
        leftPanel.add(runAndHistoryPanel, gbc);


        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridy++;
        leftPanel.add(run_button, gbc);

        return leftPanel;
    }


    private JPanel crea_panel_central() {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setPreferredSize(new Dimension(465, 600));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // Inicializa el grafico del panel.
        plot2D = new Plot2DPanel();

        // Añade los nombres de los ejes.
        plot2D.getAxis(0).setLabelText("Generacion");
        plot2D.getAxis(1).setLabelText("Fitness");
        plot2D.addLegend("SOUTH");


        rightPanel.add(plot2D, gbc);
        return rightPanel;
    }

    public void update_graph(double[][] vals, Pair<Double, Double> interval, Individuo best, boolean save) {
        plot2D.removeAllPlots();

        double[] x = new double[vals[0].length];
        for (int i = 0; i < vals[0].length; i++) x[i] = i;

        plot2D.addLinePlot("Mejor Absoluto", x, vals[0]);
        plot2D.addLinePlot("Mejor de la Generacion", x, vals[1]);
        plot2D.addLinePlot("Media", x, vals[2]);

        plot2D.getAxis(0).setLabelText("Generacion");
        plot2D.getAxis(1).setLabelText("Fitness");
        plot2D.setFixedBounds(1, interval.get_first(), interval.get_second()); // Fix Y-axis bounds


        textArea.setText(printIndividuo(best));

        if(save)
            this.historyGraphic.saveState(new HistoryState(vals, interval, best));

    }

    public void update_error(String s) {
        plot2D.removeAllPlots();
        textArea.setText(s);
    }

    private void run() {
        setValues();
        if (this.funcion_CBox.getSelectedIndex() == 4)
            algoritmoGenetico = new AlgoritmoGenetico<Double>(this);
        else
            algoritmoGenetico = new AlgoritmoGenetico<Boolean>(this);

        algoritmoGenetico.ejecuta(valores);
    }

    private JPanel crea_panel_datos() {
        JPanel historyPanel = new JPanel(new GridBagLayout());
        historyPanel.setPreferredSize(new Dimension(300, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridy++;
        textArea = new JTextArea(20, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(280, 300));

        historyPanel.add(new JLabel("Valor optimo:"));
        gbc.gridy++;
        historyPanel.add(scrollPane, gbc);

        return historyPanel;
    }

    private String printIndividuo(Individuo individuo) {
        String texto_salida = "\nFitness: " + individuo.getFitness() + "\n";
        int cont = 1;
        for (double cromosoma : individuo.getPhenotypes()) {
            texto_salida += "Variable " + (cont++) + ": " + cromosoma + "\n";
        }
        return texto_salida;
    }


    protected ImageIcon load_image(String path, int width, int height) {
        return new ImageIcon(Toolkit.getDefaultToolkit().createImage(path).getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    private void setValues() {
        valores = new Valores(Integer.parseInt(tam_poblacion.getText()),
                Integer.parseInt(generaciones.getText()),
                (String) seleccion_CBox.getSelectedItem(),
                (String) cruce_CBox.getSelectedItem(),
                Double.parseDouble(prob_cruce.getText()),
                mutacion_CBox.getSelectedIndex(),
                Double.parseDouble(prob_mut.getText()),
                Double.parseDouble(precision.getText()),
                funcion_CBox.getSelectedIndex(),
                Integer.parseInt(elitismo.getText()),
                (int) dimensionSpinner.getValue(),
                (String) escalado_CBox.getSelectedItem());
    }

    public Valores get_valores() {
        return valores;
    }

}
