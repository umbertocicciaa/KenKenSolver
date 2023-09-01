package grafica;

import griglia.Puzzle;
import risolutore.Risolutore;
import risolutore.RisolutoreBacktracking;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public enum SingletonController implements ActionListener {
    CONTROLLER;
    private GestoreFinestra gestoreFinestra;
    private Puzzle puzzle;

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void setGestoreFinestra(GestoreFinestra gestoreFinestra) {
        this.gestoreFinestra = gestoreFinestra;
        gestoreFinestra.getCancella().addActionListener(this);
        gestoreFinestra.getRisolvi().addActionListener(this);
    }

    public void setDocumentListenerText() {
        for (int i = 0; i < gestoreFinestra.getTextGriglia().length; ++i) {
            for (int j = 0; j < gestoreFinestra.getTextGriglia()[i].length; ++j) {
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
            if (!gestoreFinestra.getTextGriglia()[i][j].getText().matches("\\d"))
                warning();
            else if (gestoreFinestra.getTextGriglia()[i][j].getText().matches("\\d") && (Integer.parseInt(gestoreFinestra.getTextGriglia()[i][j].getText()) < 1 ||
                    Integer.parseInt(gestoreFinestra.getTextGriglia()[i][j].getText()) > puzzle.getSize()))
                warning();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {

        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            if (!gestoreFinestra.getTextGriglia()[i][j].getText().matches("\\d"))
                warning();
            else if (gestoreFinestra.getTextGriglia()[i][j].getText().matches("\\d") && (Integer.parseInt(gestoreFinestra.getTextGriglia()[i][j].getText()) < 1 ||
                    Integer.parseInt(gestoreFinestra.getTextGriglia()[i][j].getText()) > puzzle.getSize()))
                warning();

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
            int size = textGriglia.length;
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
        }
    }
}
