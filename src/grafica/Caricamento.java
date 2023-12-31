package grafica;

import risolutore.Risolutore;

import javax.swing.*;

/**
 * <p>Questa classe crea la finestra caricamento che "aspetta" che l'algoritmo venga elaborato. Una volta finita l'elaborazione se la
 * soluzione è stata trovata si crea la finestra contenente la soluzione</p>
 *
 * @see FinestraSoluzione
 */
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


    private class Task extends SwingWorker<Boolean, Void> {

        private final Risolutore risolutore;

        public Task(Risolutore risolutore) {
            this.risolutore = risolutore;
        }

        @Override
        protected Boolean doInBackground() {
            risolutore.risolviKenken();
            return risolutore.trovataSoluzione();
        }


        @Override
        protected void done() {
            if (doInBackground()) {
                java.awt.EventQueue.invokeLater(() -> new FinestraSoluzione(risolutore.risolviPuzzle().getBoard()));
                frame.dispose();
            } else {
                frame.dispose();
                JOptionPane.showMessageDialog(null, "La soluzione non è stata trovata", "Soluzione non trovata", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

}
