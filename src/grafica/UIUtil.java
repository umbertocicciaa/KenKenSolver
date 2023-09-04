package grafica;

import javax.swing.*;
import java.awt.*;

public final class UIUtil {
    private UIUtil() {

    }

    public static void centra(JFrame frame, int larghezzaFinestra, int altezzaFinestra) {
        frame.setSize(larghezzaFinestra, altezzaFinestra);

        Dimension dimensioniSchermo = Toolkit.getDefaultToolkit().getScreenSize();
        int larghezzaSchermo = dimensioniSchermo.width;
        int altezzaSchermo = dimensioniSchermo.height;

        int x = (larghezzaSchermo - larghezzaFinestra) / 2;
        int y = (altezzaSchermo - altezzaFinestra) / 2;
        frame.setLocation(x, y);
    }

}
