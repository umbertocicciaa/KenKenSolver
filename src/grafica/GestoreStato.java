package grafica;

public abstract class GestoreStato {
    protected Stato corrente;
    public void transition(Stato state) {
        if(corrente != null) {
            corrente.exit();
        }
        corrente = state;
        state.entry();
    }
}
