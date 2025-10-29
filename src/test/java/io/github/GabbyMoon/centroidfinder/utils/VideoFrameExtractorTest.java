package io.github.GabbyMoon.centroidfinder.utils;

import org.junit.jupiter.api.Test;

import io.github.GabbyMoon.centroidfinder.*;

import javax.imageio.ImageIO;

import java.io.BufferedWriter;
// import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class VideoFrameExtractorTest {

    @Test
    public void testExtractFramesAndDumpImages() throws Exception {
        String inputPath = "sampleInput/ensantina.mp4";
        String outputPath = "sampleOutput/output.csv";
        int hexTargetColor = 0xFFA200;
        int threshold = 164;


        VideoFrameExtractor extractor = new VideoFrameExtractor();
        List<ImageFrame> frames = extractor.extractFrames(inputPath, hexTargetColor, threshold);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (ImageFrame frame : frames) {
                Group group = frame.group();
                writer.write(group.toCsvRow());
                writer.newLine();
            }
        }
    }
}

