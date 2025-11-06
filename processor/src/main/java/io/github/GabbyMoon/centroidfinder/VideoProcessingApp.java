package io.github.GabbyMoon.centroidfinder;

import java.util.List;

import org.bytedeco.javacv.FrameGrabber;

public class VideoProcessingApp {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java VideoProcessingApp <input_video_path> <output_csv_path> <hex_target_color> <threshold>");
            return;
        }

        String inputPath = args[0];
        String outputPath = args[1];
        String targetColorInput = args[2];
        String thresholdInput = args[3];

        int targetColor = 0;
        try {
            targetColor = Integer.parseInt(targetColorInput, 16);
        } catch (NumberFormatException e) {
            System.err.println("Invalid hex target color. Please provide a color in RRGGBB format.");
            return;
        }
        
        int threshold = 0;
        try {
            threshold = Integer.parseInt(thresholdInput);
        } catch (NumberFormatException e) {
            System.err.println("Threshold argument ( " + threshold + ") must be an integer.");
            return;
        }

        List<ImageFrame> frames = null;
        try {
            VideoFrameExtractor extractor = new VideoFrameExtractor();
            frames = extractor.extractFrames(inputPath, threshold, targetColor);
        } catch (FrameGrabber.Exception e) {
            System.err.println("Video extraction failed: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // pass the ImageFrame data to a writer process to write to csv
        if (frames != null && frames.size() > 0) {
           try {
             FrameListWriter.writeFramesToCsv(frames, outputPath);
             System.out.println("Timestamps and x, y coordinates saved to: " + outputPath);
           } catch (Exception e) {
                // do something
                System.err.println("Unable to save timestamps to: " + outputPath);
                e.printStackTrace();
                return;
           }
        } else {
            System.err.println("No frames found: " + frames.toString());
        }
    }
}
