package io.github.GabbyMoon.centroidfinder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.bytedeco.javacv.FrameGrabber;

public class FrameListWriter {
    public static void writeFramesToCsv(List<ImageFrame> frames, String filePath) throws IOException, FrameGrabber.Exception {
        PrintWriter writer = new PrintWriter(filePath);
        writer.println("seconds, x, y");
        for (ImageFrame frame : frames) {
            writer.println(frame.secondsTimestamp() + ", " + frame.group().centroid().x() + ", " + frame.group().centroid().y() );
        }
        writer.close();
    }
}
