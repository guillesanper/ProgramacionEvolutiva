import model.IndividuoFactory;
import model.IndividuoRobot;
import model.Mapa;
import view.MainWindow;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Mapa mapa = new Mapa();
                    IndividuoRobot ir = (IndividuoRobot) IndividuoFactory.createIndividuo(0,0.001,2,mapa);
                    String s = "";
                    for(Point p:mapa.calcularRutaCompleta(ir)){
                        s.concat(p.toString());
                    }
                    System.out.println(s);
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new MainWindow();
            }
        });
    }
}