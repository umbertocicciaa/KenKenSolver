package risolutore;

import griglia.Cage;
import griglia.Operator;
import griglia.Point;
import griglia.Puzzle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class Risolutore extends Backtracking<Point, Integer> {
    private final Puzzle puzzle;
    private final Integer[][] board;
    private final int size;
    private final Point ultimoPunto;

    public Risolutore(Puzzle puzzle) {
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
        boolean RepetionInRowColom = presentInRowColom(scelta, puntoDiScelta);
        boolean isPossibleChoiceForCage = possibleChoiceForCage(scelta, puntoDiScelta);
        return !RepetionInRowColom && isPossibleChoiceForCage;
    }

    private boolean possibleChoiceForCage(Integer scelta, Point puntoDiScelta) {
        Cage cage = puzzle.getPointToCage().get(puntoDiScelta);
        Set<Point> points = getPointOfCage(cage);
        Operator operator = cage.getCageOperation();
        int target = cage.getTargetNumber();
        Iterator<Point> iterator = points.iterator();
        switch (operator) {
            case SUM -> {
                int sum = 0;
                while (iterator.hasNext()) {
                    Point point = iterator.next();
                    if (board[point.getX()][point.getY()] != null)
                        sum += board[point.getX()][point.getY()];
                }
                if (sum + scelta <= target)
                    return true;
            }
            case MUL -> {
                int product = 1;
                while (iterator.hasNext()) {
                    Point point = iterator.next();
                    if (board[point.getX()][point.getY()] != null)
                        product *= board[point.getX()][point.getY()];
                }
                if (product * scelta <= target)
                    return true;
            }
            case SUB -> {
                boolean primo = true;
                int sub = 0;
                while (iterator.hasNext()) {
                    Point point = iterator.next();
                    if (board[point.getX()][point.getY()] != null && primo) {
                        sub = board[point.getX()][point.getY()];
                        primo = false;
                    } else {
                        if (board[point.getX()][point.getY()] != null)
                            sub -= board[point.getX()][point.getY()];
                    }
                }
                if (sub - scelta >= target)
                    return true;
            }
            case DIV -> {
                boolean primo = true;
                int div = 0;
                while (iterator.hasNext()) {
                    Point point = iterator.next();
                    if (board[point.getX()][point.getY()] != null && primo) {
                        div = board[point.getX()][point.getY()];
                        primo = false;
                    } else {
                        if (board[point.getX()][point.getY()] != null)
                            div /= board[point.getX()][point.getY()];
                    }
                }
                if (div / scelta >= target)
                    return true;
            }
            case NONE -> {
                if (scelta == target)
                    return true;
            }
        }
        return false;
    }

    private Set<Point> getPointOfCage(Cage cage) {
        Set<Point> points = new HashSet<>();
        for (Point point : puzzle.getPointToCage().keySet())
            if (puzzle.getPointToCage().get(point).equals(cage))
                points.add(point);
        return points;
    }

    private boolean presentInRowColom(Integer scelta, Point puntoDiScelta) {
        int r = puntoDiScelta.getX();
        int c = puntoDiScelta.getY();
        for (Integer x : board[r])
            if (x != null && x.equals(scelta))
                return true;
        for (int i = 0; i < size; ++i)
            if (board[i][c] != null && board[i][c].equals(scelta))
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
