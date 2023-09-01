package risolutore;

public class Risolutore {
    private RisolviPuzzle risolviPuzzle;

    public Risolutore(RisolviPuzzle risolviPuzzle) {
        if (risolviPuzzle == null) throw new NullPointerException();
        this.risolviPuzzle = risolviPuzzle;
    }

    public RisolviPuzzle getRisolviPuzzle() {
        return risolviPuzzle;
    }

    public void risolviKenken() {
        risolviPuzzle.risolvi();
    }
}
