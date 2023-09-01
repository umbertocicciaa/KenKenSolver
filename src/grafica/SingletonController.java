package grafica;

import griglia.Puzzle;
import risolutore.Risolutore;
import risolutore.RisolutoreBacktracking;
import risolutore.RisolutoreDFS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public enum SingletonController implements ActionListener {
    CONTROLLER;
    private GestoreFinestra gestoreFinestra;
    private FinestraRisolvi finestraRisolvi;
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

    public void setFinestraRisolvi(FinestraRisolvi finestraRisolvi) {
        this.finestraRisolvi = finestraRisolvi;
        finestraRisolvi.getRisolviBack().addActionListener(this);
        finestraRisolvi.getRisolviDFS().addActionListener(this);
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
            new FinestraRisolvi();
        } else if (e.getSource() == finestraRisolvi.getRisolviDFS()) {
            Risolutore risolutore = new Risolutore(new RisolutoreDFS(puzzle));
            risolutore.risolviKenken();
            new FinestraSoluzione(risolutore.getRisolviPuzzle().getBoard());
            finestraRisolvi.dispose();
        } else if (e.getSource() == finestraRisolvi.getRisolviBack()) {
            Risolutore risolutore = new Risolutore(new RisolutoreBacktracking(puzzle));
            risolutore.risolviKenken();
            new FinestraSoluzione(risolutore.getRisolviPuzzle().getBoard());
            finestraRisolvi.dispose();
        }
    }
}
