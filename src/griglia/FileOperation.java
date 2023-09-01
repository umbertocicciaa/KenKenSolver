package griglia;

import risolutore.RisolviPuzzle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public final class FileOperation {
    private FileOperation() {
    }

    public static Puzzle createPuzzleFromFile(File f) throws FileNotFoundException {
        Puzzle puzzle = null;
        Scanner file = new Scanner(f);
        int boardSize = file.nextInt();
        Puzzle.PuzzleBuilder builder = new Puzzle.PuzzleBuilder(boardSize);
        file.nextLine();
        while (file.hasNextLine()) {
            String line = file.nextLine();
            String[] arr = line.split(" ");
            int target = Integer.parseInt(arr[0]);
            Operator operator = getOperator(arr[1].toLowerCase());
            Point[] points = getPoints(arr);
            builder.addCageToPuzzle(target, operator, points);
        }
        puzzle = builder.build();
        file.close();
        return puzzle;
    }

    private static Point[] getPoints(String[] arr) {
        ArrayList<String> arrList = new ArrayList<>(Arrays.asList(arr));
        arrList.remove("");
        int size = (arrList.size() - 2) / 2;
        Point[] list = new Point[size];
        int pos = 0;
        for (int i = 3; i < arrList.size(); i = i + 2) {
            list[pos] = (new Point(Integer.parseInt(arrList.get(i - 1)), Integer.parseInt(arrList.get(i))));
            pos++;
        }
        return list;
    }

    private static Operator getOperator(String string) {
        switch (string) {
            case "+" -> {
                return Operator.SUM;
            }
            case "-" -> {
                return Operator.SUB;
            }
            case "*" -> {
                return Operator.MUL;
            }
            case "/" -> {
                return Operator.DIV;
            }
            case "none" -> {
                return Operator.NONE;
            }
            default -> {
                throw new IllegalArgumentException("Operatore errato o inesistente");
            }
        }
    }

    public static void savePuzzle(File f, RisolviPuzzle risolutore) throws Exception {
        BufferedWriter bf = new BufferedWriter(new FileWriter(f));
        Integer[][] board = risolutore.getBoard();
        int size = board.length;
        bf.write("Size: " + Integer.valueOf(size).toString());
        bf.newLine();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                bf.write(board[i][j].toString());
                if (j < size - 1)
                    bf.write(", ");
            }
            bf.newLine();
        }
        bf.close();
    }

}
