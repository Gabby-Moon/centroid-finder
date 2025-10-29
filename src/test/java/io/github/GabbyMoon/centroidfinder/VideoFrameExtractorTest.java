package io.github.GabbyMoon.centroidfinder;

import org.junit.jupiter.api.Test;
import org.bytedeco.javacv.FrameGrabber;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VideoFrameExtractorTest {

    @Test
    public void testExtractGroupsFromEnsantinaVideo() throws FrameGrabber.Exception, IOException {
        String filepath = "sampleInput/ensantina.mp4";
        int threshold = 100;
        int hexTargetColor = 0x4A0000;

        VideoFrameExtractor extractor = new VideoFrameExtractor();
        List<ImageFrame> frames = extractor.extractFrames(filepath, threshold, hexTargetColor);

        String outputPath = "sampleOutput/ensantina_groups.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (ImageFrame frame : frames) {
                Group group = frame.group();
                writer.write(group.toCsvRow());
                writer.newLine();
            }
        }

        System.out.println("Group data written to: " + outputPath);
    }
}
