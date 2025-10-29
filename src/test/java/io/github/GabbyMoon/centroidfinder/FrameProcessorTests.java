package io.github.GabbyMoon.centroidfinder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FrameProcessorTests {

    @Test
    void groupDataProcessorImageFewGroups(){
        DfsBinaryGroupFinder center = new DfsBinaryGroupFinder();
        BufferedImage image = new BufferedImage( 11, 20, BufferedImage.TYPE_INT_RGB);
        image.setRGB(7, 7, 0x5a82c8);
        image.setRGB(6, 7, 0x5a82c8);
        image.setRGB(5, 7, 0x5a82c8);
        image.setRGB(7, 6, 0x5a82c8);
        image.setRGB(7, 8, 0x5a82c8);

        image.setRGB(1, 3, 0x5a82c8);
        image.setRGB(1, 2, 0x5a82c8);
        image.setRGB(2, 3, 0x5a82c8);

        image.setRGB(9, 11, 0x5a82c8);
        image.setRGB(10, 11, 0x5a82c8);
        image.setRGB(9, 12, 0x5a82c8);
        image.setRGB(10, 12, 0x5a82c8);

        FrameProcessor processor = new FrameProcessor();
        Group actual = processor.groupDataProcessor(15, 0x5a82c8, image);
        List<Coordinate> points = new ArrayList<>();
        points.add(new Coordinate(7, 7));
        points.add(new Coordinate(6, 7));
        points.add(new Coordinate(5, 7));
        points.add(new Coordinate(7, 6));
        points.add(new Coordinate(7, 8));
        
        Group expected = new Group(5, center.centerFinder(points));

        Assertions.assertEquals(expected, actual);
    }
    
    @Test
    void groupDataProcessorImageNoGroups(){
        BufferedImage image = new BufferedImage( 11, 20, BufferedImage.TYPE_INT_RGB);
        FrameProcessor processor = new FrameProcessor();
        Group actual = processor.groupDataProcessor(15, 0x5a82c8, image);
        Group expected = new Group(0, new Coordinate(-1, -1));

        Assertions.assertEquals(expected, actual);
    }
}
