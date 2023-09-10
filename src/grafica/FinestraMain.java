package grafica;

import mediator.SingletonController;

import javax.swing.*;
import java.awt.*;
/**
 * <p>Questa classe implementa la finestra principale del software</p>
 */
public class FinestraMain {
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

    public FinestraMain() {
        createWindow();
        createButton();
        SingletonController controller = SingletonController.CONTROLLER;
        controller.setFinestraMain(this);
        controller.addListenerToFinestraMainButton();
        frame.pack();
        frame.setVisible(true);
    }

    public JMenuItem getLoad() {
        return load;
    }

    public JMenuItem getSave() {
        return save;
    }

    public JMenuItem getExit() {
        return exit;
    }

    public JMenuItem getHelp() {
        return help;
    }

    public JMenuItem getNuovo() {
        return nuovo;
    }

    public JButton getRisolvi() {
        return risolvi;
    }

    public JCheckBox getControllo() {
        return controllo;
    }

    public JButton getCancella() {
        return cancella;
    }

    public JButton getSubmit() {
        return submit;
    }

    public JPanel getPannelloPrincipale() {
        return pannelloPrincipale;
    }

    public void setPanelloGriglia(JPanel panelloGriglia) {
        this.panelloGriglia = panelloGriglia;
    }
    public JPanel getPanelloGriglia() {
        return panelloGriglia;
    }

    public void setGriglia(JPanel[][] griglia) {
        this.griglia = griglia;
    }

    public void setTextGriglia(JTextField[][] textGriglia) {
        this.textGriglia = textGriglia;
    }

    public JPanel[][] getGriglia() {
        return griglia;
    }

    public JTextField[][] getTextGriglia() {
        return textGriglia;
    }

    private void createWindow() {
        frame = new JFrame();
        pannelloPrincipale = new JPanel();
        pannelloPrincipale.setLayout(new BorderLayout());
        frame.setContentPane(pannelloPrincipale);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIUtil.centra(frame, 500, 500);
    }

    private void createButton() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        bar.add(file);

        JMenu helpM = new JMenu("Help");
        help = new JMenuItem("Help");
        helpM.add(help);
        bar.add(helpM);

        load = new JMenuItem("Open");
        save = new JMenuItem("Save");
        exit = new JMenuItem("Exit");
        nuovo = new JMenuItem("Crea Nuovo Puzzle");


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
    }

    public void refresh(){
        frame.repaint();
        frame.revalidate();
        frame.pack();
    }

}
