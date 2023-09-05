package grafica;

import griglia.FileOperation;
import griglia.Operator;
import griglia.Point;
import griglia.Puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static griglia.FileOperation.getOperator;

public class FinestraCreaPuzzle {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel[][] grid;
    private final int size;
    private final java.util.List<Point> points = new ArrayList<>();
    private Puzzle.PuzzleBuilder puzzleBuilder;

    public FinestraCreaPuzzle(int size) {
        if (size < 3 || size > 9) throw new IllegalArgumentException("Dimensione puzzle scorretta");
        this.size = size;
        this.puzzleBuilder = new Puzzle.PuzzleBuilder(size);
        createWindow();
        createGrid();
        createButton();
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * <p>Il metodo crea e inserisce i pulsanti sulla finestra</p>
     */
    private void createButton() {
        JButton addCage = new JButton("Add Cage");
        JButton undo = new JButton("Undo Cage Creation");
        undo.addActionListener(e -> {
        });
        JButton buildPuzzle = new JButton("Crea puzzle");
        addCage.addActionListener(e -> createCage());
        buildPuzzle.addActionListener(e -> {
            try {
                Puzzle puzzle = puzzleBuilder.build();
                JFileChooser fileChooser = new JFileChooser();
                int response = fileChooser.showSaveDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    FileOperation.savePuzzle(file, puzzle, null);
                }
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(null, "Non hai fornito un puzzle corretto", "Creazione Fallita", JOptionPane.ERROR_MESSAGE);
                exception.printStackTrace();
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(null, "Non hai fornito un file corretto", "Creazione Fallita", JOptionPane.ERROR_MESSAGE);
                exception.printStackTrace();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addCage);
        buttonPanel.add(undo);
        buttonPanel.add(buildPuzzle);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createCage() {
        if (points.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Punti non selezionati col mouse");
            return;
        }
        String targetNumber = JOptionPane.showInputDialog("Target del cage");
        int target;
        try {
            target = Integer.parseInt(targetNumber);
            if (target <= 0) {
                JOptionPane.showMessageDialog(null, "Il target deve essere un numero intero maggiore o uguale ad 1");
                return;
            }
        } catch (NumberFormatException exc) {
            JOptionPane.showMessageDialog(null, "Il target deve essere un numero intero");
            return;
        }
        String operatorCage = JOptionPane.showInputDialog("Operator del cage: +-/*None");
        Operator operator;
        try {
            operator = getOperator(operatorCage.toLowerCase());
        } catch (IllegalArgumentException exc) {
            JOptionPane.showMessageDialog(null, "L'operatore deve essere  +-/*None");
            return;
        }
        try {
            puzzleBuilder.addCageToPuzzle(target, operator, points.toArray(new Point[0]));
            Color color = getRandColor();
            for (Point p : points) {
                grid[size - p.x() - 1][p.y()].setBackground(color);
                grid[size - p.x() - 1][p.y()].add(new JLabel(targetNumber + " " + operatorCage, SwingConstants.CENTER), BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();
            }
            points.clear();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(null, "Il cage non segue le regole del puzzle");
            exception.printStackTrace();
        }
    }

    private Color getRandColor() {
        Color res;
        do {
            res = UIUtil.getRandColor();
        } while (res.equals(Color.CYAN) || res.equals(Color.cyan));
        return res;
    }

    private void createGrid() {
        JPanel panelGrid = new JPanel(new GridLayout(size, size));
        mainPanel.add(panelGrid, BorderLayout.CENTER);
        grid = new JPanel[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                grid[i][j] = new JPanel(new BorderLayout());
                grid[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
                int tmpI = i, tmpJ = j;
                grid[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JPanel clickedPanel = (JPanel) e.getSource();
                        if (clickedPanel.getBackground().equals(new Color(238, 238, 238))) {
                            clickedPanel.setBackground(Color.CYAN);
                            Point point = new Point(size - tmpI - 1, tmpJ);
                            points.add(point);
                        } else if (clickedPanel.getBackground().equals(Color.CYAN)) {
                            clickedPanel.setBackground(new Color(238, 238, 238));
                            Point point = new Point(size - tmpI - 1, tmpJ);
                            points.remove(point);
                        }
                    }
                });
                grid[i][j].setPreferredSize(new Dimension(50, 50));
                panelGrid.add(grid[i][j], i, j);
            }
        }
    }

    private void createWindow() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.mainPanel = new JPanel(new BorderLayout());
        frame.setContentPane(mainPanel);
        UIUtil.centra(frame, 500, 500);
    }
}
