package mediator;

import giocare.Gioco;
import grafica.Caricamento;
import grafica.FinestraCreaPuzzle;
import grafica.FinestraMain;
import grafica.UIUtil;
import griglia.Point;
import griglia.*;
import risolutore.Risolutore;
import risolutore.RisolutoreBacktracking;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * Questa classe viene utilizzata come mediatore tra i vari oggetti. Implementa il design pattern mediator permettendo di gestire la separazione tra modello e interfaccia grafica.
 * </p>
 */
public enum SingletonController implements ActionListener {
    CONTROLLER;

    private FinestraMain finestraMain;
    private File fileCaricato;
    private Puzzle puzzle;
    private Gioco gioco;
    private boolean buttonPressed;
    private final static Map<Point, Color> colori = new HashMap<>();

    public static Color getColor(int x, int y) {
        return colori.get(new Point(x, y));
    }

    public FinestraMain getFinestraMain() {
        return finestraMain;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setFinestraMain(FinestraMain finestraMain) {
        this.finestraMain = finestraMain;
    }

    public void addListenerToFinestraMainButton() {
        finestraMain.getLoad().addActionListener(this);
        finestraMain.getSave().addActionListener(this);
        finestraMain.getHelp().addActionListener(this);
        finestraMain.getNuovo().addActionListener(this);
        finestraMain.getExit().addActionListener(this);
        finestraMain.getRisolvi().addActionListener(this);
        finestraMain.getCancella().addActionListener(this);
        finestraMain.getControllo().addActionListener(this);
        finestraMain.getSubmit().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == finestraMain.getExit()) {
            System.exit(0);
        } else if (e.getSource() == finestraMain.getHelp()) {
            JOptionPane.showMessageDialog(null, """
                    Questo software risolve il gioco ufficiale kenken.
                    Ogni puzzle ha una sola soluzione(vedi sito ufficiale)
                    Regole:
                    puoi inserire i numeri da 1 fino alla dimensione del puzzle;
                    puoi inserire un numero se non è gia presente nella stessa riga o colonna;
                    per vincere il totale all'interno di un blocco deve essere ottenuto attraverso l'operazione aritmetica raffigurata tra i numeri inseriti nel blocco""", "Kenken puzzle", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == finestraMain.getSave()) {
            JFileChooser fileChooser = new JFileChooser();
            File file;
            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    savingFile(file);
                    JOptionPane.showMessageDialog(null, "File salvato correttamente!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore salvataggio file.");
                }
            }
        } else if (e.getSource() == finestraMain.getLoad()) {
            try {
                JFileChooser fileChooser = new JFileChooser();
                int response = fileChooser.showOpenDialog(null); //select file to open
                if (response == JFileChooser.APPROVE_OPTION) {
                    this.fileCaricato = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    this.puzzle = FileOperation.createPuzzleFromFile(fileCaricato);
                    this.gioco = new Gioco();
                    if (finestraMain.getPanelloGriglia() != null) {
                        finestraMain.getPannelloPrincipale().remove(finestraMain.getPanelloGriglia());
                    }
                    colori.clear();
                    abilitareComandi();
                    createPanel();
                    setPuzzleOnPanel();
                    setDocumentListenerText();
                    try {
                        openBoard();
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                    finestraMain.refresh();
                }

            } catch (FileNotFoundException | RuntimeException exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore caricamento file: formato errato", "File errato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore generico", "Errore generico", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == finestraMain.getNuovo()) {
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
        } else if (e.getSource() == finestraMain.getSubmit()) {
            boolean haiVinto = gioco.getVittoria();
            if (haiVinto) JOptionPane.showMessageDialog(null, "Hai vinto!!!", "Complimenti", JOptionPane.PLAIN_MESSAGE);
            else JOptionPane.showMessageDialog(null, "Prova ancora...", "Hai Sbagliato", JOptionPane.PLAIN_MESSAGE);
        } else if (e.getSource() == finestraMain.getCancella()) {
            JTextField[][] textGriglia = finestraMain.getTextGriglia();
            int size = puzzle.getSize();
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < size; ++j) {
                    textGriglia[i][j].setText("");
                }
            }
        } else if (e.getSource() == finestraMain.getRisolvi()) {
            SwingUtilities.invokeLater(() -> new Caricamento(new Risolutore(new RisolutoreBacktracking(puzzle))));
        } else if (finestraMain.getControllo().isSelected()) {
            buttonPressed = true;
            verificaInseribilita();
        } else if (!finestraMain.getControllo().isSelected()) {
            buttonPressed = false;
        }
    }

    private void savingFile(File file) throws IOException {
        FileOperation.savePuzzle(file, puzzle, gioco);
    }

    private void verificaInseribilita() {
        gioco.inseribile();
    }

    private void setPuzzleOnPanel() {
        Set<Cage> cages = puzzle.getCages();
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
            int size = puzzle.getSize();
            finestraMain.getGriglia()[size - x - 1][y].add(textArea, BorderLayout.NORTH);
            finestraMain.getGriglia()[size - x - 1][y].setBackground(color);
            finestraMain.getTextGriglia()[size - x - 1][y].setBackground(color);
            while (points.hasNext()) {
                Point point = points.next();
                x = point.x();
                y = point.y();
                finestraMain.getGriglia()[size - x - 1][y].setBackground(color);
                finestraMain.getTextGriglia()[size - x - 1][y].setBackground(color);
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


    private void createPanel() {
        JPanel panelloGriglia = new JPanel();
        int size = puzzle.getSize();
        panelloGriglia.setLayout(new GridLayout(size, size));
        JTextField[][] textGriglia = new JTextField[size][size];
        JPanel[][] griglia = new JPanel[size][size];
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
        finestraMain.setGriglia(griglia);
        finestraMain.setTextGriglia(textGriglia);
        finestraMain.setPanelloGriglia(panelloGriglia);
        finestraMain.getPannelloPrincipale().add(panelloGriglia, BorderLayout.CENTER);
    }

    private void setDocumentListenerText() {
        int size = puzzle.getSize();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                finestraMain.getTextGriglia()[i][j].getDocument().addDocumentListener(new MyDocumentListener(i, j));
            }
        }
    }

    private class MyDocumentListener implements DocumentListener {

        private final int i, j;

        public MyDocumentListener(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            controlNumber();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (finestraMain.getTextGriglia()[i][j].getText().isEmpty())
                setNumberInBoard(gioco.getSize() - i - 1, j, null);
            System.out.println(Arrays.deepToString(gioco.getBoard()));
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            controlNumber();
        }

        private void controlNumber() {
            if (!finestraMain.getTextGriglia()[i][j].getText().matches("\\d")) warning();
            else if (finestraMain.getTextGriglia()[i][j].getText().matches("\\d") && (Integer.parseInt(finestraMain.getTextGriglia()[i][j].getText()) < 1 || Integer.parseInt(finestraMain.getTextGriglia()[i][j].getText()) > puzzle.getSize()))
                warning();
            else {
                Integer x = Integer.parseInt(finestraMain.getTextGriglia()[i][j].getText());
                setNumberInBoard(gioco.getSize() - i - 1, j, x);
                if (buttonPressed) {
                    verificaInseribilita();
                }
            }
            System.out.println(Arrays.deepToString(gioco.getBoard()));
        }

        private void warning() {
            JOptionPane.showMessageDialog(null, "Error: Inserisci numeri tra 1 e la dimensione del puzzle", "Messaggio errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setNumberInBoard(int i, int j, Integer x) {
        gioco.setNumberInBoard(i, j, x);
    }

    private void abilitareComandi() {
        finestraMain.getRisolvi().setEnabled(true);
        finestraMain.getControllo().setEnabled(true);
        finestraMain.getCancella().setEnabled(true);
        finestraMain.getRisolvi().setEnabled(true);
        finestraMain.getSubmit().setEnabled(true);
        finestraMain.getSave().setEnabled(true);
    }

    public void openBoard() throws FileNotFoundException {
        FileOperation.openOnBoard(fileCaricato);
    }

}
