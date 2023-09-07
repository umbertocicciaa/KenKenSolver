package griglia;

import java.util.Arrays;

/**
 * <p>Questa classe rappresenta un cage del puzzle kenken</p>
 *
 * @see Puzzle
 */
public class Cage {
    private final Point[] cagePoint;
    private final int targetNumber;
    private final Operator cageOperation;


    public Point[] getCagePoint() {
        return cagePoint;
    }

    public int getTargetNumber() {
        return targetNumber;
    }


    public Operator getCageOperation() {
        return cageOperation;
    }

    /**
     * @throws IllegalArgumentException vincoli non soddisfatti
     */
    public Cage(int targetNumber, Operator operator, Point... points) {
        if ((operator == Operator.DIV || operator == Operator.SUB) && (points.length != 2))
            throw new IllegalArgumentException("Divisone e Sottrazione accettano solo 2 blocchi... vedi regole ufficiali kenken");
        if ((operator == Operator.NONE && points.length != 1) || (points.length == 1 && operator != Operator.NONE))
            throw new IllegalArgumentException("L'operazione \"nessuna\" ha solo un blocco... vedi regole ufficiali kenken");
        if (targetNumber < 1)
            throw new IllegalArgumentException("Numero obbiettivo negativo o 0");
        this.targetNumber = targetNumber;
        this.cageOperation = operator;
        this.cagePoint = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cage cage)) return false;

        if (targetNumber != cage.targetNumber) return false;
        if (cageOperation != cage.cageOperation) return false;
        return Arrays.equals(cagePoint, cage.cagePoint);
    }

    @Override
    public int hashCode() {
        int result = targetNumber;
        result = 31 * result + (cageOperation != null ? cageOperation.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(cagePoint);
        return result;
    }

    @Override
    public String toString() {
        return "Cage{" +
                "cagePoint=" + Arrays.toString(cagePoint) +
                ", targetNumber=" + targetNumber +
                ", cageOperation=" + cageOperation +
                '}';
    }
}
