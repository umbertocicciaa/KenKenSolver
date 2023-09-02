package grafica;

import giocare.Gioco;
import griglia.FileOperation;
import griglia.Puzzle;
import risolutore.Risolutore;
import risolutore.RisolutoreBacktracking;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public enum SingletonController implements ActionListener {
    CONTROLLER;
    private GestoreFinestra gestoreFinestra;
    private Puzzle puzzle;
    private Gioco gioco;
    private int size;

    private File fileOpened;

    public void setFile(File file) {
        this.fileOpened = file;
    }

    public File getFileOpened() {
        return fileOpened;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        if (puzzle == null)
            throw new NullPointerException();
        this.size = puzzle.getSize();
        this.puzzle = puzzle;
    }

    public GestoreFinestra getGestoreFinestra() {
        return gestoreFinestra;
    }

    public void setGestoreFinestra(GestoreFinestra gestoreFinestra) {
        this.gestoreFinestra = gestoreFinestra;
        gestoreFinestra.getCancella().addActionListener(this);
        gestoreFinestra.getRisolvi().addActionListener(this);
        gestoreFinestra.getSubmit().addActionListener(this);
    }

    public void setPlayboard(Gioco gioco) {
        if (gioco == null)
            throw new NullPointerException();
        this.gioco = gioco;
    }

    public void setNumberInBoard(int i, int j, Integer x) {
        gioco.setNumberInBoard(i, j, x);
    }

    public void setDocumentListenerText() {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                gestoreFinestra.getTextGriglia()[i][j].getDocument().addDocumentListener(new MyDocumentListener(i, j));
            }
        }
    }

    private class MyDocumentListener implements DocumentListener {

        private int i, j;

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
            if (gestoreFinestra.getTextGriglia()[i][j].getText().equals(""))
                setNumberInBoard(gioco.getSize() - i - 1, j, null);
            System.out.println(Arrays.deepToString(gioco.getBoard()));
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            controlNumber();
        }

        private void controlNumber() {
            if (!gestoreFinestra.getTextGriglia()[i][j].getText().matches("\\d"))
                warning();
            else if (gestoreFinestra.getTextGriglia()[i][j].getText().matches("\\d") && (Integer.parseInt(gestoreFinestra.getTextGriglia()[i][j].getText()) < 1 ||
                    Integer.parseInt(gestoreFinestra.getTextGriglia()[i][j].getText()) > puzzle.getSize()))
                warning();
            else {
                Integer x = Integer.parseInt(gestoreFinestra.getTextGriglia()[i][j].getText());
                setNumberInBoard(gioco.getSize() - i - 1, j, x);
            }
            System.out.println(Arrays.deepToString(gioco.getBoard()));
        }

        private void warning() {
            JOptionPane.showMessageDialog(null,
                    "Error: Inserisci numeri tra 1 e la dimensione del puzzle", "Messaggio errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gestoreFinestra.getCancella()) {
            JTextField[][] textGriglia = gestoreFinestra.getTextGriglia();
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < size; ++j) {
                    textGriglia[i][j].setText("");
                }
            }
        } else if (e.getSource() == gestoreFinestra.getRisolvi()) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new Caricamento(new Risolutore(new RisolutoreBacktracking(puzzle)));
                }
            });
        } else if (e.getSource() == gestoreFinestra.getSubmit()) {
            boolean haiVinto = gioco.getVittoria();
            if (haiVinto)
                JOptionPane.showMessageDialog(null, "Hai vinto!!!", "Complimenti", JOptionPane.PLAIN_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Prova ancora...", "Hai Sbagliato", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void savingFile(File file) throws IOException {
        FileOperation.savePuzzle(file, puzzle, gioco);
    }

    public void openBoard() throws FileNotFoundException {
        FileOperation.openOnBoard();
    }


}
