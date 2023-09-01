package grafica;

import risolutore.Risolutore;

import javax.swing.*;
import java.awt.*;
import java.awt.image.renderable.RenderableImage;
import java.util.List;

public class Caricamento extends JFrame {
    private JProgressBar bar;
    public Caricamento(Risolutore risolutore) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bar = new JProgressBar(0, 100);
        this.add(bar);
        centra();
        Task task = new Task(risolutore);
        task.execute();
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

        private Risolutore risolutore;

        public Task(Risolutore risolutore) {
            this.risolutore = risolutore;
        }

        @Override
        protected Void doInBackground() throws Exception {
            risolutore.risolviKenken();
            publish();
            return null;
        }

        @Override
        protected void process(List<Void> chunks) {
            bar.setValue(bar.getValue() + 1);
        }

        @Override
        protected void done() {
            new FinestraSoluzione(risolutore.getRisolviPuzzle().getBoard());
            Caricamento.this.dispose();
        }
    }

}
