package io.github.GabbyMoon.centroidfinder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * The Image Summary Application.
 * 
 * This application takes three command-line arguments:
 * 1. The path to an input image file (for example, "image.png").
 * 2. A target hex color in the format RRGGBB (for example, "FF0000" for red).
 * 3. An integer threshold for binarization.
 * 
 * The application performs the following steps:
 * 
 * 1. Loads the input image.
 * 2. Parses the target color from the hex string into a 24-bit integer.
 * 3. Binarizes the image by comparing each pixel's Euclidean color distance to the target color.
 *    A pixel is marked white (1) if its distance is less than the threshold; otherwise, it is marked black (0).
 * 4. Converts the binary array back to a BufferedImage and writes the binarized image to disk as "binarized.png".
 * 5. Finds connected groups of white pixels in the binary image.
 *    Pixels are connected vertically and horizontally (not diagonally).
 *    For each group, the size (number of pixels) and the centroid (calculated using integer division) are computed.
 * 6. Writes a CSV file named "groups.csv" containing one row per group in the format "size,x,y".
 *    Coordinates follow the convention: (x:0, y:0) is the top-left, with x increasing to the right and y increasing downward.
 * 
 * Usage:
 *   java ImageSummaryApp <input_image> <hex_target_color> <threshold>
 */
public class ImageSummaryApp {
    /**
     * The main method of the Image Summary Application.
     * 
     * @param args Command-line arguments: <input_image> <hex_target_color> <threshold>
     * @throws IllegalArgumentException if the number of arguments is incorrect or if the threshold is not an integer.
     * @throws Exception if there are issues loading the image or writing the output files.
     * @throws  NumberFormatException if the hex target color is not in the correct format.
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java ImageSummaryApp <input_image> <hex_target_color> <threshold>");
            return;
        }
        
        String inputImagePath = args[0];
        String hexTargetColor = args[1];
        int threshold = 0;
        try {
            threshold = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
            return;
        }
        
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(new File(inputImagePath));
        } catch (Exception e) {
            System.err.println("Error loading image: " + inputImagePath);
            e.printStackTrace();
            return;
        }
        
        int targetColor = 0;
        try {
            targetColor = Integer.parseInt(hexTargetColor, 16);
        } catch (NumberFormatException e) {
            System.err.println("Invalid hex target color. Please provide a color in RRGGBB format.");
            return;
        }
        
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
        
        int[][] binaryArray = binarizer.toBinaryArray(inputImage);
        BufferedImage binaryImage = binarizer.toBufferedImage(binaryArray);
        
        try {
            ImageIO.write(binaryImage, "png", new File("binarized.png"));
            System.out.println("Binarized image saved as binarized.png");
        } catch (Exception e) {
            System.err.println("Error saving binarized image.");
            e.printStackTrace();
        }
        
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());
        
        List<Group> groups = groupFinder.findConnectedGroups(inputImage);
        
        try (PrintWriter writer = new PrintWriter("groups.csv")) {
            for (Group group : groups) {
                writer.println(group.toCsvRow());
            }
            System.out.println("Groups summary saved as groups.csv");
        } catch (Exception e) {
            System.err.println("Error writing groups.csv");
            e.printStackTrace();
        }
    }
}
