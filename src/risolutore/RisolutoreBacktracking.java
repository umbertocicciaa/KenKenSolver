package risolutore;

import griglia.Cage;
import griglia.Operator;
import griglia.Point;
import griglia.Puzzle;

import java.util.*;

public final class RisolutoreBacktracking extends Backtracking<Point, Integer> {
    private final Puzzle puzzle;
    private final Integer[][] board;
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
        this.ultimoPunto = new Point(size - 1, size - 1);
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
        boolean obbiettivoVerificato = verificaObiettivo(puzzle.getPointToCage().get(puntoDiScelta), scelta);
        return !presente && obbiettivoVerificato && board[puntoDiScelta.getX()][puntoDiScelta.getY()] == null;
    }

    private boolean verificaObiettivo(Cage cage, Integer scelta) {
        List<Integer> valori = new ArrayList<>();
        Point[] points = cage.getCagePoint();
        for (Point p : points)
            if (board[p.getX()][p.getY()] != null)
                valori.add(board[p.getX()][p.getY()]);
        int target = cage.getTargetNumber();
        switch (cage.getCageOperation()) {
            case SUM -> {
                int sum = 0;
                for (Integer x : valori)
                    sum += x;
                return sum + scelta <= target;
            }
            case MUL -> {
                int mul = 1;
                for (Integer x : valori)
                    mul *= x;
                return mul * scelta <= target;
            }
            case SUB -> {
                Point p1 = points[0], p2 = points[1];
                Integer v1 = board[p1.getX()][p1.getY()], v2 = board[p2.getX()][p2.getY()];
                if (v1 == null && v2 == null)
                    return true;
                if (v1 == null) {
                    return Math.abs(v2 - scelta) == target;
                }
                return Math.abs(v1 - scelta) == target;
            }
            case DIV -> {
                Point p1 = points[0], p2 = points[1];
                Integer v1 = board[p1.getX()][p1.getY()], v2 = board[p2.getX()][p2.getY()];
                if (v1 == null && v2 == null)
                    return true;
                if (v1 == null) {
                    if (v2 % scelta == 0)
                        return v2 / scelta == target;
                    return false;
                }
                if (v1 % scelta == 0)
                    return v1 / scelta == target;
                return false;
            }
            case NONE -> {
                return scelta == target;
            }
        }
        return false;
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
        System.out.println(print);
    }
}
