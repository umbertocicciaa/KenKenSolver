package main;

import griglia.Puzzle;

import risolutore.RisolutoreDFS;
import risolutore.RisolviPuzzle;

import java.io.File;


import static griglia.FileOperation.*;

public class MainRisolvi {
    public static void main(String[] args) {
        File f = new File("/Users/umbertodomenicociccia/Desktop/puzzle9x9.txt");
        //File save = new File("/Users/umbertodomenicociccia/Desktop/puzzle6x6risolto.txt");
        try {
            Puzzle puzzle = createPuzzleFromFile(f);
            RisolviPuzzle risolviPuzzle = new RisolutoreDFS(puzzle);
            System.out.println("DFS");
            risolviPuzzle.risolvi();

            // risolviPuzzle = new RisolutoreBacktracking(puzzle);
            //System.out.println("Backtracking");
            //risolviPuzzle.risolvi();
            //savePuzzle(save, risolviPuzzle);
        }catch(Exception e){}


    }
}
