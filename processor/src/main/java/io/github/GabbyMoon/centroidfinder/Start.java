package io.github.GabbyMoon.centroidfinder;
/**
 * The Start class serves as the entry point for the application.
 * It determines which application to run based on the number of command-line arguments provided.
 */
public class Start {
    /**
     * The main method of the Start class.
     * It checks the number of command-line arguments and invokes the appropriate application.
     * If 4 arguments are provided, it runs the VideoProcessingApp.
     * If 2 arguments are provided, it runs the ThumbnailsProcessingApp.
     * If the number of arguments is incorrect, it throws an IllegalArgumentException.
     *
     * @param args Command-line arguments
     * @throws IllegalArgumentException if the number of arguments is not 2 or 4
     */
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
