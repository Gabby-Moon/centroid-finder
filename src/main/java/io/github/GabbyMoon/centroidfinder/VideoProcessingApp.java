package io.github.GabbyMoon.centroidfinder;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacv.FrameGrabber;

public class VideoProcessingApp {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java VideoProcessingApp <input_video_path> <output_csv_path> <hex_target_color> <threshhold>");
            return;
        }

        String inputPath = args[0];
        String outputPath = args[1];
        String targetColorInput = args[3];
        String thresholdInput = args[4];

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
            VideoFrameExtractor extractor = new VideoFrameExtractor(inputPath);
            frames = extractor.extractFrames();
        } catch (FrameGrabber.Exception e) {
            System.err.println("Video extraction failed: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        List<FrameGroupData> frameGroupData = new ArrayList<>();
        // pass frames to next step after checking that frames contains at least one frame
        if (frames != null & frames.size() > 0) {
            // call the frame processor and and pass 
            // groupDataProcessor(threshold, targetColor, frameGroupData); << pass in threshold and target color and then add the frame group data to the list in the method?
            FrameProcessor processor = new FrameProcessor(frames);
            processor.groupDataProcessor(threshold, targetColor, frameGroupData);
        } else {
            System.err.println("No frames available for processing: " + frames.toString());
            return;
        }

        // pass the Frame Group data to a writer process to write to csv
        if (frameGroupData != null && frameGroupData.size() > 0) {
            try (PrintWriter writer = new PrintWriter(outputPath)) {
                for (FrameGroupData frameData : frameGroupData) {
                    writer.println(frameData.toCsvRow());
                }
                System.out.println("Groups summary saved as groups.csv");
            } catch (Exception e) {
                System.err.println("Error writing groups.csv");
                e.printStackTrace();
                return;
            }
        } else {
            System.err.println("No groups found: " + frames.toString());
        }
    }
}
