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
    public FrameProcessor() {
        this.frames = null;
    }

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
