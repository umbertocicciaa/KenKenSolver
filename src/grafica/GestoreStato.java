package grafica;

/**<p>Questa classe permette di gestire stati; Il metodo {@code transition} permette di attuare il cambio stato</p>
 * @see Stato*/
public abstract class GestoreStato {
    protected Stato corrente;
    public void transition(Stato state) {
        if (corrente != null) {
            corrente.exit();
        }
        corrente = state;
        state.entry();
    }
}
