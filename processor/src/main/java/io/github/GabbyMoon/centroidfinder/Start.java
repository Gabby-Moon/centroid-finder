package io.github.GabbyMoon.centroidfinder;

public class Start {
    public static void main(String[] args) {
        if (args.length == 4) {
            VideoProcessingApp.main(args);
        } else if (args.length == 2) {
            ThumbnailsProcessingApp.main(args);
        } else {
            throw new IllegalArgumentException("Incorrect number of arguments: " + args.length + ".");
        }
    }
}
