package view;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

    public  MainWindow() {
        super("Algoritmos geneticos");
        this.setSize(1250, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.add(new Controls());
        this.setVisible(true);
    }
}
