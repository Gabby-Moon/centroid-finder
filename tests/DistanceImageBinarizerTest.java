// format pulled from previous assignment
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.awt.image.BufferedImage;

class DistanceImageBinarizerTest {
    @Test
    void toBinaryArrayLimitThreshold() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        int threshold = 0;
        int targetColor = 0x2ABBBF;
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        
    }
    
    @Test
    void test_name() {
    }
}
