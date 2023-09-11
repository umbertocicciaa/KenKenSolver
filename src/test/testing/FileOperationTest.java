package testing;

import griglia.FileOperation;
import griglia.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class FileOperationTest {

    @Test
    public void shouldThrowNoSuchFileException() {
        Assertions.assertThrows(FileNotFoundException.class, () -> FileOperation.createPuzzleFromFile(new File("")));
        Assertions.assertThrows(FileNotFoundException.class, () -> FileOperation.openOnBoard(new File("")));
    }

    @Test
    public void shouldThrowNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> FileOperation.createPuzzleFromFile(null));
        Assertions.assertThrows(NullPointerException.class, () -> FileOperation.openOnBoard(null));
    }

    @ParameterizedTest
    @MethodSource("fileParameter")
    public void correctOpenFile(File f) {
        assertDoesNotThrow(() -> {
            Puzzle puzzle = FileOperation.createPuzzleFromFile(f);
            Assertions.assertNotNull(puzzle);
        });
    }

    private static Stream<Arguments> fileParameter() {
        return Stream.of(
                Arguments.of(new File("src/test/source/puzzle4x4.txt")),
                Arguments.of(new File("src/test/source/puzzle5x5.txt")),
                Arguments.of(new File("src/test/source/puzzle6x6.txt")),
                Arguments.of(new File("src/test/source/puzzle9x9.txt")),
                Arguments.of(new File("src/test/source/puzzleirrisolvibile.txt"))
        );
    }
}