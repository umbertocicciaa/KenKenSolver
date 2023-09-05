package risolutore;

/**
 * <p>Questa classe insieme a {@code RisolviPuzzle}, {@code RisolutoreBacktracking} implementa il design pattern strategy
 * per permettere di risolvere il puzzle con algoritmi diversi (nel caso nel futuro si vogliano implementare nuovi algoritmi risolutori)</p>
 *
 * @see RisolviPuzzle
 * @see RisolutoreBacktracking
 * @see Backtracking
 */
public record Risolutore(RisolviPuzzle risolviPuzzle) {
    public Risolutore {
        if (risolviPuzzle == null) throw new NullPointerException();
    }

    public void risolviKenken() {
        risolviPuzzle.risolvi();
    }

    public boolean trovataSoluzione() {
        return risolviPuzzle.trovataSoluzione();
    }
}
