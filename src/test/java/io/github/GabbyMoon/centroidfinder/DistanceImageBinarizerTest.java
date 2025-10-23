package io.github.GabbyMoon.centroidfinder;


// format pulled from previous assignment
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        testImage.setRGB(1, 2, 0xFFFFFF);
        testImage.setRGB(2, 2, 0xFFFFFF);
        
        testImage.setRGB(0, 4, 0xFFFFFF);
        testImage.setRGB(0, 5, 0xFFFFFF);
        testImage.setRGB(1, 4, 0xFFFFFF);
        testImage.setRGB(1, 5, 0xFFFFFF);


        // height and width should be the same
        assertEquals(image.getWidth(), testImage.getWidth());
        assertEquals(image.getHeight(), testImage.getHeight());
        assertEquals(image.getType(), testImage.getType());
        
        // checking colors
        for (int y = 0; y < testImage.getHeight(); y++) {
            for (int x = 0; x < testImage.getWidth(); x++) {
                assertEquals(testImage.getRGB(x, y), image.getRGB(x, y));
            }
        }
    }
}
