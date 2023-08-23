package risolutore;

import griglia.Cage;
import griglia.Operator;
import griglia.Point;
import griglia.Puzzle;

import java.util.*;

public final class RisolutoreBacktracking extends Backtracking<Point, Integer> {
    private final Puzzle puzzle;
    private final Integer[][] board;

    private final Integer[][] soluzioneFinale;

    private final int size;
    private final Point ultimoPunto;
    private boolean risolto;

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
    public boolean soluzioneTrovata() {
        return risolto;
    }

    @Override
    protected Point primoPuntoDiScelta() {
        return new Point(0, 0);
    }

    @Override
    protected Point prossimoPuntoDiScelta(Point ps) {
        int x = ps.getX();
        int y = ps.getY();
        if (y == size - 1)
            return new Point(x + 1, 0);
        return new Point(x, y + 1);
    }

    @Override
    protected Point precedentePuntoDiScelta(Point puntoDiScelta) {
        int x = puntoDiScelta.getX();
        int y = puntoDiScelta.getY();
        if (x > 0 && y == 0)
            return new Point(x - 1, size - 1);
        return new Point(x, y - 1);
    }

    @Override
    protected Point ultimoPuntoDiScelta() {
        return ultimoPunto;
    }

    @Override
    protected boolean assegnabile(Integer scelta, Point puntoDiScelta) {
        boolean presente = presenteInRigaColonna(scelta, puntoDiScelta);
        Integer cageTotalWithValue = verificaObiettivo(puzzle.getPointToCage().get(puntoDiScelta), scelta);
        int filledPositions = 1;
        boolean cagePositionEqualsOne = false;
        Cage cage = puzzle.getPointToCage().get(puntoDiScelta);
        for (Point p : cage.getCagePoint()) {
            if (board[p.getX()][p.getY()] != null)
                filledPositions++;
            if (board[p.getX()][p.getY()] != null && board[p.getX()][p.getY()] == 1)
                cagePositionEqualsOne = true;
        }
        if (!presente) {
            if (cageTotalWithValue != null && cageTotalWithValue == cage.getTargetNumber() && filledPositions == cage.getCagePoint().length)
                return true;
            else if (filledPositions < cage.getCagePoint().length) {
                if (cageTotalWithValue != null && cageTotalWithValue < cage.getTargetNumber())
                    return true;
                else if (cageTotalWithValue != null && cageTotalWithValue > cage.getTargetNumber() && cage.getCageOperation() == Operator.SUB || cage.getCageOperation() == Operator.DIV)
                    return true;
                else
                    return (cageTotalWithValue != null && cageTotalWithValue == cage.getTargetNumber() && !cagePositionEqualsOne && cage.getCageOperation() == Operator.MUL);
            }
        }
        return false;
    }

    private Integer verificaObiettivo(Cage cage, Integer value) {
        switch (cage.getCageOperation()) {
            case SUM -> {
                for (Point p : cage.getCagePoint())
                    if (board[p.getX()][p.getY()] != null)
                        value += board[p.getX()][p.getY()];
                return value;
            }
            case SUB -> {
                for (Point p : cage.getCagePoint()) {
                    if (board[p.getX()][p.getY()] != null)
                        value -= board[p.getX()][p.getY()];
                    return Math.abs(value);
                }
            }
            case MUL -> {
                for (Point p : cage.getCagePoint()) {
                    if (board[p.getX()][p.getY()] != null)
                        value *= board[p.getX()][p.getY()];
                }
                return value;
            }
            case DIV -> {
                for (Point p : cage.getCagePoint()) {
                    if (board[p.getX()][p.getY()] != null && value % board[p.getX()][p.getY()] == 0)
                        value /= board[p.getX()][p.getY()];
                    return value;
                }
            }
            case NONE -> {
                return value;
            }
        }
        return null;
    }

    private boolean presenteInRigaColonna(Integer scelta, Point puntoDiScelta) {
        int riga = puntoDiScelta.getX();
        int colonna = puntoDiScelta.getY();
        for (Integer x : board[riga])
            if ((x != null) && x.equals(scelta))
                return true;
        for (int i = 0; i < size; ++i)
            if (board[i][colonna] != null && board[i][colonna].equals(scelta))
                return true;
        return false;
    }


    @Override
    protected void assegna(Integer scelta, Point puntoDiScelta) {
        board[puntoDiScelta.getX()][puntoDiScelta.getY()] = scelta;
    }

    @Override
    protected void deassegna(Integer scelta, Point puntoDiScelta) {
        board[puntoDiScelta.getX()][puntoDiScelta.getY()] = null;
    }


    @Override
    protected Integer ultimaSceltaAssegnataA(Point puntoDiScelta) {
        return board[puntoDiScelta.getX()][puntoDiScelta.getY()];
    }

    @Override
    protected Integer primaScelta(Point ps) {
        if (puzzle.getPointToCage().get(ps).getCageOperation() == Operator.NONE)
            return puzzle.getPointToCage().get(ps).getTargetNumber();
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
    protected void scriviSoluzione(int nr_sol) {
        String print = Arrays.deepToString(board);
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                soluzioneFinale[i][j] = board[i][j];
            }
        }
        risolto = true;
        System.out.println(print);
    }
}
