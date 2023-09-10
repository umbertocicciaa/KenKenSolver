package grafica;

import mediator.SingletonController;

import javax.swing.*;
import java.awt.*;

/**
 * <p>Questa classe serve a rappresentare la finestra che verra visualizzata una volta calcolata la soluzione del puzzle</p>
 */
public class FinestraSoluzione {
    private JPanel gridPanel;
    private final int size;
    private JFrame frame;

    public FinestraSoluzione(Integer[][] soluzione) {
        this.size = soluzione.length;
        createWindow();
        createPuzzleWindow(soluzione);
        UIUtil.centra(frame, 500, 500);
        frame.setContentPane(gridPanel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * <p>Questo metodo riceve la soluzione e ne permette la rappresentazione GUI</p>
     *
     * @param soluzione matrice di Integer contenente la soluzione del puzzle
     */
    private void createPuzzleWindow(Integer[][] soluzione) {
        JLabel[][] label = new JLabel[size][size];
        JPanel[][] pannelli = new JPanel[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                label[i][j] = new JLabel("", SwingConstants.CENTER);
                pannelli[i][j] = new JPanel(new BorderLayout());
                pannelli[i][j].setPreferredSize(new Dimension(75, 75));
                label[i][j].setText("" + soluzione[size - i - 1][j]);
                pannelli[i][j].setBackground(SingletonController.getColor(size - i - 1, j));
                pannelli[i][j].add(label[i][j], BorderLayout.CENTER);
                gridPanel.add(pannelli[i][j], i, j);
            }
        }
    }

    /**
     * <p>Questo crea la finestra</p>
     */
    private void createWindow() {
        frame = new JFrame();
        gridPanel = new JPanel(new GridLayout(size, size));
        frame.add(gridPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
