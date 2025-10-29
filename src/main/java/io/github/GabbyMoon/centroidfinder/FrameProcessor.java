package io.github.GabbyMoon.centroidfinder;

import java.util.List;
import java.awt.image.BufferedImage;

public class FrameProcessor {
    private final List<ImageFrame> frames;
    private final ColorDistanceFinder finder = new EuclideanColorDistance();
    private final BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();

    public FrameProcessor(List<ImageFrame> frames) {
        this.frames = frames;
    }

    public void groupDataProcessor(int threshold, int targetColor, List<FrameGroupData> frameGroupData) {
        ImageBinarizer binarizer = new DistanceImageBinarizer(this.finder, targetColor, threshold);
        BinarizingImageGroupFinder binaryGroupFinder = new BinarizingImageGroupFinder(binarizer, this.groupFinder);

        for(ImageFrame frame : frames) {
            // assuming the given frames are in the correct time interval and not EVERY frame
            List<Group> binaryGroup = binaryGroupFinder.findConnectedGroups(frame.image());
            // passes in the largest group(at 0 is the largest) and the time
            frameGroupData.add(new FrameGroupData(frame.microsecondsTimestamp(), binaryGroup.get(0)));
        }
    }

    public Group groupDataProcessor(int threshold, int targetColor, BufferedImage image) {
        ImageBinarizer binarizer = new DistanceImageBinarizer(this.finder, targetColor, threshold);
        BinarizingImageGroupFinder binaryGroupFinder = new BinarizingImageGroupFinder(binarizer, this.groupFinder);
        List<Group> binGroups = binaryGroupFinder.findConnectedGroups(image);
        if(binGroups == null) {
            return null;
        }
        return binGroups.get(0);
    }
}
