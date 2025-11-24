/**
 * A utility class for writing a list of ImageFrame objects to a CSV file.
 * Each ImageFrame contains a timestamp and a Group representing the largest group of pixels found in that frame.
 * The CSV file will have a header row and each subsequent row will contain the timestamp in seconds, and the x and y coordinates of the largest group's centroid.
 */
package io.github.GabbyMoon.centroidfinder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.bytedeco.javacv.FrameGrabber;

public class FrameListWriter {
    /**
     * Writes a list of ImageFrame objects to a CSV file.
     * Each row in the CSV file will contain the timestamp in seconds, and the x and y coordinates of the largest group's centroid.
     * @param frames the list of ImageFrame objects to be written to the CSV file
     * @param filePath the path to the output CSV file
     * @throws IOException if there is an error writing to the file
     * @throws FrameGrabber.Exception if there is an error grabbing frames from the video
     */
    public static void writeFramesToCsv(List<ImageFrame> frames, String filePath) throws IOException, FrameGrabber.Exception {
        PrintWriter writer = new PrintWriter(filePath);
        writer.println("seconds, x, y");
        for (ImageFrame frame : frames) {
            writer.println(frame.secondsTimestamp() + ", " + frame.group().centroid().x() + ", " + frame.group().centroid().y() );
        }
        writer.close();
    }
}
