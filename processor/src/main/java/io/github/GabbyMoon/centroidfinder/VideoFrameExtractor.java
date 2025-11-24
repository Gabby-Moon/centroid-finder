package io.github.GabbyMoon.centroidfinder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Frame;
/**
 * The VideoFrameExtractor class is responsible for extracting frames from a video file and processing them to find the largest group of connected pixels in each frame.
 * It uses the FFmpegFrameGrabber to read the video and the Java2DFrameConverter to convert the video frames to BufferedImages.
 * The extracted frames are processed using the FrameProcessor to find the largest group of connected pixels, and the results are stored in a list of ImageFrame objects.
 */
public class VideoFrameExtractor {
    /**
     * Extracts frames from a video file and processes them to find the largest group of connected pixels in each frame.
     * The method uses the FFmpegFrameGrabber to read the video and the Java2DFrameConverter to convert the video frames to BufferedImages.
     * The extracted frames are processed using the FrameProcessor to find the largest group of connected pixels, and the results are stored in a list of ImageFrame objects.
     * The method saves every nth frame based on the video's frame rate, where n is the rounded value of the frame rate.
     * @param filepath the path to the video file to be processed
     * @param threshold the threshold value used in processing the frames
     * @param hex_target_color the target color in hexadecimal format used in processing the frames
     * @return a list of ImageFrame objects representing the processed frames
     * @throws FrameGrabber.Exception if there is an error grabbing frames from the video
     */
    public List<ImageFrame> extractFrames(String filepath, int threshold, int hex_target_color) throws FrameGrabber.Exception {
        List<ImageFrame> frames = new ArrayList<>();

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filepath);
             Java2DFrameConverter converter = new Java2DFrameConverter()) {

            grabber.start();

            FrameProcessor processor = new FrameProcessor();
            double fps = grabber.getFrameRate();
            int nth = (int) Math.round(fps);
            System.out.printf("Frame rate: %.2f fps → saving every %dth frame%n", fps, nth);

            int current = 0;
            while (true) {
                Frame frame = grabber.grabImage();
                if (frame == null) break;
                current++;

                if (current % nth == 0) {
                    BufferedImage img = converter.convert(frame);
                    if (img != null) {
                        Group group = processor.groupDataProcessor(threshold, current, img);
                        ImageFrame imageData = new ImageFrame(img, frame.timestamp, frame.timestamp / 1_000_000, group);
                        frames.add(imageData);
                    }
                }
            }

            grabber.stop();
        }

        Collections.sort(frames);
        return frames;
    }
}

