package grafica;

import javax.swing.*;
import java.awt.*;

public class FinestraSoluzione extends JFrame {
    private JPanel gridPanel;
    private JPanel[][] pannelli;
    private JLabel[][] label;
    private int size;

    public FinestraSoluzione(Integer[][] soluzione) {
        this.size = soluzione.length;
        createWindow();
        createPuzzleWindow(soluzione);
        this.setVisible(true);
    }

    private void createPuzzleWindow(Integer[][] soluzione) {
        label = new JLabel[size][size];
        pannelli = new JPanel[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                label[i][j] = new JLabel("", SwingConstants.CENTER);
                pannelli[i][j] = new JPanel(new BorderLayout());
                label[i][j].setText("" + soluzione[i][j]);
                pannelli[i][j].setBackground(GestoreFinestra.PuzzleCaricato.getColor(i, j));
                pannelli[i][j].add(label[i][j], BorderLayout.CENTER);
                gridPanel.add(pannelli[i][j], i, j);
            }
        }
    }

    private void createWindow() {
        gridPanel = new JPanel(new GridLayout(size, size));
        this.add(gridPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(500, 500);
    }

}
