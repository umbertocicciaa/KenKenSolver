package testing;

import griglia.Operator;
import griglia.Point;
import griglia.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class PuzzleTest {
    @ParameterizedTest
    @MethodSource(
            "parameter"
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
    private static Stream<Arguments> parameter() {
        return Stream.of(
                Arguments.of(2),
                Arguments.of(10)
        );
    }
}