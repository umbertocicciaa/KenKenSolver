package grafica;

import griglia.*;
import griglia.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GestoreFinestra extends GestoreStato {
    private JFrame frame;
    private JPanel pannelloPrincipale;
    private JPanel panelloGriglia;
    private JButton risolvi;
    private JCheckBox controllo;
    private JButton cancella;
    private JPanel[][] griglia;
    private JTextField[][] textGriglia;
    private JMenuItem load;
    private JMenuItem save;
    private JMenuItem exit;
    private SingletonController singletonController;
    private final Stato INIT = new Init();

    public GestoreFinestra() {
        transition(INIT);
        singletonController = SingletonController.CONTROLLER;
        singletonController.setGestoreFinestra(this);
    }

    public JButton getRisolvi() {
        return risolvi;
    }

    public JCheckBox getControllo() {
        return controllo;
    }

    public JButton getCancella() {
        return cancella;
    }

    public JPanel[][] getGriglia() {
        return griglia;
    }

    public JTextField[][] getTextGriglia() {
        return textGriglia;
    }

    public class PuzzleCaricato implements Stato {
        private int size;

        private static Map<Point, Color> colori = new HashMap<>();

        public PuzzleCaricato() {
            size = singletonController.getPuzzle().getSize();
            frame.setTitle("Puzzle " + size + " x " + size);
        }

        private void createPanel() {
            panelloGriglia = new JPanel();
            panelloGriglia.setLayout(new GridLayout(size, size));
            pannelloPrincipale.add(panelloGriglia, BorderLayout.CENTER);
            textGriglia = new JTextField[size][size];
            griglia = new JPanel[size][size];
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < size; ++j) {
                    griglia[i][j] = new JPanel();
                    griglia[i][j].setSize(50, 50);
                    textGriglia[i][j] = new JTextField();
                    griglia[i][j].setLayout(new BorderLayout());
                    griglia[i][j].add(textGriglia[i][j], BorderLayout.CENTER);
                    panelloGriglia.add(griglia[i][j], i, j);
                }
            }
        }

        @Override
        public void entry() {
            risolvi.setEnabled(true);
            controllo.setEnabled(true);
            cancella.setEnabled(true);
            risolvi.setEnabled(true);
            createPanel();
            setPuzzleOnPanel();
            pannelloPrincipale.repaint();
            pannelloPrincipale.revalidate();
        }

        @Override
        public void exit() {
            pannelloPrincipale.remove(panelloGriglia);
        }

        private void setPuzzleOnPanel() {
            Set<Cage> cages = singletonController.getPuzzle().getCages();
            for (Cage cage : cages) {
                Color color = getRandColor();
                int target = cage.getTargetNumber();
                Operator operator = cage.getCageOperation();
                String op = getStringByOP(operator);
                Iterator<Point> points = Arrays.stream(cage.getCagePoint()).iterator();
                Point primo = points.next();
                JLabel textArea = new JLabel("" + target + " " + op);
                textArea.setBackground(color);
                colori.put(primo, color);
                int x = primo.getX();
                int y = primo.getY();
                griglia[x][y].add(textArea, BorderLayout.NORTH);
                griglia[x][y].setBackground(color);
                textGriglia[x][y].setBackground(color);
                while (points.hasNext()) {
                    Point point = points.next();
                    x = point.getX();
                    y = point.getY();
                    griglia[x][y].setBackground(color);
                    textGriglia[x][y].setBackground(color);
                    colori.put(point, color);
                }
            }
        }

        private String getStringByOP(Operator operator) {
            switch (operator) {
                case MUL -> {
                    return "*";
                }
                case DIV -> {
                    return "/";
                }
                case SUM -> {
                    return "+";
                }
                case SUB -> {
                    return "-";
                }
                case NONE -> {
                    return "";
                }
            }
            return "";
        }

        private Color getRandColor() {
            int r = (int) (Math.random() * 255);
            int g = (int) (Math.random() * 255);
            int b = (int) (Math.random() * 255);

            // assicurati che il colore non sia troppo scuro
            while (r < 128 || g < 128 || b < 128) {
                r = (int) (Math.random() * 255);
                g = (int) (Math.random() * 255);
                b = (int) (Math.random() * 255);
            }
            return new Color(r, g, b);
        }

        public static Color getColor(int x, int y) {
            return colori.get(new Point(x, y));
        }

    }

    public class Init implements Stato, ActionListener {

        @Override
        public void entry() {
            createWindow();
            createButton();
            frame.setVisible(true);
        }

        private void createButton() {
            JMenuBar bar = new JMenuBar();
            JMenu file = new JMenu("File");
            bar.add(file);

            load = new JMenuItem("Open");
            save = new JMenuItem("Save");
            exit = new JMenuItem("Exit");

            load.addActionListener(this);
            save.addActionListener(this);
            exit.addActionListener(this);

            file.add(load);
            file.add(save);
            file.add(exit);

            frame.setJMenuBar(bar);

            JPanel command = new JPanel();

            pannelloPrincipale.add(command, BorderLayout.SOUTH);

            risolvi = new JButton("Risolvi");
            risolvi.setEnabled(false);

            cancella = new JButton("Cancella tutto");
            cancella.setEnabled(false);


            controllo = new JCheckBox("Abilita controllo vincoli");
            controllo.setEnabled(false);


            command.add(risolvi);
            command.add(controllo);
            command.add(cancella);


            frame.add(pannelloPrincipale);
        }

        private void createWindow() {
            frame = new JFrame();
            pannelloPrincipale = new JPanel();
            pannelloPrincipale.setLayout(new BorderLayout());

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            int larghezzaFinestra = 500;
            int altezzaFinestra = 500;

            frame.setSize(larghezzaFinestra, altezzaFinestra);

            Dimension dimensioniSchermo = Toolkit.getDefaultToolkit().getScreenSize();
            int larghezzaSchermo = dimensioniSchermo.width;
            int altezzaSchermo = dimensioniSchermo.height;

            int x = (larghezzaSchermo - larghezzaFinestra) / 2;
            int y = (altezzaSchermo - altezzaFinestra) / 2;
            frame.setLocation(x, y);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == exit) {
                System.exit(1);
            } else if (e.getSource() == load) {
                try {
                    File file = null;
                    JFileChooser fileChooser = new JFileChooser();
                    int response = fileChooser.showOpenDialog(null); //select file to open
                    if (response == JFileChooser.APPROVE_OPTION) {
                        file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                    Puzzle puzzle = FileOperation.createPuzzleFromFile(file);
                    singletonController.setPuzzle(puzzle);
                    transition(new PuzzleCaricato());

                } catch (FileNotFoundException exception) {
                    JOptionPane.showMessageDialog(null, "Errore caricamento file: formato errato", "File errato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Errore generico", "Errore generico", JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getSource() == save) {

            }
        }
    }
}
