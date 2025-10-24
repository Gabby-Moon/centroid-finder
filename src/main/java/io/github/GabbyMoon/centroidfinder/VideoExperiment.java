package io.github.GabbyMoon.centroidfinder;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;


//import for a utility used with ffmpeg
import org.bytedeco.ffmpeg.global.avutil;

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

    public static void javaCVAsh() {
        // these were my local paths for testing things
        String videoPath = "sampleInput/ensantina.mp4";
        String outputName = "sampleOutput/exampleFrame.png";


        // try/catch to prevent exceptions in case the video is missing/wrong path
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            // we're doing RGB processing so we can set the grabber to save pictures as RGB
            grabber.setPixelFormat(avutil.AV_PIX_FMT_RGB24);
            
            // starts the frame grabber
            grabber.start();
            
            // outputs the video metadata
            // can use these methods to figure out how long the video is to grab specific frames based on timing data
            // at least every 60 seconds per Auberon
            System.out.println("Video Info:");
            System.out.println("Format: " + grabber.getFormat());
            System.out.println("Frame Rate: " + grabber.getFrameRate());
            System.out.println("Total Frames: " + grabber.getLengthInFrames());
            System.out.println("Duration (s): " + grabber.getLengthInTime() / 1_000_000.0);
            System.out.println("Resolution: " + grabber.getImageWidth() + "x" + grabber.getImageHeight());

            // syntax for grabbing the frame
            // grabImage specifically only gets the image data and not sound, etc.
            Frame frame = grabber.grabImage();

            // check if it got a frame
            if (frame != null) {
                // converting the frame to a buffered image and putting it through the BinarizingImageGroupFinder
                // using the Java2D frame converter lets us convert the frame data directly to a buffered image
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage image = converter.getBufferedImage(frame);

            

                //checking what type this is using by default
                
                System.out.println("Type:" + image.getType());

                // Note: So this type ignores alpha (3BYTE_BGR) but it still has the RGB values we need for color distance

                // the salamander color and threshold for building the binarizer
                int color = 0x281E19;
                int threshold = 50;

                ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
                ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, color, threshold);

                ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

                // finding the salamander in the buffered image
                List<Group> groups = groupFinder.findConnectedGroups(image);
                
                //print the groups to console so I can see if it's working
                //on the real program we would only need the first group since they're in order by size
                //and we just want the biggest group per frame for the tracking data since it's most likely to be the salamander
                StringBuilder sb = new StringBuilder();
                for (Group group : groups) {
                    sb.append(group.toCsvRow()).append("\n");
                }
                System.out.println(sb);
                
                // Mat mat = converter.convert(frame);

                // // Save the frame as an image
                // // imwrite is smart enough to use the extension (.png etc) to determine the file type
                // opencv_imgcodecs.imwrite(outputName, mat);
                // System.out.println("Saved first frame to " + outputName);

                // I don't actually know if you need to close this but vsCode was complaining
                converter.close();
            } else {
                // test output if frame was null for some reason
                System.out.println("No frame grabbed!");
            }
            

            // stops the framegrab process and lets go of the resources
            grabber.stop();
            grabber.release();
        } catch (Exception e) {
            // prints an exception if the try/catch failed
            e.printStackTrace();
        }
    }
}
