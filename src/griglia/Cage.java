package griglia;

public class Cage {
    private Point[]cagePoint;
    private int targetNumber;
    private Operator cageOperation;

    public int getTotal() {
        int total=0;
        switch (cageOperation){
            case SUM -> {
                for(Point p:cagePoint)
                    total+=p.getNumber();

            }
            case DIV -> {
                total=1;
                for(Point p:cagePoint)
                    total/=p.getNumber();

            }
            case MUL -> {
                total=1;
                for (Point p:cagePoint)
                    total*=p.getNumber();
            }
            case SUB -> {
                for (Point p:cagePoint)
                    total-=p.getNumber();
            }
        }
        return total;
    }

    public void setOperation(Operator operator){
        cageOperation=operator;
    }
    public int getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(int targetNumber) {
        this.targetNumber = targetNumber;
    }

}
