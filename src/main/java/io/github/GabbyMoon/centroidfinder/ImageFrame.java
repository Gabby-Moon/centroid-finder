package io.github.GabbyMoon.centroidfinder;
import java.awt.image.BufferedImage;

public record ImageFrame(BufferedImage image, long microsecondsTimestamp) {
    // The java CV grabber .getTimestamp is a long so we can convert this later during printing for readability to preserve the original data
    @Override
    public String toString() {
        return "Microseconds since Video Start: " + microsecondsTimestamp + " Image Type: " + image.getType() + " Height: " + image.getHeight() + " Width: " + image.getWidth();
    }
}
