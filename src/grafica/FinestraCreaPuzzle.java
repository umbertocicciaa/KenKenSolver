package grafica;

import griglia.Point;
import griglia.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import static griglia.FileOperation.getOperator;

/**
 * <p>Questa classe si occupa della finestra che permette di creare puzzle customizzati dall'utente</p>
 */
public class FinestraCreaPuzzle {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel[][] grid;
    private final int size;
    private final Set<Point> points = new HashSet<>();
    private final Set<Cage> cages = new HashSet<>();
    private final MementoManager manager = new MementoManager();

    /**
     * <p>Questa classe implementa il design pattern Memento; Il memento viene utilizzato per implementare il pulsante undo
     * con il quale eliminare i cage inseriti nella finestra</p>
     */
    private record Memento(Set<Point> point, Cage cage) {
        private Memento(Set<Point> point, Cage cage) {
            this.point = new HashSet<>();
            this.point.addAll(point);
            this.cage = cage;
        }
    }

    /**
     * <p>Questa classe gestisce i memento permettendone il ripristino richiamando il metodo restore;
     * "funziona" come un Caretaker</p>
     */
    private class MementoManager {
        private final Stack<Memento> stack = new Stack<>();

        private void add(Memento memento) {
            stack.add(memento);
        }

        private void restore() {
            if (stack.size() != 1) {
                Memento memento = stack.pop();
                restoreStateMemento(memento);
            }
        }
    }

    /**
     * <p>Questo metodo salva lo stato attuale dei cage permettendone il riprsitino successivo</p>
     */
    private Memento saveStateMemento(Set<Point> points, Cage cages) {
        return new Memento(points, cages);
    }

    /**
     * <p>Questa metodo ripristina lo stato attuale a quello del memento passato in input</p>
     */
    private void restoreStateMemento(Memento memento) {
        for (Point p : memento.point) {
            grid[size - p.x() - 1][p.y()].setBackground(new Color(238, 238, 238));
            grid[size - p.x() - 1][p.y()].removeAll();
        }
        if (memento.cage != null) cages.remove(memento.cage);
        System.out.println("restored");
    }

    public FinestraCreaPuzzle(int size) {
        if (size < 3 || size > 9) throw new IllegalArgumentException("Dimensione puzzle scorretta");
        this.size = size;
        createWindow();
        createGrid();
        createButton();
        manager.add(new Memento(points, null));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * <p>Il metodo crea e inserisce i pulsanti sulla finestra</p>
     */
    private void createButton() {
        JButton addCage = new JButton("Add Cage");
        JButton undo = new JButton("Undo Cage Creation");
        undo.addActionListener(e -> manager.restore());
        JButton buildPuzzle = new JButton("Crea puzzle");
        addCage.addActionListener(e -> createCage());
        buildPuzzle.addActionListener(e -> {
            Puzzle puzzle;
            try {
                System.out.println(cages);
                Puzzle.PuzzleBuilder puzzleBuilder = new Puzzle.PuzzleBuilder(size);
                for (Cage cage : cages)
                    puzzleBuilder.addCageToPuzzle(cage);
                puzzle = puzzleBuilder.build();
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
            Cage cage = new Cage(target, operator, points.toArray(new Point[0]));
            Color color = getRandColor();
            for (Point p : points) {
                grid[size - p.x() - 1][p.y()].setBackground(color);
                JLabel label = new JLabel(targetNumber + " " + operatorCage, SwingConstants.CENTER);
                grid[size - p.x() - 1][p.y()].add(label, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();
            }
            cages.add(cage);
            manager.add(saveStateMemento(points, cage));
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

    private class MouseEventManager extends MouseAdapter {
        private final int tmpI, tmpJ;

        private MouseEventManager(int tmpI, int tmpJ) {
            this.tmpI = tmpI;
            this.tmpJ = tmpJ;
        }

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
    }

    private void createGrid() {
        JPanel panelGrid = new JPanel(new GridLayout(size, size));
        mainPanel.add(panelGrid, BorderLayout.CENTER);
        grid = new JPanel[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                grid[i][j] = new JPanel(new BorderLayout());
                grid[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
                grid[i][j].addMouseListener(new MouseEventManager(i, j));
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

