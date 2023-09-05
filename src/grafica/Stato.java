package grafica;
/**<p>Questa interfaccia rappresenta il concetto di stato</p>*/
public interface Stato {
    /**<p>Questo metodo viene eseguito appena si entra in questo stato</p>*/
    default void entry() {
    }
    /**<p>Questo metodo viene eseguito appena si esce da questo stato</p>*/
    default void exit() {
    }
}
