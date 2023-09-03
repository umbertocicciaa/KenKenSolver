package grafica;

import risolutore.Risolutore;

import javax.swing.*;
import java.awt.*;

public class Caricamento extends JFrame {

    public Caricamento(Risolutore risolutore) {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JLabel loading = new JLabel("Loading...");
        this.add(loading);
        centra();
        Task task = new Task(risolutore);
        task.execute();
        this.pack();
        this.setVisible(true);
    }

    private void centra() {
        int larghezzaFinestra = 200;
        int altezzaFinestra = 200;

        this.setSize(larghezzaFinestra, altezzaFinestra);

        Dimension dimensioniSchermo = Toolkit.getDefaultToolkit().getScreenSize();
        int larghezzaSchermo = dimensioniSchermo.width;
        int altezzaSchermo = dimensioniSchermo.height;

        int x = (larghezzaSchermo - larghezzaFinestra) / 2;
        int y = (altezzaSchermo - altezzaFinestra) / 2;
        this.setLocation(x, y);
    }

    private class Task extends SwingWorker<Void, Void> {

        private final Risolutore risolutore;

        public Task(Risolutore risolutore) {
            this.risolutore = risolutore;
        }

        @Override
        protected Void doInBackground() {
            risolutore.risolviKenken();
            return null;
        }

        @Override
        protected void done() {
            new FinestraSoluzione(risolutore.getRisolviPuzzle().getBoard());
            Caricamento.this.dispose();
        }
    }

}
