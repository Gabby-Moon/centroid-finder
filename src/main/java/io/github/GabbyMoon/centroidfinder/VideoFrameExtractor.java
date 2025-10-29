package io.github.GabbyMoon.centroidfinder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Frame;

public class VideoFrameExtractor {

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

        return frames;
    }
}

