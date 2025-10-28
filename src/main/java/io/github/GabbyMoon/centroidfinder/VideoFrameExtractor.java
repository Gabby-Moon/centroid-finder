package io.github.GabbyMoon.centroidfinder;


import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Frame;

public class VideoFrameExtractor {
    private final FFmpegFrameGrabber grabber;
    private final ArrayList<ImageFrame> frames = new ArrayList<>();
    private final Java2DFrameConverter converter = new Java2DFrameConverter();

    public VideoFrameExtractor(String filepath) throws FrameGrabber.Exception  {
        grabber = new FFmpegFrameGrabber(filepath); // create a new framegrabber
        grabber.setPixelFormat(avutil.AV_PIX_FMT_RGB24); // set the image format to RGB
    }

    public ArrayList<ImageFrame> extractFrames() throws FrameGrabber.Exception {
        // start the grabber
        grabber.start();
        long interval = 60_000_000; // the interval is every 60 seconds
        long duration = grabber.getLengthInTime(); // check video length
        
        // loop over the video
        for (long microseconds = 0; microseconds <= duration; microseconds+=interval) {
            grabber.setTimestamp(microseconds); //seek to the next location we're doing a screengrab
            long actualTimestamp = grabber.getTimestamp(); // this can apparently vary from the requested (set) value due to codec things?
            double seconds = actualTimestamp / 1_000_000.0; // recording seconds so this is easier to read
            Frame frame = null;
            try {
                frame = grabber.grabImage();

                if (frame != null && frame.image != null) {
                    try {
                        BufferedImage image = converter.convert(frame);
                        frames.add(new ImageFrame(image, actualTimestamp, seconds));
                    } catch (Exception e) {
                        System.err.printf("Image conversion to buffered image failed at %.2f seconds\n", seconds);
                        frames.add(new ImageFrame(null, actualTimestamp, seconds));
                    }
                    
                } else {
                    System.err.printf("Null framegrab at %.2f seconds\n", seconds);
                    frames.add(new ImageFrame(null, actualTimestamp, seconds));
                }
            } catch(Exception e) {
                System.err.printf("Failed to grab frame at %.2f seconds:\n", seconds, e.getMessage());
                frames.add(new ImageFrame(null, actualTimestamp, seconds));
            }
        }

        grabber.stop();
        return frames;
    }
}
