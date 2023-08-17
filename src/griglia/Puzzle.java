package griglia;

import java.util.Map;

public class Puzzle {
    private int [][]board;
    private int size;
    private Map<Point,Cage> pointCageMap;

    public Puzzle (int size){
        board=new int[size][size];
        this.size=size;
    }
}
