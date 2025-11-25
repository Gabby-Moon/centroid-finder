/**
 * The ThumbnailsProcessingApp class is responsible for extracting a thumbnail image from a video file.
 * It uses the FFmpegFrameGrabber to read the video and the Java2DFrameConverter to convert the video frame to a BufferedImage.
 * The extracted thumbnail is then saved as a JPEG image to the specified output path.
 */
package io.github.GabbyMoon.centroidfinder;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
        if (args.length != 2) {
            System.err.println("Usage: java ThumbnailsProcessingApp <input_video> <output_thumbnail>");
            System.exit(1);
        }
        
        String inputString = args[0];
        String outputString = args[1];

        File inputFile = new File(inputString);
        if (!inputFile.exists() || !inputFile.isFile()) {
            System.err.println("Input video file not found: " + inputString);
            System.exit(2);
        }
        
        try {
            generateThumbnail(inputString, outputString);
        } catch (FFmpegFrameGrabber.Exception e) {
            System.err.println("Error grabbing frames from video file: " + e.getMessage());
            e.printStackTrace();
            System.exit(3);
        }  catch (IOException e) {
            System.err.println("Error writing thumbnail image: " + e.getMessage());
            e.printStackTrace();
            System.exit(4);
        }
    }

    public static void generateThumbnail(String inputString, String outputString) throws IOException, FFmpegFrameGrabber.Exception {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputString);
             Java2DFrameConverter converter = new Java2DFrameConverter()) {
            
            grabber.start();

            BufferedImage frame = converter.convert(grabber.grabImage());

            if (frame == null) {
                grabber.stop();
                converter.close();
                throw new IOException("No frames could be grabbed from the video.");
            }
            
            ImageIO.write(frame, "jpg", new File(outputString));
            System.out.println("Thumbnail saved to " + outputString);

            grabber.stop();
            converter.close();
        }
    }
} 
