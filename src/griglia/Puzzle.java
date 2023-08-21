package griglia;

import java.io.File;
import java.util.*;

public class Puzzle {
    private final int size;
    private final Set<Cage> cages;
    private final Map<Point, Cage> pointToCage;

    private Puzzle(PuzzleBuilder builder) {
        if (builder == null)
            throw new NullPointerException("Hai passato un builder null");
        this.size = builder.size;
        this.cages = Collections.unmodifiableSet(builder.cages);
        this.pointToCage = Collections.unmodifiableMap(builder.pointToCage);
    }

    public int getSize() {
        return size;
    }

    public Set<Cage> getCages() {
        return cages;
    }

    public Map<Point, Cage> getPointToCage() {
        return pointToCage;
    }

    public static class PuzzleBuilder {
        private int size;
        private Set<Cage> cages;
        private Map<Point, Cage> pointToCage;

        public PuzzleBuilder(int size) {
            if (size < 3 || size > 9)
                throw new IllegalArgumentException("Dimensioni puzzle scorrette");
            this.size = size;
            cages = new HashSet<>();
            pointToCage = new HashMap<>();
        }

        public static Puzzle createPuzzleFromFile(File f) {
            Puzzle puzzle = null;
            try {
                Scanner file = new Scanner(f);
                int boardSize = file.nextInt();
                PuzzleBuilder builder = new PuzzleBuilder(boardSize);
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
                list[pos] = (new Point(Integer.parseInt(arrList.get(i-1)), Integer.parseInt(arrList.get(i))));
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

        public PuzzleBuilder addCageToPuzzle(int target, Operator operator, Point... points) {
            Cage cage = new Cage(target, operator, points);
            cages.add(cage);
            for (Point point : points) {
                if (pointToCage.containsKey(point))
                    throw new RuntimeException("Il puzzle contiene gia il punto: " + point);
                pointToCage.put(point, cage);
            }
            return this;
        }

        public Puzzle build() {
            checkAllPointAreInCage();
            return new Puzzle(this);
        }

        private void checkAllPointAreInCage() {
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < size; ++j) {
                    Point point = new Point(i, j);
                    if (!pointToCage.containsKey(point))
                        throw new RuntimeException("Alcune celle non sono state inserite nei cage");
                }
            }
        }

    }

    @Override
    public String toString() {
        return "Puzzle{" +
                "size=" + size +
                ", cages=" + cages +
                ", pointToCage=" + pointToCage +
                '}';
    }
}
