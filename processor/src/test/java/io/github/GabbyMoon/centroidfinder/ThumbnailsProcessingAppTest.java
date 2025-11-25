package io.github.GabbyMoon.centroidfinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.*;

public class ThumbnailsProcessingAppTest {

@Test
void testMain_createsThumbnailFromVideo() throws Exception {
    // Paths relative to centroid-finder/processor
    String inputPath = "../videos/testing_square.mp4";
    String outputPath = "../thumbnails/testing_square.jpg";

    // Ensure output folder exists
    Path outputFolder = Path.of(outputPath).getParent();
    if (!Files.exists(outputFolder)) {
        Files.createDirectories(outputFolder);
    }

    // Assert input file exists
    Path inputFile = Path.of(inputPath);
    assertTrue(Files.exists(inputFile) && Files.isRegularFile(inputFile),
            "Input video file not found: " + inputPath);

    // Clean up output if it exists
    Path outputFile = Path.of(outputPath);
    if (Files.exists(outputFile)) {
        Files.delete(outputFile);
    }

    // Execute the main method
    ThumbnailsProcessingApp.main(new String[]{inputPath, outputPath});

    // Verify output
    assertTrue(Files.exists(outputFile), "Thumbnail was not created");
    assertTrue(Files.size(outputFile) > 0, "Thumbnail file is empty");

    BufferedImage img = ImageIO.read(outputFile.toFile());
    assertNotNull(img, "Thumbnail image could not be read");
    assertTrue(img.getWidth() > 0, "Thumbnail width should be > 0");
    assertTrue(img.getHeight() > 0, "Thumbnail height should be > 0");
}

  @Test
    void testGenerateThumbnailSuccess() throws Exception {
        // Video path relative to the processor module
        String inputPath = "../videos/testing_square.mp4";

        // Output path in the thumbnails directory
        String outputPath = "../thumbnails/test-squares.jpg";

        // Ensure output directory exists
        Path outputDir = Path.of(outputPath).getParent();
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        // Call the method under test
        ThumbnailsProcessingApp.generateThumbnail(inputPath, outputPath);

        // Assert that the thumbnail file was created
        assertTrue(Files.exists(Path.of(outputPath)), "Thumbnail should have been created");
    }

    @Test
    void testGenerateThumbnailMissingFile() {
        // Video path that does not exist
        String inputPath = "../videos/non_existent_video.mp4";

        // Output path for thumbnail
        String outputPath = "../thumbnails/non_existent.jpg";

        // Expect FFmpegFrameGrabber.Exception to be thrown
        assertThrows(FFmpegFrameGrabber.Exception.class, () -> {
            ThumbnailsProcessingApp.generateThumbnail(inputPath, outputPath);
        });
    }

    @Test
    void testGenerateThumbnailOverwrite() throws Exception {
        // Video path
        String inputPath = "../videos/testing_square.mp4";

        // Output path
        String outputPath = "../thumbnails/test-squares-overwrite.jpg";

        // Ensure output directory exists
        Path outputDir = Path.of(outputPath).getParent();
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        // Create a dummy file at the output path to simulate existing thumbnail
        Files.writeString(Path.of(outputPath), "old content");

        // Run generateThumbnail() — should overwrite without error
        ThumbnailsProcessingApp.generateThumbnail(inputPath, outputPath);

        // Assert file exists and is not empty
        assertTrue(Files.exists(Path.of(outputPath)), "Thumbnail should exist");
        assertTrue(Files.size(Path.of(outputPath)) > 0, "Thumbnail should not be empty");
    }

    @Test
    void testGenerateThumbnailInvalidFormat(@TempDir Path tempDir) throws Exception {
        // Create a non-video file
        String inputPath = tempDir.resolve("not_a_video.txt").toString();
        Files.writeString(Path.of(inputPath), "this is not a video");

        String outputPath = tempDir.resolve("output.jpg").toString();

        // Expect FFmpegFrameGrabber.Exception because file is not a valid video
        assertThrows(FFmpegFrameGrabber.Exception.class, () ->
                ThumbnailsProcessingApp.generateThumbnail(inputPath, outputPath)
        );
    }
}
