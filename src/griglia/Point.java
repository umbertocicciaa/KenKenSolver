package griglia;

public class Point {

    private int x;
    private int y;

    private int number;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
