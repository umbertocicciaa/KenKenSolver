package griglia;

import java.util.*;

public class Puzzle {
    private final int size;
    private final Set<Cage> cages;
    private final Map<Point, Cage> pointToCage;

    private Puzzle(PuzzleBuilder builder) {
        if (builder == null)
            throw new NullPointerException("Hai passato un builder null");
        this.size = builder.size;
        this.cages = Collections.unmodifiableSet(builder.cages);
        this.pointToCage = Collections.unmodifiableMap(builder.pointToCage);
    }

    public int getSize() {
        return size;
    }

    public Set<Cage> getCages() {
        return cages;
    }

    public Map<Point, Cage> getPointToCage() {
        return pointToCage;
    }

    public static class PuzzleBuilder {
        private int size;
        private Set<Cage> cages;
        private Map<Point, Cage> pointToCage;

        public PuzzleBuilder(int size) {
            if (size < 3 || size > 9)
                throw new IllegalArgumentException("Dimensioni puzzle scorrette");
            this.size = size;
            cages = new HashSet<>();
            pointToCage = new HashMap<>();
        }

        public PuzzleBuilder addCageToPuzzle(int target, Operator operator, Point... points) {
            Cage cage = new Cage(target, operator, points);
            cages.add(cage);
            for (Point point : points) {
                if (pointToCage.containsKey(point))
                    throw new RuntimeException("Il puzzle contiene gia il punto: " + point);
                pointToCage.put(point, cage);
            }
            return this;
        }

        public Puzzle build() {
            checkAllPointAreInCage();
            return new Puzzle(this);
        }

        private void checkAllPointAreInCage() {
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < size; ++j) {
                    Point point = new Point(i, j);
                    if (!pointToCage.containsKey(point))
                        throw new RuntimeException("Alcune celle non sono state inserite nei cage");
                }
            }
        }

    }

    @Override
    public String toString() {
        return "Puzzle{" +
                "size=" + size +
                ", cages=" + cages +
                ", pointToCage=" + pointToCage +
                '}';
    }
}
