package io.github.GabbyMoon.centroidfinder;

import java.awt.image.BufferedImage;
import java.util.Random;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

public class VideoExperiment {
    public static void main(String[] args) {
        VideoExperiment experiment1 = new VideoExperiment();
        experiment1.javaCV();

    }

    public void javaCV() {
        // grabs video from given path
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("sampleInput/ensantina.mp4");
        // used to tell grabber that it's an mp4, test to fix an error
        grabber.setFormat("mp4");
        // an object to convert a given frame to a buffered image
        Java2DFrameConverter converter = new Java2DFrameConverter();
        try{
            // starts the framegrabber on the video
            grabber.start();

            // gets the amount of frames in the video
            int totalFrames = grabber.getLengthInFrames();
            // picks a number between 0 and the number of frames
            int randomFrameIndex = new Random().nextInt(totalFrames);
            // sets the video to the given frame number
            grabber.setFrameNumber(randomFrameIndex);

            // saves the frame to a frame object
            Frame frame = grabber.grabImage();
            
            // uses the converter to make the frame to a buffered image
            BufferedImage image = converter.convert(frame);

            // looks to see if an item was passed through and returns the frame number
            if(image != null) {
                // prints if successful
                System.out.println("Grabbed frame at " + randomFrameIndex);
            } else {
                System.out.println("Failed to make image.");
            }

        } catch (Exception e) {
            //throws the error if anything goes wrong
            e.printStackTrace();
        } finally {
            // closes the grabber or prints error if failed
            try {
                grabber.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // closes the converter
            converter.close();
        }
    }
}
