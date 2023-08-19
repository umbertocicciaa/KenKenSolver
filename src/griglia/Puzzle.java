package griglia;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Puzzle {
    private final int size;
    private final Set<Cage> cages;
    private Integer[][] board;

    private Puzzle(PuzzleBuilder builder) {
        if (builder == null)
            throw new NullPointerException("Hai passato un builder null");
        this.size = builder.size;
        this.cages = Collections.unmodifiableSet(builder.cages);
        this.board = builder.board;
    }

    public int getSize() {
        return size;
    }

    public static class PuzzleBuilder {
        private int size;
        private Set<Cage> cages;
        private Integer[][] board;

        public PuzzleBuilder(int size) {
            if (size < 3 || size > 9)
                throw new IllegalArgumentException("Dimensioni puzzle scorrette");
            this.size = size;
            cages = new HashSet<>();
            board = new Integer[size][size];
        }

        public PuzzleBuilder addCageToPuzzle(int target, Operator operator, Point... points) {
            cages.add(new Cage(target, operator, points));
            return this;
        }

        public PuzzleBuilder addNumberToPoint(int number, Point point) {
            int x = point.getX();
            int y = point.getY();
            if (board[x][y] != null)
                throw new RuntimeException("Cella gia occupata: ripeti costruzione");
            board[x][y] = number;
            return this;
        }

        public Puzzle build() {
            return new Puzzle(this);
        }

    }

}
