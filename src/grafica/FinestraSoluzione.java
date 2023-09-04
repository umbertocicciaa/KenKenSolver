package grafica;

import javax.swing.*;
import java.awt.*;

public class FinestraSoluzione extends JFrame {
    private JPanel gridPanel;
    private final int size;

    public FinestraSoluzione(Integer[][] soluzione) {
        this.size = soluzione.length;
        createWindow();
        createPuzzleWindow(soluzione);
        UIUtil.centra(this, 500, 500);
        this.setContentPane(gridPanel);
        this.setVisible(true);
    }


    private void createPuzzleWindow(Integer[][] soluzione) {
        JLabel[][] label = new JLabel[size][size];
        JPanel[][] pannelli = new JPanel[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                label[i][j] = new JLabel("", SwingConstants.CENTER);
                pannelli[i][j] = new JPanel(new BorderLayout());
                label[i][j].setText("" + soluzione[size - i - 1][j]);
                pannelli[i][j].setBackground(FinestraMain.PuzzleCaricato.getColor(size - i - 1, j));
                pannelli[i][j].add(label[i][j], BorderLayout.CENTER);
                gridPanel.add(pannelli[i][j], i, j);
            }
        }
    }

    private void createWindow() {
        gridPanel = new JPanel(new GridLayout(size, size));
        this.add(gridPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
