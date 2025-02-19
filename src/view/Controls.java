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
    private JSpinner dimensionSpinner;
    private JTextArea text_area;
    private Plot2DPanel plot2D;
    private Valores valores;
    private Plot2DPanel plotHistory;
    private JButton undoButton;
    private JButton redoButton;
    private HistoryGraphic historyGraphic;
    private JTextArea historyTextArea;


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
        JPanel mediumPanel = crea_panel_derecho();
        JPanel historyPanel = crea_panel_historial();
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
                "F3: Holder table",
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

        funcion_CBox = new JComboBox<>(funciones);
        seleccion_CBox = new JComboBox<>(seleccion);
        cruce_CBox = new JComboBox<>(cruce);
        mutacion_CBox = new JComboBox<>(mutacion);

        text_area = new JTextArea(2, 2);
        text_area.append("Esperando una ejecucion...");
        text_area.setPreferredSize(new Dimension(300, 100));
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
        run_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tmp = Integer.parseInt(elitismo.getText());
                if (tmp < 0 || tmp > 100) update_error("Elitismo porcentaje");
                else run();
            }
        });

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
        leftPanel.add(new JLabel("  d:"), gbc);
        gbc.gridy++;
        leftPanel.add(new JLabel("  Elitismo:"), gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy++;
        leftPanel.add(new JLabel("Valor optimo:  "), gbc);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx++;
        gbc.gridy = 0;
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
        leftPanel.add(dimensionSpinner, gbc);
        gbc.gridy++;
        leftPanel.add(elitismo, gbc);
        gbc.gridy++;
        leftPanel.add(text_area, gbc);

        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridy++;
        leftPanel.add(run_button, gbc);

        return leftPanel;
    }


    private JPanel crea_panel_derecho() {
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


        plot2D.addLegend("SOUTH");

        String texto_salida = "Fitness: " + best.getFitness() + "\n";
        int cont = 1;
        for (double cromosoma : best.getPhenotypes()) {
            texto_salida += "Variable " + (cont++) + ": " + cromosoma + "\n";
        }

        text_area.setText(texto_salida);

        if(save)
            this.historyGraphic.saveState(new HistoryState(vals, interval, best));

    }

    public void update_error(String s) {
        plot2D.removeAllPlots();
        text_area.setText(s);
    }

    private void run() {
        setValues();
        if (this.funcion_CBox.getSelectedIndex() == 4)
            algoritmoGenetico = new AlgoritmoGenetico<Double>(this);
        else
            algoritmoGenetico = new AlgoritmoGenetico<Boolean>(this);

        algoritmoGenetico.ejecuta(valores);
    }

    private JPanel crea_panel_historial() {
        JPanel historyPanel = new JPanel(new GridBagLayout());
        historyPanel.setPreferredSize(new Dimension(300, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel("Mejor Individuo");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        historyPanel.add(titleLabel, gbc);

        gbc.gridy++;
        historyTextArea = new JTextArea(5, 20);
        historyTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        scrollPane.setPreferredSize(new Dimension(280, 100));
        historyPanel.add(scrollPane, gbc);

        gbc.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton undoButton = new JButton("Deshacer");
        JButton redoButton = new JButton("Rehacer");

        undoButton.addActionListener(e -> {
            if (historyGraphic.undo()) {
                HistoryState state = historyGraphic.getState();
                historyTextArea.setText("Mejor Individuo: " + printIndividuo(state.getBest()) + "\n");
                update_graph(state.getVals(), state.getInterval(), state.getBest(),false);
                plot2D.revalidate();
                plot2D.repaint();
            }
        });

        redoButton.addActionListener(e -> {
            if (historyGraphic.redo()) {
                HistoryState state = historyGraphic.getState();
                historyTextArea.setText("Mejor Individuo: " +printIndividuo(state.getBest()) + "\n");
                update_graph(state.getVals(), state.getInterval(), state.getBest(),false);
            }
        });

        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);

        gbc.gridy++;
        historyPanel.add(buttonPanel, gbc);

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
                (int) dimensionSpinner.getValue());
    }

    public Valores get_valores() {
        return valores;
    }

}
