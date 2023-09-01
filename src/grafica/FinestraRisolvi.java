package grafica;

import javax.swing.*;
import java.awt.*;

public class FinestraRisolvi extends JFrame {
    private JButton risolviBack, risolviDFS;
    private JPanel mainPanel;
    private SingletonController singletonController;

    public FinestraRisolvi() {
        createWindow();
        createButton();
        singletonController = SingletonController.CONTROLLER;
        singletonController.setFinestraRisolvi(this);
        this.pack();
        this.setVisible(true);
    }

    private void createButton() {
        mainPanel.add(new JLabel("Seleziona l'algoritmo risolutore (DFS poco piu efficente)"));
        risolviBack = new JButton("Risolvi con Backtracking");
        risolviDFS = new JButton("Risolvi con DFS");
        mainPanel.add(risolviBack);
        mainPanel.add(risolviDFS);
    }

    public JButton getRisolviBack() {
        return risolviBack;
    }

    public JButton getRisolviDFS() {
        return risolviDFS;
    }

    private void createWindow() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainPanel = new JPanel();
        this.add(mainPanel);

        Dimension dimensioniSchermo = Toolkit.getDefaultToolkit().getScreenSize();
        int larghezzaSchermo = dimensioniSchermo.width;
        int altezzaSchermo = dimensioniSchermo.height;
        int x = (larghezzaSchermo) / 2;
        int y = (altezzaSchermo) / 2;

        this.setLocation(x, y);
    }

}
