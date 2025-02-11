/**
 *
 */
package view;

import org.math.plot.*;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import logic.AlgoritmoGenetico;
import model.Individuo;
import model.Valores;
import utils.Pair;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

public class Controls extends JPanel {

    private static final long serialVersionUID = 1L;

    private AlgoritmoGenetico AG;
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
    private JSpinner genes_spinner;
    private JTextArea text_area;
    private Plot2DPanel plot2D;
    private Valores valores;

    /**
     * Constructor de la clase.
     */
    public Controls() {
        this.tam_poblacion	=new JTextField("100", 15);
        this.generaciones 	=new JTextField("100", 15);
        this.prob_cruce 	=new JTextField("0.6", 15);
        this.prob_mut 		=new JTextField("0.05", 15);
        this.precision 		=new JTextField("0.001", 15);
        this.elitismo 		=new JTextField("0", 15);
        this.genes_spinner 	=new JSpinner();

        AG=new AlgoritmoGenetico(this);

        init_GUI();
    }

    private void init_GUI() {
        setLayout(new BorderLayout());
        JPanel leftPanel =crea_panel_izquiedo();
        JPanel rightPanel=crea_panel_derecho();
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private JPanel crea_panel_izquiedo() {
        JPanel leftPanel=new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(335, 600));
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.insets=new Insets(5, 5, 5, 5);
        String[] funciones={"F1: Calibracion y Prueba",
                "F2: Mishra Bird",
                "F3: Holder table",
                "F4: Michalewicz (Binaria)",
                "F5: Michalewicz (Real)"
        };
        String[] seleccion={"Ruleta",
                "Torneo Deterministico",
                "Torneo Probabilistico",
                "Estocastico Universal",
                "Truncamiento",
                "Ranking",
                "Restos",
        };
        String[] cruce={"Mono-Punto",
                "Uniforme"};
        String[] mutacion = { "Básica"};

        funcion_CBox	=new JComboBox<>(funciones);
        seleccion_CBox	=new JComboBox<>(seleccion);
        cruce_CBox		=new JComboBox<>(cruce);
        mutacion_CBox	=new JComboBox<>(mutacion);

        text_area=new JTextArea(2, 2);
        text_area.append("Esperando una ejecucion...");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(2, 1, 10, 1);
        genes_spinner.setModel(spinnerModel);
        run_button=new JButton();
        run_button.setToolTipText("Run button");

        run_button.setIcon(load_image("icons/run.png"));
        run_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tmp=Integer.parseInt(elitismo.getText());
                // ENG: Executes the algorithm if the elitism percentage is valid.
                // ESP: Ejecuta el algoritmo si el porcentaje de elitismo es valido.
                if(tmp<0||tmp>100) actualiza_fallo("Elitismo porcentaje");
                else run();
            }
        });

        gbc.anchor=GridBagConstraints.WEST;
        gbc.gridx=0;
        gbc.gridy=0;

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
        gbc.anchor=GridBagConstraints.EAST;
        gbc.gridy++;
        leftPanel.add(new JLabel("Valor optimo:  "), gbc);
        gbc.anchor=GridBagConstraints.WEST;

        gbc.gridx++;
        gbc.gridy=0;

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
        leftPanel.add(genes_spinner, gbc);
        gbc.gridy++;
        leftPanel.add(elitismo, gbc);
        gbc.gridy++;
        leftPanel.add(text_area, gbc);

        gbc.anchor=GridBagConstraints.SOUTH;
        gbc.gridy++;
        leftPanel.add(run_button, gbc);

        return leftPanel;
    }


    private JPanel crea_panel_derecho() {
        JPanel rightPanel=new JPanel(new GridBagLayout());
        rightPanel.setPreferredSize(new Dimension(465, 600));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=1.0;
        gbc.weighty=1.0;
        gbc.fill=GridBagConstraints.BOTH;

        // Inicializa el grafico del panel.
        plot2D=new Plot2DPanel();
        // Añade los nombres de los ejes.
        plot2D.getAxis(0).setLabelText("Generacion");
        plot2D.getAxis(1).setLabelText("Fitness");


        rightPanel.add(plot2D, gbc);
        return rightPanel;
    }

    public void actualiza_grafico(double[][] vals, Pair<Double, Double> interval, Individuo mejor_ind) {
        plot2D.removeAllPlots();

        double[] x=new double[vals[0].length];
        for (int i=0;i<vals[0].length;i++) x[i]=i;

        plot2D.addLinePlot("Mejor Absoluto", x, vals[0]);
        plot2D.addLinePlot("Mejor de la Generacion", x, vals[1]);
        plot2D.addLinePlot("Media", x, vals[2]);

        plot2D.getAxis(0).setLabelText("Generacion");
        plot2D.getAxis(1).setLabelText("Fitness");
        plot2D.setFixedBounds(1, interval.get_first(), interval.get_second()); // Fix Y-axis bounds


        plot2D.addLegend("SOUTH");

        String texto_salida="Fitness: "+mejor_ind.fitness+"\n";
        int cont=1;
        for(double cromosoma: mejor_ind.getPhenotypes()) {
            texto_salida+="Variable "+(cont++)+": "+cromosoma + "\n";
        }

        text_area.setText(texto_salida);

    }

    public void actualiza_fallo(String s) {
        plot2D.removeAllPlots();
        text_area.setText(s);
    }

    private void run() {
        set_valores();
        AG.ejecuta(valores);
    }



    protected ImageIcon load_image(String path) {
        return new ImageIcon(Toolkit.getDefaultToolkit().createImage(path));
    }

    private void set_valores() {
        valores = new Valores(Integer.parseInt(tam_poblacion.getText()),
                Integer.parseInt(generaciones.getText()),
                seleccion_CBox.getSelectedIndex(),
                cruce_CBox.getSelectedIndex(),
                Double.parseDouble(prob_cruce.getText()),
                mutacion_CBox.getSelectedIndex(),
                Double.parseDouble(prob_mut.getText()),
                Double.parseDouble(precision.getText()),
                funcion_CBox.getSelectedIndex(),
                (int) genes_spinner.getValue(),
                Integer.parseInt(elitismo.getText()));
    }

    public Valores get_valores() { return valores; }

}
