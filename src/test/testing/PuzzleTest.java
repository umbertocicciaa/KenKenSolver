package testing;

import griglia.Operator;
import griglia.Point;
import griglia.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

public class PuzzleTest {
    @ParameterizedTest
    @MethodSource(
            "parameterSource"
    )
    public void shouldThrowIllegalArgumentExceptionSize(int size) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Puzzle.PuzzleBuilder(size));
    }

    @Test
    public void shouldThrowRunTimeExceptionForExistingPointInPuzzle() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            Puzzle.PuzzleBuilder builder = new Puzzle.PuzzleBuilder(3);
            builder.addCageToPuzzle(1, Operator.NONE, new Point(0, 0));
            builder.addCageToPuzzle(1, Operator.NONE, new Point(0, 0));
        });
    }

    @Test
    public void shouldThrowRunTimeExceptionForNotFullPuzzle() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            Puzzle.PuzzleBuilder builder = new Puzzle.PuzzleBuilder(3);
            builder.addCageToPuzzle(1, Operator.NONE, new Point(0, 0));
            builder.build();
        });
    }
    private static Stream<Arguments> parameterSource() {
        return Stream.of(
                Arguments.of(2),
                Arguments.of(10),
                Arguments.of(1),
                Arguments.of(11),
                Arguments.of(0),
                Arguments.of(12)
        );
    }
}