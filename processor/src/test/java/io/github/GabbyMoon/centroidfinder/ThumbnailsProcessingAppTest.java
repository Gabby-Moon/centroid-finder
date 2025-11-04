package io.github.GabbyMoon.centroidfinder;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.*;

public class ThumbnailsProcessingAppTest {

 @Test
 void testMain_createsThumbnailFromVideo() throws Exception {
        // Relative to centroid-finder/processor
        // String inputPath = "../server/src/static/videos/moving_cyan_video.mp4";
        // String outputPath = "../server/src/static/thumbnails/moving_cyan_video.jpg";

        String inputPath = "./sampleInput/ensantina.mp4";
        String outputPath = "./sampleOutput/ensantina.jpg";

        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        File outputFolder = outputFile.getParentFile();

        // Assert folder exists
        assertTrue(outputFolder.exists() && outputFolder.isDirectory(), "Thumbnails folder not found: " + outputFolder.getPath());

        // Assert input file exists
        assertTrue(inputFile.exists() && inputFile.isFile(), "Input video file not found: " + inputPath);

        // Clean up output if it exists
        if (outputFile.exists()) {
            assertTrue(outputFile.delete(), "Failed to delete existing thumbnail");
        }

        // Execute
        ThumbnailsProcessingApp.main(new String[]{inputPath, outputPath});

        // Verify output
        assertTrue(outputFile.exists(), "Thumbnail was not created");
        assertTrue(outputFile.length() > 0, "Thumbnail file is empty");

        BufferedImage img = ImageIO.read(outputFile);
        assertNotNull(img, "Thumbnail image could not be read");
        assertTrue(img.getWidth() > 0, "Thumbnail width should be > 0");
        assertTrue(img.getHeight() > 0, "Thumbnail height should be > 0");
    }
}
