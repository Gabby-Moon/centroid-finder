package io.github.GabbyMoon.centroidfinder;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ThumbnailsProcessingApp {
    public static void main(String[] args) {
        String inputPath = args[0];

        String outputPath = args[1];


        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputPath)) {
            grabber.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage frame = converter.convert(grabber.grabImage());

            if (frame != null) {
                ImageIO.write(frame, "jpg", new File(outputPath));
                System.out.println("Thumbnail saved to " + outputPath);
            } else {
                System.err.println("No image frame found in video.");
            }

            grabber.stop();
            converter.close();
        } catch (Exception e) {
            System.out.println("Output Path: " + outputPath);
            System.out.println("Input Path: " + inputPath);
            e.printStackTrace();
        }
    }
} 
