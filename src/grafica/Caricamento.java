package grafica;

import risolutore.Risolutore;

import javax.swing.*;

public class Caricamento {

    private final JFrame frame;

    public Caricamento(Risolutore risolutore) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JLabel loading = new JLabel("Loading...");
        frame.add(loading);
        UIUtil.centra(frame, 200, 200);
        Task task = new Task(risolutore);
        task.execute();
        frame.pack();
        frame.setVisible(true);
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
            frame.dispose();
        }
    }

}
