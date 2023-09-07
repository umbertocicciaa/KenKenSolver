package griglia;

import java.util.*;

/**
 * <p>Questa classe è il cuore del software. Rappresenta il concetto di puzzle kenken</p>
 */
public class Puzzle {
    private final int size;
    private final Set<Cage> cages;
    /**
     * <p>Quesa variabile {@code pointToCage}associa ad ogni punto del puzzle il corrispettivo cage di cui fa parte</p>
     */
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

    /**
     * <p>Questa classe attraverso il design pattern Builder permette la creazione di un puzzle "per parti" a runtime</p>
     */
    public static class PuzzleBuilder {
        private final int size;
        private final Set<Cage> cages;
        private final Map<Point, Cage> pointToCage;

        public PuzzleBuilder(int size) {
            if (size < 3 || size > 9)
                throw new IllegalArgumentException("Dimensioni puzzle scorrette");
            this.size = size;
            cages = new HashSet<>();
            pointToCage = new HashMap<>();
        }

        /**
         * @throws RuntimeException eccezzione lanciata se un punto gia appartiene ad un altro cage
         */
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

        public PuzzleBuilder addCageToPuzzle(Cage cage) {
            cages.add(cage);
            for (Point point : cage.getCagePoint()) {
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

        /**
         * <p>Questo metodo controlla se tutti i punti fanno parte del puzzle</p>
         *
         * @throws RuntimeException eccezzione lanciata se alcune celle non sono state inserite nei cage
         */
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
