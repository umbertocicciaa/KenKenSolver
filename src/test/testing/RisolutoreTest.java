package testing;

import griglia.FileOperation;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import risolutore.Risolutore;
import risolutore.RisolutoreBacktracking;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RisolutoreTest {

    @ParameterizedTest
    @MethodSource("fileParameter")
    void risolviKenken(File f) {
        assertDoesNotThrow(() -> {
            Risolutore risolutore = new Risolutore(new RisolutoreBacktracking(FileOperation.createPuzzleFromFile(f)));
            risolutore.risolviKenken();
            Integer[][] board = risolutore.risolviPuzzle().getBoard();
            for (Integer[] integers : board) {
                for (int j = 0; j < board.length; ++j) {
                    Assertions.assertNotNull(integers[j]);
                }
            }
        });
    }

    private static Stream<Arguments> fileParameter() {
        return Stream.of(
                Arguments.of(new File("src/test/source/puzzle4x4.txt")),
                Arguments.of(new File("src/test/source/puzzle5x5.txt")),
                Arguments.of(new File("src/test/source/puzzle6x6.txt")),
                Arguments.of(new File("src/test/source/puzzle9x9.txt"))
        );
    }

}