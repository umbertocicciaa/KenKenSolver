package risolutore;

/**
 * <p>Questa interfaccia insieme a {@code Risolutore}, {@code RisolutoreBacktracking} implementa il design pattern strategy
 * per permettere di risolvere il puzzle con algoritmi diversi (nel caso nel futuro si vogliano implementare nuovi algoritmi risolutori)</p>
 *
 * @see Risolutore
 * @see RisolutoreBacktracking
 * @see Backtracking
 */
public interface RisolviPuzzle {
    void risolvi();

    Integer[][] getBoard();

    boolean trovataSoluzione();
}
