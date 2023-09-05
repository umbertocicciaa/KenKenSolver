package grafica;

import giocare.Gioco;
import griglia.*;
import griglia.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FinestraMain implements ActionListener {
    private JFrame frame;
    private JPanel pannelloPrincipale;
    private JPanel panelloGriglia;
    private JButton risolvi;
    private JCheckBox controllo;
    private JButton cancella;
    private JButton submit;
    private JPanel[][] griglia;
    private JTextField[][] textGriglia;
    private JMenuItem load;
    private JMenuItem save;
    private JMenuItem exit;
    private JMenuItem help;
    private JMenuItem nuovo;

    private final SingletonController singletonController;

    private final GestoreStato gestoreStato = new GestoreStato() {
    };

    public FinestraMain() {
        createWindow();
        createButton();
        singletonController = SingletonController.CONTROLLER;
        singletonController.setGestoreFinestra(this);
        frame.pack();
        frame.setVisible(true);

    }

    public JButton getRisolvi() {
        return risolvi;
    }

    public JCheckBox getControllo() {
        return controllo;
    }

    public JButton getSubmit() {
        return submit;
    }

    public JButton getCancella() {
        return cancella;
    }

    public JTextField[][] getTextGriglia() {
        return textGriglia;
    }

    private void createButton() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        bar.add(file);

        JMenu helpM = new JMenu("Help");
        help = new JMenuItem("Help");
        helpM.add(help);
        help.addActionListener(this);
        bar.add(helpM);

        load = new JMenuItem("Open");
        save = new JMenuItem("Save");
        exit = new JMenuItem("Exit");
        nuovo = new JMenuItem("Crea Nuovo Puzzle");

        load.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);
        nuovo.addActionListener(this);

        file.add(nuovo);
        file.add(load);
        file.add(save);
        file.add(exit);

        frame.setJMenuBar(bar);
        JPanel command = new JPanel();

        pannelloPrincipale.add(command, BorderLayout.SOUTH);

        risolvi = new JButton("Risolvi");
        risolvi.setEnabled(false);

        submit = new JButton("Invia soluzione");
        submit.setEnabled(false);

        cancella = new JButton("Cancella tutto");
        cancella.setEnabled(false);

        controllo = new JCheckBox("Abilita controllo vincoli");
        controllo.setEnabled(false);

        save.setEnabled(false);

        command.add(risolvi);
        command.add(controllo);
        command.add(cancella);
        command.add(submit);

        frame.setContentPane(pannelloPrincipale);
    }

    private void createWindow() {
        frame = new JFrame();
        pannelloPrincipale = new JPanel();
        pannelloPrincipale.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIUtil.centra(frame, 500, 500);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit) {
            System.exit(1);
        } else if (e.getSource() == load) {
            try {
                File file;
                JFileChooser fileChooser = new JFileChooser();
                int response = fileChooser.showOpenDialog(null); //select file to open
                if (response == JFileChooser.APPROVE_OPTION) {
                    file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    Puzzle puzzle = FileOperation.createPuzzleFromFile(file);
                    singletonController.setFile(file);
                    singletonController.setPuzzle(puzzle);
                    gestoreStato.transition(new PuzzleCaricato());
                }

            } catch (FileNotFoundException | RuntimeException exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore caricamento file: formato errato", "File errato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore generico", "Errore generico", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == save) {
            JFileChooser fileChooser = new JFileChooser();
            File file;
            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    singletonController.savingFile(file);
                    JOptionPane.showMessageDialog(null, "File salvato correttamente!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore salvataggio file.");
                }
            }

        } else if (e.getSource() == help) {
            JOptionPane.showMessageDialog(null, """
                    Questo software risolve il gioco ufficiale kenken.
                    Ogni puzzle ha una sola soluzione(vedi sito ufficiale)
                    Regole:
                    puoi inserire i numeri da 1 fino alla dimensione del puzzle;
                    puoi inserire un numero se non è gia presente nella stessa riga o colonna;
                    per vincere il totale all'interno di un blocco deve essere ottenuto attraverso l'operazione aritmetica raffigurata tra i numeri inseriti nel blocco""", "Kenken puzzle", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == nuovo) {
            int number = 0;
            String answer;
            do {
                 answer = JOptionPane.showInputDialog("Inserisci la dimensione del puzzle che vuoi creare (da 3 a 9)");
                try {
                    number = Integer.parseInt(answer);
                    if (number < 3 || number > 9)
                        JOptionPane.showMessageDialog(null, "Inserisci dimensione da 1 a 9", null, JOptionPane.WARNING_MESSAGE);
                } catch (NumberFormatException exception) {
                    exception.printStackTrace();
                }
            } while (number < 3 || number > 9);
            new FinestraCreaPuzzle(number);
        }
    }

    public class PuzzleCaricato implements Stato {
        private final int size;
        private final static Map<Point, Color> colori = new HashMap<>();

        public PuzzleCaricato() {
            size = singletonController.getPuzzle().getSize();
            frame.setTitle("Puzzle " + size + " x " + size);
            singletonController.setPlayboard(new Gioco());
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
                    griglia[i][j].setPreferredSize(new Dimension(100, 100));
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
            submit.setEnabled(true);
            save.setEnabled(true);
            createPanel();
            setPuzzleOnPanel();
            singletonController.setDocumentListenerText();
            try {
                singletonController.openBoard();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            frame.pack();
            frame.revalidate();
            frame.repaint();
        }

        @Override
        public void exit() {
            pannelloPrincipale.remove(panelloGriglia);
            risolvi.setEnabled(false);
            controllo.setEnabled(false);
            controllo.setSelected(false);
            cancella.setEnabled(false);
            risolvi.setEnabled(false);
            submit.setEnabled(false);
            save.setEnabled(false);
        }

        private void setPuzzleOnPanel() {
            Set<Cage> cages = singletonController.getPuzzle().getCages();
            for (Cage cage : cages) {
                Color color = UIUtil.getRandColor();
                int target = cage.getTargetNumber();
                Operator operator = cage.getCageOperation();
                String op = getStringByOP(operator);
                Iterator<Point> points = Arrays.stream(cage.getCagePoint()).iterator();
                Point primo = points.next();
                JLabel textArea = new JLabel(target + " " + op);
                textArea.setBackground(color);
                colori.put(primo, color);
                int x = primo.x();
                int y = primo.y();
                griglia[size - x - 1][y].add(textArea, BorderLayout.NORTH);
                griglia[size - x - 1][y].setBackground(color);
                textGriglia[size - x - 1][y].setBackground(color);
                while (points.hasNext()) {
                    Point point = points.next();
                    x = point.x();
                    y = point.y();
                    griglia[size - x - 1][y].setBackground(color);
                    textGriglia[size - x - 1][y].setBackground(color);
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

        public static Color getColor(int x, int y) {
            return colori.get(new Point(x, y));
        }

    }
}
