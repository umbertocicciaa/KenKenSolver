package grafica;

import risolutore.Risolutore;

import javax.swing.*;

public class Caricamento extends JFrame {

    public Caricamento(Risolutore risolutore) {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JLabel loading = new JLabel("Loading...");
        this.add(loading);
        UIUtil.centra(this, 200, 200);
        Task task = new Task(risolutore);
        task.execute();
        this.pack();
        this.setVisible(true);
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
            new FinestraSoluzione(risolutore.risolviPuzzle().getBoard());
            Caricamento.this.dispose();
        }
    }

}
