package view;

import javax.swing.JFrame;

public class MainView extends JFrame {

    public MainView(){
        super("Algoritmos geneticos");
        this.setSize(900,700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new Controls());
        this.setVisible(true);
    }
}
