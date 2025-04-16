package view;

import logic.AlgoritmoGenetico;
import model.Individuo;
import model.Mapa;
import model.Tree;
import utils.*;
import org.math.plot.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

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
    private JCheckBox bloating_controller_checkbox;
    private JComboBox<String> funcion_CBox;
    private JComboBox<String> seleccion_CBox;
    private JComboBox<String> cruce_CBox;
    private JComboBox<String> mutacion_CBox;
    private JComboBox<String> escalado_CBox;
    private JComboBox<Integer> min_depth_CBox;
    private JSpinner dimensionSpinner;
    private JCheckBox invMejoradoCheckbox;
    private Plot2DPanel plot2D;
    private Valores valores;
    private HistoryGraphic historyGraphic;
    private JTextArea textArea;
    private JTextArea bestFitnessArea;
    private JTextArea crossedMutedArea;
    private JTextArea worstFitnessArea;
    private HouseView houseView;


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
        this.houseView = new HouseView();
        this.invMejoradoCheckbox = new JCheckBox("INV Mejorado");
        this.bloating_controller_checkbox = new JCheckBox("Controlar bloating");

        init_GUI();
    }


    private void init_GUI() {
        setLayout(new BorderLayout());
        JPanel leftPanel = crea_panel_izquiedo();
        JPanel historyPanel = crea_panel_datos();

        // Crear un JTabbedPane para contener tanto la gráfica como la vista de la casa.
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel plotPanel = crea_panel_central();
        tabbedPane.addTab("Gráfica", plotPanel);
        tabbedPane.addTab("Casa", houseView);

        // Agregar los paneles al layout principal de Controls
        add(leftPanel, BorderLayout.WEST);
        add(tabbedPane, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.EAST);
    }


    private JPanel crea_panel_izquiedo() {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(335, 600));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        String[] funciones = {
                "GROW",
                "COMPLETE",
                "RAMPED AND HALF"
        };
        String[] seleccion = {"Ruleta",
                "Torneo Deterministico",
                "Torneo Probabilistico",
                "Estocastico Universal",
                "Truncamiento",
                "Ranking",
                "Restos"
        };
        String[] cruce = {"PMX", "OX", "OXPP", "CX", "CO", "ERX", "INV"};
        String[] mutacion = {"Insercion",
                "Intercambio",
                "Inversion",
                "Heuristica",
                "Invencion"
        };
        String[] escalados = {"Ninguno", "Lineal", "Sigma", "Boltzmann"};

        funcion_CBox = new JComboBox<>(funciones);

        seleccion_CBox = new JComboBox<>(seleccion);
        cruce_CBox = new JComboBox<>(cruce);
        mutacion_CBox = new JComboBox<>(mutacion);
        escalado_CBox = new JComboBox<>(escalados);
        min_depth_CBox = new JComboBox<>();
        for (int i = 2; i <= 10; i++) {
            min_depth_CBox.addItem(i);
        }

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

        // Configurar el checkbox para que solo sea visible cuando se selecciona INV
        invMejoradoCheckbox.setVisible(false);

        // Añadir ItemListener al combobox de cruce para mostrar/ocultar el checkbox
        cruce_CBox.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                int selectedIndex = cruce_CBox.getSelectedIndex();
                // El índice 6 corresponde a "INV" en el array de cruce
                invMejoradoCheckbox.setVisible(selectedIndex == 6);
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
                Transfer t = new Transfer(state.getVals(), state.getInterval(), state.getBest(), false, state.getCrossed(), state.getMuted(),state.getWorstFitness());
                update_graph(t);
                plot2D.revalidate();
                plot2D.repaint();
            }
        });

        redoButton.addActionListener(e -> {
            if (historyGraphic.redo()) {
                HistoryState state = historyGraphic.getState();
                textArea.setText("Mejor Individuo: " + printIndividuo(state.getBest()) + "\n");
                Transfer t = new Transfer(state.getVals(), state.getInterval(), state.getBest(), false, state.getCrossed(), state.getMuted(), state.getWorstFitness());
                update_graph(t);
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
        leftPanel.add(new JLabel("  Metodo de inicializacion:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Profundidad minima:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Escalado"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Elitismo:"), gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy += 2;

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
        leftPanel.add(funcion_CBox, gbc);
        gbc.gridy++;
        leftPanel.add(min_depth_CBox, gbc);
        gbc.gridy++;
        leftPanel.add(escalado_CBox, gbc);
        gbc.gridy++;
        leftPanel.add(elitismo, gbc);

        // Añadir el checkbox INV mejorado después del elitismo
        gbc.gridy++;
        leftPanel.add(invMejoradoCheckbox, gbc);
        leftPanel.add(bloating_controller_checkbox, gbc);

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

    public void update_graph(Transfer t) {
        plot2D.removeAllPlots();

        double[] x = new double[t.vals[0].length];
        for (int i = 0; i < t.vals[0].length; i++) x[i] = i;

        plot2D.addLinePlot("Mejor Absoluto", x, t.vals[0]);
        plot2D.addLinePlot("Mejor de la Generacion", x, t.vals[1]);
        plot2D.addLinePlot("Media", x, t.vals[2]);

        plot2D.getAxis(0).setLabelText("Generacion");
        plot2D.getAxis(1).setLabelText("Fitness");
        plot2D.setFixedBounds(1, t.interval.get_first(), t.interval.get_second()); // Fix Y-axis bounds


        bestFitnessArea.setText(String.valueOf(t.best.getFitness()));
        worstFitnessArea.setText(String.valueOf(t.worstFitness));
        textArea.setText(printIndividuo(t.best));
        textArea.setCaretPosition(0);

        crossedMutedArea.setText("Cruces: " + t.crossed + " | Mutaciones: " + t.muted);

        if (t.save)
            this.historyGraphic.saveState(new HistoryState(t.vals, t.interval, t.best, t.crossed, t.muted, t.worstFitness));

        Mapa map = new Mapa();

        //houseView.setPath(map.calcularRutaCompleta(t.best));
        houseView.repaint();


    }

    public void update_error(String s) {
        plot2D.removeAllPlots();
        textArea.setText(s);
    }

    private void run() {
        setValues();
        if (this.funcion_CBox.getSelectedIndex() == 0)
            algoritmoGenetico = new AlgoritmoGenetico<Tree>(this);
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

        // Área para mostrar el mejor fitness
        bestFitnessArea = new JTextArea(1, 20);
        bestFitnessArea.setEditable(false);
        // Crear un borde negro y aplicarlo al JTextArea
        Border blackBorder = BorderFactory.createLineBorder(Color.GRAY);
        bestFitnessArea.setBorder(blackBorder);
        historyPanel.add(new JLabel("Mejor fitness:"), gbc);
        gbc.gridy++;
        historyPanel.add(bestFitnessArea, gbc);
        gbc.gridy++;

        // Área para mostrar el peor fitness
        worstFitnessArea = new JTextArea(1, 20);
        worstFitnessArea.setEditable(false);
        worstFitnessArea.setBorder(blackBorder);
        historyPanel.add(new JLabel("Peor fitness:"), gbc);
        gbc.gridy++;
        historyPanel.add(worstFitnessArea, gbc);
        gbc.gridy++;

        // Área para mostrar el orden de habitaciones
        historyPanel.add(new JLabel("Orden de habitaciones:"), gbc);
        gbc.gridy++;
        textArea = new JTextArea(15, 20); // Reduce un poco el tamaño para hacer espacio al nuevo componente
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(280, 250)); // Reduce un poco la altura
        historyPanel.add(scrollPane, gbc);
        gbc.gridy++;

        // Nueva área para mostrar cruces y mutaciones
        historyPanel.add(new JLabel("Estadísticas:"), gbc);
        gbc.gridy++;
        crossedMutedArea = new JTextArea(1, 20);
        crossedMutedArea.setEditable(false);
        crossedMutedArea.setBorder(blackBorder);
        historyPanel.add(crossedMutedArea, gbc);

        return historyPanel;
    }

    private String printIndividuo(Individuo individuo) {
        StringBuilder texto_salida = new StringBuilder();
        for (Integer alelo : (Integer[]) individuo.chromosome) {
            texto_salida.append(alelo).append("\n");
        }
        return texto_salida.toString();
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
                (String) escalado_CBox.getSelectedItem(),
                invMejoradoCheckbox.isSelected(),
                (Integer) min_depth_CBox.getSelectedItem()); // Añadimos el estado del checkbox
    }

    public Valores get_valores() {
        return valores;
    }
}