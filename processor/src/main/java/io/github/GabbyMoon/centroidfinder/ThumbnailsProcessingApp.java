package io.github.GabbyMoon.centroidfinder;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
/**
 * The ThumbnailsProcessingApp class is responsible for extracting a thumbnail image from a video file.
 * It uses the FFmpegFrameGrabber to read the video and the Java2DFrameConverter to convert the video frame to a BufferedImage.
 * The extracted thumbnail is then saved as a JPEG image to the specified output path.
 */
public class ThumbnailsProcessingApp {
    /**
     *  The main method of the ThumbnailsProcessingApp.
     *  It takes two command-line arguments: the input video file path and the output thumbnail image path.
     *  The method extracts the first frame of the video, converts it to a BufferedImage, and saves it as a JPEG image.
     *  If the video cannot be read or the thumbnail cannot be saved, it prints an error message and the stack trace.
     * @param args Command-line arguments: <input_video> <output_thumbnail>
     * @throws Exception if there are issues reading the video or writing the thumbnail image. 
     * @throws IllegalArgumentException if the number of arguments is incorrect.
     */  
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
