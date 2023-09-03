package griglia;

public record Point(int x, int y) {
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
}
