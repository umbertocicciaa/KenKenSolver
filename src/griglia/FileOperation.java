package griglia;

import giocare.Gioco;
import grafica.SingletonController;
import risolutore.RisolviPuzzle;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public final class FileOperation {
    private FileOperation() {
    }

    private static final SingletonController singletonController = SingletonController.CONTROLLER;

    public static Puzzle createPuzzleFromFile(File f) throws FileNotFoundException {
        Puzzle puzzle = null;
        Scanner scanner = new Scanner(f);
        int boardSize = scanner.nextInt();
        Puzzle.PuzzleBuilder builder = new Puzzle.PuzzleBuilder(boardSize);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.equalsIgnoreCase("separator")) {
                String[] arr = line.split(" ");
                int target = Integer.parseInt(arr[0]);
                Operator operator = getOperator(arr[1].toLowerCase());
                Point[] points = getPoints(arr);
                builder.addCageToPuzzle(target, operator, points);
            } else break;
        }
        puzzle = builder.build();

        scanner.close();
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

    public static void openOnBoard() throws FileNotFoundException {
        Scanner scanner = new Scanner(singletonController.getFileOpened());
        boolean trovatoSeparatore = false;
        JTextField[][] testo = singletonController.getGestoreFinestra().getTextGriglia();
        int size = singletonController.getPuzzle().getSize();
        int i = 0, j = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("separator"))
                trovatoSeparatore = true;
            if (trovatoSeparatore && !line.equalsIgnoreCase("separator")) {
                StringTokenizer stringTokenizer = new StringTokenizer(line, ",",true);

                while (stringTokenizer.hasMoreTokens()) {
                    String token = stringTokenizer.nextToken();
                    if (token.matches("\\d")) {
                        int number = Integer.parseInt(token);
                        testo[size - i - 1][j].setText(Integer.toString(number));
                    }else
                         j++;
                }
                j = 0;
                i++;
            }
        }
        scanner.close();
    }

    public static void savePuzzle(File f, Puzzle puzzle, Gioco gioco) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(f));
        if (puzzle == null)
            throw new RuntimeException();
        int size = puzzle.getSize();
        bf.write(Integer.valueOf(size).toString());
        bf.newLine();
        for (Cage cage : puzzle.getCages()) {
            int target = cage.getTargetNumber();
            Operator operator = cage.getCageOperation();
            StringBuilder linea = new StringBuilder();
            linea.append(target);
            switch (operator) {
                case SUB -> linea.append(" - ");
                case NONE -> linea.append(" none ");
                case SUM -> linea.append(" + ");
                case DIV -> linea.append(" / ");
                case MUL -> linea.append(" * ");
            }
            for (Point point : cage.getCagePoint()) {
                int x = point.getX();
                int y = point.getY();
                linea.append(x + " " + y + " ");
            }
            bf.write(linea.toString());
            bf.newLine();
        }
        bf.write("separator");
        bf.newLine();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (gioco.getBoard()[i][j] != null)
                    bf.write(gioco.getBoard()[i][j].toString());
                if (j < size - 1)
                    bf.write(",");
            }
            bf.newLine();
        }
        bf.close();
    }

}
