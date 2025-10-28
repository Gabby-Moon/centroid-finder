package io.github.GabbyMoon.centroidfinder;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

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
        }
        
        int threshold = 0;
        try {
            threshold = Integer.parseInt(thresholdInput);
        } catch (NumberFormatException e) {
            System.err.println("Threshold argument ( " + threshold + ") must be an integer.");
        }

        List<ImageFrame> frames = null;
        try {
            VideoFrameExtractor extractor = new VideoFrameExtractor(inputVideoString);
            frames = extractor.extractFrames();
        } catch (FrameGrabber.Exception e) {
            System.err.println("Video extraction failed: " + e.getMessage());
            e.printStackTrace();
        }

        List<FrameGroupData> groupData = new ArrayList<>();
        // pass frames to next step after checking that frames contains at least one frame
        if (frames != null & frames.size() > 0) {
            // call the frame processor and return to groupdata
            // groupData = 
        } else {
            System.err.println("No frames extracted: " + frames.toString());
        }

        // pass the Frame Group data to a writer process to write to csv
        if (groupData != null && groupData.size() > 0) {
            try (PrintWriter writer = new PrintWriter(out)) {
            for (Group group : groups) {
                writer.println(group.toCsvRow());
            }
            System.out.println("Groups summary saved as groups.csv");
        } catch (Exception e) {
            System.err.println("Error writing groups.csv");
            e.printStackTrace();
        }
        } else {
            System.err.println("No groups found: " + frames.toString());
        }
        







    }
}
