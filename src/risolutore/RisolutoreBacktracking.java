package risolutore;

import griglia.Cage;
import griglia.Point;
import griglia.Puzzle;

import java.util.*;

public final class RisolutoreBacktracking extends Backtracking<Point, Integer> {
    private final Puzzle puzzle;
    private final Integer[][] board;
    private final Integer[][] soluzioneFinale;
    private final int size;
    private final Point ultimoPunto;

    public RisolutoreBacktracking(Puzzle puzzle) {
        // https://www.kenkenpuzzle.com/faq#faq-3 una sola soluzione per problema
        super(1);
        if (puzzle == null)
            throw new NullPointerException("Hai fornito un puzzle nullo");
        this.puzzle = puzzle;
        this.size = puzzle.getSize();
        board = new Integer[size][size];
        soluzioneFinale = new Integer[size][size];
        this.ultimoPunto = new Point(size - 1, size - 1);
    }

    public Integer[][] getBoard() {
        return soluzioneFinale;
    }

    @Override
    protected Point primoPuntoDiScelta() {
        return new Point(0, 0);
    }

    @Override
    protected Point prossimoPuntoDiScelta(Point ps) {
        int x = ps.x();
        int y = ps.y();
        if (y == size - 1)
            return new Point(x + 1, 0);
        return new Point(x, y + 1);
    }

    @Override
    protected Point precedentePuntoDiScelta(Point puntoDiScelta) {
        int x = puntoDiScelta.x();
        int y = puntoDiScelta.y();
        if (x > 0 && y == 0)
            return new Point(x - 1, size - 1);
        return new Point(x, y - 1);
    }

    @Override
    protected Point ultimoPuntoDiScelta() {
        return ultimoPunto;
    }

    @Override
    protected void assegna(Integer scelta, Point puntoDiScelta) {
        board[puntoDiScelta.x()][puntoDiScelta.y()] = scelta;
    }

    @Override
    protected void deassegna(Integer scelta, Point puntoDiScelta) {
        board[puntoDiScelta.x()][puntoDiScelta.y()] = null;
    }


    @Override
    protected Integer ultimaSceltaAssegnataA(Point puntoDiScelta) {
        return board[puntoDiScelta.x()][puntoDiScelta.y()];
    }

    @Override
    protected Integer primaScelta(Point ps) {
        return 1;
    }

    @Override
    protected Integer prossimaScelta(Integer integer) {
        return integer + 1;
    }

    @Override
    protected Integer ultimaScelta(Point ps) {
        return size;
    }

    @Override
    protected boolean assegnabile(Integer scelta, Point puntoDiScelta) {
        boolean presente = presenteInRigaColonna(scelta, puntoDiScelta);
        Cage cage = puzzle.getPointToCage().get(puntoDiScelta);
        boolean inseribile = inseribileSceltaPoint(cage, scelta);
        return !presente && inseribile;
    }

    private boolean inseribileSceltaPoint(Cage cage, Integer scelta) {
        int tot = scelta, assegnati = 1;
        Point[] points = cage.getCagePoint();
        switch (cage.getCageOperation()) {
            case SUM -> {
                for (Point point : points) {
                    int x = point.x(), y = point.y();
                    if (board[x][y] != null) {
                        tot += board[x][y];
                        assegnati++;
                    }
                }
                if (tot > cage.getTargetNumber())
                    return false;
                if (assegnati == points.length && tot != cage.getTargetNumber())
                    return false;
            }
            case MUL -> {
                for (Point point : points) {
                    int x = point.x(), y = point.y();
                    if (board[x][y] != null) {
                        tot *= board[x][y];
                        assegnati++;
                    }
                }
                if (tot > cage.getTargetNumber())
                    return false;
                if (assegnati == points.length && tot != cage.getTargetNumber())
                    return false;
            }
            case SUB -> {
                for (Point point : points) {
                    int x = point.x(), y = point.y();
                    if (board[x][y] != null) {
                        if (tot < board[x][y])
                            tot = board[x][y] - tot;
                        else
                            tot -= board[x][y];
                        assegnati++;
                    }
                    if (assegnati == points.length && tot != cage.getTargetNumber()) {
                        return false;
                    }
                }
            }
            case DIV -> {
                double t = (double) tot;
                for (Point point : points) {
                    int x = point.x(), y = point.y();
                    if (board[x][y] != null) {
                        int number = board[x][y];
                        double v = (double) number;
                        if (t < v) {
                            t = v / t;
                        } else {
                            t /= v;
                        }
                        assegnati++;
                    }
                } // for
                if (assegnati == points.length && t != (double) cage.getTargetNumber()) {
                    return false;
                }
            }
            case NONE -> {
                if (scelta != cage.getTargetNumber())
                    return false;
            }
        }
        return true;
    }


    private boolean presenteInRigaColonna(Integer scelta, Point puntoDiScelta) {
        int riga = puntoDiScelta.x();
        int colonna = puntoDiScelta.y();
        for (Integer x : board[riga])
            if ((x != null) && x.equals(scelta))
                return true;
        for (int i = 0; i < size; ++i)
            if (board[i][colonna] != null && board[i][colonna].equals(scelta))
                return true;
        return false;
    }

    @Override
    protected void scriviSoluzione(int nr_sol) {
        String print = Arrays.deepToString(board);
        for (int i = 0; i < size; ++i) {
            System.arraycopy(board[i], 0, soluzioneFinale[i], 0, size);
        }
        System.out.println(print);
    }
}