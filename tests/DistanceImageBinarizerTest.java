// format pulled from previous assignment
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.awt.image.BufferedImage;
import java.util.Arrays;

class DistanceImageBinarizerTest {
    @Test
    void toBinaryArrayLimitThreshold() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        int threshold = 1;
        int targetColor = 0x2ABBBF;
        int outerColor = 0x1F898C;
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
        BufferedImage image = new BufferedImage(7, 11, BufferedImage.TYPE_INT_RGB);
        image.setRGB(5, 7, targetColor);
        image.setRGB(5, 8, targetColor);
        image.setRGB(4, 9, targetColor);
        image.setRGB(3, 10, targetColor);
        image.setRGB(2, 10, targetColor);
        image.setRGB(1, 9, targetColor);
        image.setRGB(0, 8, targetColor);
        image.setRGB(0, 7, targetColor);
        image.setRGB(1, 6, targetColor);
        image.setRGB(2, 5, targetColor);
        image.setRGB(3, 5, targetColor);
        image.setRGB(4, 6, targetColor);
        image.setRGB(5, 10, outerColor);

        int[][] real = binarizer.toBinaryArray(image);
        int[][] should = new int[11][7];
        should[7][5] = 1;
        should[8][5] = 1;
        should[9][4] = 1;
        should[10][3] = 1;
        should[10][2] = 1;
        should[9][1] = 1;
        should[8][0] = 1;
        should[7][0] = 1;
        should[6][1] = 1;
        should[5][2] = 1;
        should[5][3] = 1;
        should[6][4] = 1;

        for(int y = 0; y < real.length; y++) {
            for(int x = 0; x < real[y].length; x++) {
                int a = real[y][x];
                int b = should[y][x];
                System.out.println((a == b) + " " + a + " " + b);
                
            }
        }
        System.out.println(real[1][6]);

        
        Assertions.assertTrue(Arrays.deepEquals(real, should));
    }
    
    @Test
    void test_name() {
    }
}
