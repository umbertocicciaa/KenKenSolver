package griglia;

/**
 * <p>Questa classe rappresenta l'entita punto geometrico, il puzzle è formato infatti da un'insieme di punti uniti in un cage</p>
 *
 * @see Cage
 * @see Puzzle
 */
public record Point(int x, int y) implements Comparable<Point> {
    /**
     * @throws IllegalArgumentException eccezione lanciata se le coordinate fornite sono negative
     */
    public Point {
        if (x < 0 || y < 0)
            throw new IllegalArgumentException("Coordinate errate");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;

        if (x != point.x) return false;
        return y == point.y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int compareTo(Point other) {
        int cmp = this.x - other.x;
        if (cmp != 0) {
            return cmp;
        }
        return this.y - other.y;
    }
}
