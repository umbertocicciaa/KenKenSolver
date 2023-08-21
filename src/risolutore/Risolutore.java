package risolutore;

import griglia.Puzzle;

public class Risolutore {
    private RisolviPuzzle risolviPuzzle;

    public Risolutore(RisolviPuzzle risolviPuzzle) {
        if (risolviPuzzle == null) throw new NullPointerException();
        this.risolviPuzzle = risolviPuzzle;
    }

    public void risolviKenken() {
        risolviPuzzle.risolvi();
    }
}
