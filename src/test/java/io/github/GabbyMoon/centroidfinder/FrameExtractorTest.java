package io.github.GabbyMoon.centroidfinder;


import org.junit.jupiter.api.Test;

import io.github.GabbyMoon.centroidfinder.utils.VideoTestUtilities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FrameExtractorTest {

    @Test
    void testFrameExtractionFromGeneratedVideo() throws Exception {
        // Generate a 3-minute video, 1 frame per minute, 3 solid colors
        Path video = VideoTestUtilities.createTestVideo(
                180,        // duration in seconds
                1.0 / 60.0, // fps = 1 frame per minute
                320, 240,   // resolution
                new Color[]{Color.RED, Color.GREEN, Color.BLUE}
        );

        String videoPath = video.toString();

        // Call your frame-extraction method here
        VideoFrameExtractor extractor = new VideoFrameExtractor(videoPath);
        List<ImageFrame> actual = extractor.extractFrames();

        List<ImageFrame> expected = new ArrayList<>();

        expected.add(new ImageFrame(
            VideoTestUtilities.makeSolidImage(Color.RED, 320, 240),
            0L,
            0.0
        ));

        expected.add(new ImageFrame(
            VideoTestUtilities.makeSolidImage(Color.GREEN, 320, 240),
            60_000_000L,
            60.0
        ));

        expected.add(new ImageFrame(
            VideoTestUtilities.makeSolidImage(Color.BLUE, 320, 240),
            120_000_000L,
            120.0
        ));

        
        assertTrue(video.toFile().exists());

        // System.out.println("Actual frame count: " + actual.size());
        // for (int i = 0; i < actual.size(); i++) {
        //     ImageFrame f = actual.get(i);
        //     System.out.printf(
        //         "Frame %d -> timestamp: %d µs (%.3f s), color sample: %s%n",
        //         i,
        //         f.microsecondsTimestamp(),
        //         f.secondsTimestamp(),
        //         new Color(f.image().getRGB(f.image().getWidth()/2, f.image().getHeight()/2))
        //     );
        // }

        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            ImageFrame actualImageFrame = actual.get(i);
            ImageFrame expectedImageFrame = expected.get(i);

            Color colorA = new Color(actualImageFrame.image().getRGB(actualImageFrame.image().getWidth() / 2, actualImageFrame.image().getHeight() / 2));
            Color colorB = new Color(expectedImageFrame.image().getRGB(expectedImageFrame.image().getWidth() / 2, expectedImageFrame.image().getHeight() / 2));

            long actualLongTimestamp = actualImageFrame.microsecondsTimestamp();
            long expectedLongTimestamp = expectedImageFrame.microsecondsTimestamp();

            double actualShortTimestamp = actualImageFrame.secondsTimestamp();
            double expectedShortTimestamp = expectedImageFrame.secondsTimestamp();

            assertEquals(colorB, colorA, "Frame " + i + " color mismatch");
            assertEquals(expectedLongTimestamp, actualLongTimestamp, "Frame " + i + " microseconds mismatch.");
            assertEquals(expectedShortTimestamp, actualShortTimestamp, "Frame " + i + " seconds mismatch.");
        }
    }
}



