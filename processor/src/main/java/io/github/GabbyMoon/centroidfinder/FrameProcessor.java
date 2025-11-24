/**
 * A class responsible for processing a list of ImageFrame objects to find the largest group of connected pixels in each frame.
 * It uses a specified color distance threshold and target color to binarize the images and then identifies the largest group of connected pixels.
 * The results are stored in a list of FrameGroupData objects, which contain the timestamp and the largest group found in each frame.
 */
package io.github.GabbyMoon.centroidfinder;

import java.util.List;
import java.awt.image.BufferedImage;

public class FrameProcessor {
    private final List<ImageFrame> frames;
    private final ColorDistanceFinder finder = new EuclideanColorDistance();
    private final BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
    /**
     * Constructs a FrameProcessor with the given list of ImageFrame objects.
     * Each ImageFrame contains a timestamp and an image to be processed.
     * @param frames the list of ImageFrame objects to be processed
     */
    public FrameProcessor(List<ImageFrame> frames) {
        this.frames = frames;
    }
    public FrameProcessor() {
        this.frames = null;
    }

    /**
     * Processes the list of ImageFrame objects to find the largest group of connected pixels in each frame.
     * The method uses a specified color distance threshold and target color to binarize the images and then identifies the largest group of connected pixels.
     * The results are stored in the provided list of FrameGroupData objects, which contain the timestamp and the largest group found in each frame.
     * If no pixel groups from the binarized image are detected and returned, or the frame is null or empty, a default FrameGroupData with a timestamp of -1 and a group with size 0 and centroid at (-1, -1) is added to the list.
      * @param threshold the color distance threshold used for binarization
      * @param targetColor the target color used for binarization, represented as a 24-bit hex RGB integer (0xRRGGBB)
      * @param frameGroupData the list to store the results of processing each frame, containing the timestamp and the largest group found in each frame
      */

    public void groupDataProcessor(int threshold, int targetColor, List<FrameGroupData> frameGroupData) {
        ImageBinarizer binarizer = new DistanceImageBinarizer(this.finder, targetColor, threshold);
        BinarizingImageGroupFinder binaryGroupFinder = new BinarizingImageGroupFinder(binarizer, this.groupFinder);
        if(frames == null || frames.size() == 0) {
            frameGroupData.add(new FrameGroupData((long)-1.0, new Group(0, new Coordinate(-1, -1))));
            return;
        }

        for(ImageFrame frame : frames) {
            List<Group> binaryGroup = binaryGroupFinder.findConnectedGroups(frame.image());
            frameGroupData.add(new FrameGroupData(frame.microsecondsTimestamp(), binaryGroup.get(0)));
        }
    }
    /**
     * Processes a single BufferedImage to find the largest group of connected pixels.
     * The method uses a specified color distance threshold and target color to binarize the image with the image binarizer and then identifies the largest group of connected pixels with the binary group finder.
     * If no pixel groups from the binarized image are detected and returned, or the    frame is null or empty, a default Group with size 0 and centroid at (-1, -1) is returned.
     * @param threshold the color distance threshold used for binarization
     * @param targetColor the target color used for binarization, represented as a 24-bit hex RGB integer (0xRRGGBB)
     * @param image the BufferedImage to be processed
     * @return the Group data for the largest group of pixels, or a default group of (0, -1, -1) if no groups were detected 
     */
    public Group groupDataProcessor(int threshold, int targetColor, BufferedImage image) {
        ImageBinarizer binarizer = new DistanceImageBinarizer(this.finder, targetColor, threshold);
        BinarizingImageGroupFinder binaryGroupFinder = new BinarizingImageGroupFinder(binarizer, this.groupFinder);
        List<Group> binGroups = binaryGroupFinder.findConnectedGroups(image);
        if(binGroups == null || binGroups.size() == 0) {
            return new Group(0, new Coordinate(-1, -1));
        }
        return binGroups.get(0);
    }
}
