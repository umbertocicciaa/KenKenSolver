package giocare;

import mediator.SingletonController;
import griglia.Cage;
import griglia.Operator;
import griglia.Point;

import javax.swing.*;

/**
 * Questa classe gestisce la giocabilita di un puzzle;
 * fornisce metodi per verificare se il puzzle è stato risolto {@code getVittoria} e annessi
 */
public class Gioco {

    private final int size;
    private final Integer[][] board;
    /**
     * <p>{@code singletonController} mediatore tra la GUI e il modello </p
     *
     * @see SingletonController>
     */
    private final SingletonController singletonController;

    public Gioco() {
        singletonController = SingletonController.CONTROLLER;
        this.size = singletonController.getPuzzle().getSize();
        this.board = new Integer[size][size];
    }

    public Integer[][] getBoard() {
        return board;
    }

    /**
     * <p>Il metodo permette di inserire un numero all'interno del tavolo da gioco</p>
     *
     * @param x numero da inserire
     * @throws IllegalArgumentException le coordinate i,j date in input sono negative oppure oltre la dimensione del tavolo da gioco
     */
    public void setNumberInBoard(int i, int j, Integer x) {
        if (i < 0 || i > size || j < 0 || j > size)
            throw new IllegalArgumentException("Coordinate i,j negative o oltre la dimensione del puzzle");
        board[i][j] = x;
    }

    public int getSize() {
        return size;
    }

    /**
     * <p>Il metodo permette di verificare se il gioco è stato vinto</p>
     */
    public boolean getVittoria() {
        boolean nulli = presentiNull();
        if (nulli) return false;
        boolean noDuplicati = verificaMatrice();
        System.out.println("no duplicati " + noDuplicati);
        boolean verificaCage = verificaCage();
        System.out.println("cage verificato " + verificaCage);
        return noDuplicati && verificaCage;
    }

    /**
     * <p>Il metodo permette di verificare se il totale di ogni cage è soddisfatto</p>
     */
    private boolean verificaCage() {
        for (Cage cage : singletonController.getPuzzle().getCages()) {
            System.out.println(cage);
            Operator operator = cage.getCageOperation();
            int target = cage.getTargetNumber();
            int total = 0;
            double tot = 0;
            int max = getMax(cage);
            if (operator == Operator.MUL) total = 1;
            if (operator == Operator.SUB) total = max;
            if (operator == Operator.DIV) {
                total = max;
                tot = total;
            }

            for (Point point : cage.getCagePoint()) {
                int x = point.x();
                int y = point.y();
                switch (operator) {
                    case NONE -> total = board[x][y];

                    case SUM -> total += board[x][y];
                    case MUL -> total *= board[x][y];

                    case SUB -> {
                        if (board[x][y] != max) total -= board[x][y];
                    }
                    case DIV -> {
                        if (board[x][y] != max) tot /= board[x][y];
                    }
                }
                System.out.println("op: " + operator + " tot: " + total + "target:  " + target + "x:  " + x + "y:  " + y + " numero: " + board[x][y] + " max: " + max);
            }

            if (total != target && operator != Operator.DIV) return false;

            if (tot != (double) target && operator == Operator.DIV) return false;

        }
        return true;
    }

    private int getMax(Cage cage) {
        int max = 0;
        for (Point point : cage.getCagePoint()) {
            int x = point.x();
            int y = point.y();
            if (board[x][y] > max) max = board[x][y];
        }
        return max;
    }

    /**
     * <p>Il metodo permette di verificare se ogni rica o colonna non ha valori duplicati</p>
     */
    private boolean verificaMatrice() {
        int righe = board.length;
        int colonne = board[0].length;
        // Verifica righe
        for (Integer[] integers : board) {
            for (int j = 0; j < colonne; j++) {
                for (int k = j + 1; k < colonne; k++) {
                    if (integers[j].equals(integers[k])) {
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
                if (board[i][j] == null) return true;
            }
        }
        return false;
    }

    /**
     * <p>Il metodo permette di verificare se è possibile inserire un valore x in posizione r,c</p>
     *
     * @param x  numero da inserire
     * @param r  indice riga
     * @param c  indice colonna
     * @param sb viene utilizzato per creare una stringa di errore (se il numero non è inseribile)
     */
    private void inseribilita(int x, int r, int c, StringBuilder sb) {
        for (int i = 0; i < size; ++i) {
            if (board[i][c] != null && board[i][c].equals(x) && i != r) {
                sb.append("Il numero: ").append(x).append(" è gia presente in posizione x: ").append(i + 1).append(" y: ").append(c + 1).append("\n");
                return;
            }
        }
        for (int i = 0; i < size; ++i) {
            if (board[r][i] != null && board[r][i].equals(x) && i != c) {
                sb.append("Il numero: ").append(x).append(" è gia presente in posizione x: ").append(r + 1).append(" y: ").append(i + 1).append("\n");
                return;
            }
        }
        Cage cage = singletonController.getPuzzle().getPointToCage().get(new Point(r, c));
        int inseritiNelCage = 0;
        for (Point point : cage.getCagePoint()) {
            if (board[point.x()][point.y()] != null) inseritiNelCage++;
        }
        if (inseritiNelCage == cage.getCagePoint().length) {
            Operator operator = cage.getCageOperation();
            int target = cage.getTargetNumber();
            int total = 0;
            double tot = 0;
            int max = getMax(cage);
            if (operator == Operator.MUL) total = 1;
            if (operator == Operator.SUB) total = max;
            if (operator == Operator.DIV) {
                total = max;
                tot = total;
            }
            for (Point point : cage.getCagePoint()) {
                int xc = point.x();
                int yc = point.y();
                switch (operator) {
                    case NONE -> total = board[xc][yc];
                    case SUM -> total += board[xc][yc];
                    case MUL -> total *= board[xc][yc];
                    case SUB -> {
                        if (board[xc][yc] != max) total -= board[xc][yc];
                    }
                    case DIV -> {
                        if (board[xc][yc] != max) tot /= board[xc][yc];
                    }
                }
            }
            if (total != target && operator != Operator.DIV) {
                sb.append("Il totale: ").append(total).append(" non è corretto, dovrebbe essere: ").append(cage.getTargetNumber()).append(" per ").append(cage).append("\n");
                return;
            }
            if (tot != (double) target && operator == Operator.DIV) {
                sb.append("Il totale: ").append(total).append(" non è corretto, dovrebbe essere: ").append(cage.getTargetNumber()).append(" per ").append(cage).append("\n");
            }
        }
    }

    public void inseribile() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (board[i][j] != null) inseribilita(board[i][j], i, j, sb);
            }
        }
        if (!sb.toString().isEmpty())
            JOptionPane.showMessageDialog(null, sb.toString(), "Vincoli non rispettati", JOptionPane.WARNING_MESSAGE);
    }
}
