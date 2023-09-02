package giocare;

import grafica.SingletonController;
import griglia.Cage;
import griglia.Operator;
import griglia.Point;


public class Gioco {

    private int size;
    private Integer[][] board;
    private SingletonController singletonController;

    public Gioco() {
        singletonController = SingletonController.CONTROLLER;
        this.size = singletonController.getPuzzle().getSize();
        this.board = new Integer[size][size];
    }

    public Integer[][] getBoard() {
        return board;
    }

    public void setNumberInBoard(int i, int j, Integer x) {
        if (i < 0 || i > size || j < 0 || j > size)
            throw new IllegalArgumentException();
        board[i][j] = x;
    }

    public int getSize() {
        return size;
    }

    public boolean getVittoria() {
        boolean nulli = presentiNull();
        if (nulli)
            return false;
        boolean noDuplicati = verificaMatrice();
        System.out.println("no duplicati " + noDuplicati);
        boolean verificaCage = verificaCage();
        System.out.println("cage verificato " + verificaCage);
        return noDuplicati && verificaCage;
    }

    private boolean verificaCage() {
        for (Cage cage : singletonController.getPuzzle().getCages()) {
            System.out.println(cage);
            Operator operator = cage.getCageOperation();
            int target = cage.getTargetNumber();
            int total = 0;
            int max = getMax(cage);
            if (operator == Operator.MUL)
                total = 1;
            if (operator == Operator.SUB || operator == Operator.DIV)
                total = max;

            for (Point point : cage.getCagePoint()) {
                int x = point.getX();
                int y = point.getY();
                switch (operator) {
                    case NONE -> {
                        total = board[x][y];
                    }
                    case SUM -> total += board[x][y];
                    case MUL -> {
                        total *= board[x][y];
                    }
                    case SUB -> {
                        if (board[x][y] != max)
                            total -= board[x][y];
                    }
                    case DIV -> {
                        if (board[x][y] != max)
                            total /= board[x][y];
                    }
                }
                System.out.println(operator + " " + total + " " + target + " " + x + " " + y + " " + board[x][y] + " " + max);
            }


            if (total != target)
                return false;
        }
        return true;
    }

    private int getMax(Cage cage) {
        int max = 0;
        for (Point point : cage.getCagePoint()) {
            int x = point.getX();
            int y = point.getY();
            if (board[x][y] > max)
                max = board[x][y];
        }
        return max;
    }

    private boolean verificaMatrice() {
        int righe = board.length;
        int colonne = board[0].length;
        // Verifica righe
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                for (int k = j + 1; k < colonne; k++) {
                    if (board[i][j].equals(board[i][k])) {
                        return false; // Trovato un duplicato nella stessa riga
                    }
                }
            }
        }
        // Verifica colonne
        for (int j = 0; j < colonne; j++) {
            for (int i = 0; i < righe; i++) {
                for (int k = i + 1; k < righe; k++) {
                    if (board[i][j].equals(board[k][j])) {
                        return false; // Trovato un duplicato nella stessa colonna
                    }
                }
            }
        }
        return true;
    }


    private boolean presentiNull() {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (board[i][j] == null)
                    return true;
            }
        }
        return false;
    }

}
