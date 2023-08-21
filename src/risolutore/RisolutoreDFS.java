package risolutore;

import griglia.Cage;
import griglia.Operator;
import griglia.Point;
import griglia.Puzzle;

import java.util.Arrays;

public class RisolutoreDFS implements RisolviPuzzle {
    private Puzzle puzzle;
    private Integer[][] board;
    private Point endPoint;
    private int size;

    public RisolutoreDFS(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.size = puzzle.getSize();
        this.board = new Integer[size][size];
        this.endPoint = new Point(size - 1, size - 1);
    }

    @Override
    public void risolvi() {
        solveRec(board, new Point(0, 0));
    }

    private void scriviSoluzione() {
        System.out.println(Arrays.deepToString(board));
    }

    private boolean solveRec(Integer[][] board, Point index) {
        if (board[endPoint.getX()][endPoint.getY()] != null) {
            scriviSoluzione();
            return true;
        }
        for (int i = 1; i <= size; i++) {
            if (solvable(i, index)) {
                board[index.getX()][index.getY()] = i;
                Point indexAdvanced = incrementIndex(index);
                boolean temp = solveRec(board, indexAdvanced);
                if (temp)
                    return true;
                else {
                    board[index.getX()][index.getY()] = null;
                }
            }
        }
        return false;
    }

    private boolean solvable(int value, Point index) {
        int row = index.getX();
        int col = index.getY();
        Cage cage = puzzle.getPointToCage().get(index);
        int filledPositions = 1;
        boolean cagePositionEqualsOne = false;
        for (Point p : cage.getCagePoint()) {
            if (board[p.getX()][p.getY()] != null)
                filledPositions++;
            if (board[p.getX()][p.getY()] != null && board[p.getX()][p.getY()] == 1)
                cagePositionEqualsOne = true;
        }
        Integer cageTotalWithValue = getCageTotalWithValue(value, cage);
        if (!presenteInRigaColonna(value, new Point(row, col))) {
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

    private Integer getCageTotalWithValue(int value, Cage cage) {
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

    private Point incrementIndex(Point index) {
        int row = index.getX();
        int col = index.getY();
        if (col == size - 1) {
            row++;
            col = 0;
        } else {
            col++;
        }
        return new Point(row, col);
    }


}
