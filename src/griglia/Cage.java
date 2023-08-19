package griglia;

import java.util.Arrays;

public class Cage {
    private Point[] cagePoint;
    private int targetNumber;
    private Operator cageOperation;

    public Cage(int targetNumber, Operator operator, Point... points) {
        if ((operator == Operator.DIV || operator == Operator.SUB) && (points.length != 2))
            throw new IllegalArgumentException("Divisone e Sottrazione accettano solo 2 blocchi... vedi regole ufficiali kenken");
        this.targetNumber = targetNumber;
        this.cageOperation = operator;
        this.cagePoint = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cage)) return false;

        Cage cage = (Cage) o;

        if (targetNumber != cage.targetNumber) return false;
        if (cageOperation != cage.cageOperation) return false;
        if (!Arrays.equals(cagePoint, cage.cagePoint)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = targetNumber;
        result = 31 * result + (cageOperation != null ? cageOperation.hashCode() : 0);
        result = 31 * result + (cagePoint != null ? Arrays.hashCode(cagePoint) : 0);
        return result;
    }


}
