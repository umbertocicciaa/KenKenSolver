package grafica;

import javax.swing.*;
import java.awt.*;

/**
 * <p>Questa classe di utilita fornisce metodi utili alla creazione di finestre</p>
 */
public final class UIUtil {
    private UIUtil() {
    }

    /**
     * <p>Questa metodo centra la finestra passata in input</p>
     *
     * @param frame             finestra da centrare
     * @param larghezzaFinestra larghezza che deve avere la finestra
     * @param altezzaFinestra   altezza che deve avere la finestra
     */
    public static void centra(JFrame frame, int larghezzaFinestra, int altezzaFinestra) {
        frame.setSize(larghezzaFinestra, altezzaFinestra);

        Dimension dimensioniSchermo = Toolkit.getDefaultToolkit().getScreenSize();
        int larghezzaSchermo = dimensioniSchermo.width;
        int altezzaSchermo = dimensioniSchermo.height;

        int x = (larghezzaSchermo - larghezzaFinestra) / 2;
        int y = (altezzaSchermo - altezzaFinestra) / 2;
        frame.setLocation(x, y);
    }

    /**
     * <p>Questa metodo genera un colore non troppo scuro</p>
     *
     * @return Color colore generato
     */

    public static Color getRandColor() {
        int r = (int) (Math.random() * 255);
        int g = (int) (Math.random() * 255);
        int b = (int) (Math.random() * 255);

        // assicurati che il colore non sia troppo scuro
        while (r < 128 || g < 128 || b < 128) {
            r = (int) (Math.random() * 255);
            g = (int) (Math.random() * 255);
            b = (int) (Math.random() * 255);
        }
        return new Color(r, g, b);
    }


}
