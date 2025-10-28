package io.github.GabbyMoon.centroidfinder;
import java.awt.image.BufferedImage;

public record ImageFrame(BufferedImage image, long microsecondsTimestamp, double secondsTimestamp) {
    // The java CV grabber .getTimestamp is a long so we can convert this later during printing for readability to preserve the original data
    @Override
    public String toString() {
        return "Seconds since Video Start: " + secondsTimestamp + " Image Type: " + image.getType() + " Height: " + image.getHeight() + " Width: " + image.getWidth();
    }
}
