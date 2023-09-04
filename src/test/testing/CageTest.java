package testing;

import griglia.Cage;
import griglia.Operator;
import griglia.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CageTest {

    @ParameterizedTest
    @MethodSource("parametriIllegalArgumentException")
    public void shouldThrowIllegalArgumentException(int target,Operator operator,Point...points) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Cage(target, operator,points));
    }
    private static Stream<Arguments> parametriIllegalArgumentException() {
        return Stream.of(
                Arguments.of(0,Operator.NONE, new Point[]{ new Point(0, 0)}),
                Arguments.of(1,Operator.SUB, new Point[]{new Point(0,0),new Point(0,0),new Point(0,0)}),
                Arguments.of(1,Operator.DIV,new Point[]{new Point(0,0),new Point(0,0),new Point(0,0)}),
                Arguments.of(1,Operator.NONE,new Point[]{new Point(0,0),new Point(0,0)}));
    }
}