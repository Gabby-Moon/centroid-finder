/**
 * A record class representing a single frame of a video, containing the image data, timestamp in microseconds, timestamp in seconds, and the largest group of connected pixels found in the frame.
 * The class implements the Comparable interface to allow sorting based on the timestamp.
 */
package io.github.GabbyMoon.centroidfinder;
import java.awt.image.BufferedImage;

public record ImageFrame(BufferedImage image, long microsecondsTimestamp, double secondsTimestamp, Group group) implements Comparable<ImageFrame> {
    /**
     * Constructs an ImageFrame with the given image, timestamp in microseconds, timestamp in seconds, and group data.
     * @param image the BufferedImage representing the frame's image data
     * @param microsecondsTimestamp the timestamp of the frame in microseconds
     * @param secondsTimestamp the timestamp of the frame in seconds
     * @param group the Group representing the largest group of connected pixels found in the frame
     */
    @Override
    public String toString() {
        return "Seconds since Video Start: " + secondsTimestamp + " Image Type: " + image.getType() + " Height: " + image.getHeight() + " Width: " + image.getWidth();
    }

    /**
     * Compares this ImageFrame to another based on their timestamps in microseconds.
     * @param other the other ImageFrame to compare to
     */
    @Override
    public int compareTo(ImageFrame other) {
        return Long.compare(this.microsecondsTimestamp, other.microsecondsTimestamp);
    }
}
