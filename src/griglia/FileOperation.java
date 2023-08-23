package griglia;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public final class FileOperation {

    private FileOperation() {
    }

    public static Puzzle createPuzzleFromFile(File f) {
        Puzzle puzzle = null;
        try {
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

        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
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
}
