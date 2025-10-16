// format pulled from previous assignment
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertEquals;


import java.awt.image.BufferedImage;
import java.util.Arrays;

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
    void test_toBufferedImage_OneWhitePixel() {
        int[][] imageArray = {
            // a 
            // y, x 0  1  2  3  4  5  6  7  8
            /* 0 */{0, 0, 0},
            /* 1 */{0, 1, 0},
            /* 2 */{0, 0, 0},
        };
 
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0, 0);
        
        BufferedImage image = binarizer.toBufferedImage(imageArray);
        
        BufferedImage testImage = new BufferedImage(imageArray[0].length, imageArray.length, BufferedImage.TYPE_INT_RGB);
        testImage.setRGB(1, 1, 0xFFFFFF);

        // height and width should be the same
        assertEquals(image.getWidth(), testImage.getWidth());
        assertEquals(image.getHeight(), testImage.getHeight());
        assertEquals(image.getType(), testImage.getType());
        
        // each pixel should be the same color
        for (int y = 0; y < imageArray.length; y++) {
            for (int x = 0; x < imageArray[0].length; x++) {
                assertEquals(image.getRGB(x, y), testImage.getRGB(x, y));
            }
        }
    }

    @Test
    void test_toBufferedImage_TwoRegions() {
        int[][] imageArray = {
            // a 
            // y, x 0  1  2  3  4
            /* 0 */{1, 1, 1, 0, 0},
            /* 1 */{1, 1, 1, 0, 0},
            /* 2 */{1, 1, 1, 0, 0},
            /* 3 */{0, 0, 0, 0, 0},
            /* 4 */{1, 1, 0, 0, 0},
            /* 5 */{1, 1, 0, 0, 0}, 
        };
 
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0, 0);
        
        BufferedImage image = binarizer.toBufferedImage(imageArray);
        
        BufferedImage testImage = new BufferedImage(imageArray[0].length, imageArray.length, BufferedImage.TYPE_INT_RGB);
        testImage.setRGB(0, 0, 0xFFFFFF);
        testImage.setRGB(1, 0, 0xFFFFFF);
        testImage.setRGB(2, 0, 0xFFFFFF);
        testImage.setRGB(0, 1, 0xFFFFFF);
        testImage.setRGB(1, 1, 0xFFFFFF);
        testImage.setRGB(2, 1, 0xFFFFFF);
        testImage.setRGB(0, 2, 0xFFFFFF);
        testImage.setRGB(2, 2, 0xFFFFFF);
        testImage.setRGB(2, 2, 0xFFFFFF);
        
        testImage.setRGB(0, 0, 0xFFFFFF);
        testImage.setRGB(0, 0, 0xFFFFFF);
        testImage.setRGB(0, 0, 0xFFFFFF);
        testImage.setRGB(0, 0, 0xFFFFFF);


        // height and width should be the same
        assertEquals(image.getWidth(), testImage.getWidth());
        assertEquals(image.getHeight(), testImage.getHeight());
        assertEquals(image.getType(), testImage.getType());
        
        // each pixel should be the same color
        for (int y = 0; y < imageArray.length; y++) {
            for (int x = 0; x < imageArray[0].length; x++) {
                assertEquals(image.getRGB(x, y), testImage.getRGB(x, y));
            }
        }
    }
}
