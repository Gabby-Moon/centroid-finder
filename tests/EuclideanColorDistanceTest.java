// format pulled from previous assignment
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class EuclideanColorDistanceTest {
    @Test
    void euclideanColorDistanceSameComplexColor() {
        EuclideanColorDistance compare = new EuclideanColorDistance();
        double real = compare.distance(0x2C33FF, 0x2C33FF);
        double should = 0.0;
        Assertions.assertEquals(should, real);
    }

    @Test
    void euclideanColorDistanceSameComplexRed() {
        EuclideanColorDistance compare = new EuclideanColorDistance();
        double real = compare.distance(0xFF0000, 0xFF0000);
        double should = 0.0;
        Assertions.assertEquals(should, real);
    }

    @Test
    void euclideanColorDistanceSameComplexGreen() {
        EuclideanColorDistance compare = new EuclideanColorDistance();
        double real = compare.distance(0x00FF00, 0x00FF00);
        double should = 0.0;
        Assertions.assertEquals(should, real);
    }

    @Test
    void euclideanColorDistanceSameComplexBlue() {
        EuclideanColorDistance compare = new EuclideanColorDistance();
        double real = compare.distance(0x0000FF, 0x0000FF);
        double should = 0.0;
        Assertions.assertEquals(should, real);
    }
}
