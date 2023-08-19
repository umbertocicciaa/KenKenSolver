package griglia;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Puzzle {
    private final int size;
    private final Set<Cage> cages;

    private Puzzle(PuzzleBuilder builder) {
        this.size = builder.size;
        this.cages = Collections.unmodifiableSet(builder.cages);
    }

    public static class PuzzleBuilder {
        private int size;
        private Set<Cage> cages;

        private int [][]board;

        public PuzzleBuilder(int size) {
            if (size < 3 || size > 9)
                throw new IllegalArgumentException("Dimensioni puzzle scorrette");
            this.size = size;
            cages = new HashSet<>();
        }

        public PuzzleBuilder addCageToPuzzle(int target, Operator operator, Point... points) {
            cages.add(new Cage(target, operator, points));
            return this;
        }

        public Puzzle build() {
            return new Puzzle(this);
        }

    }

}
