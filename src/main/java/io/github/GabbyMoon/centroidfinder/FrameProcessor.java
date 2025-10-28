package io.github.GabbyMoon.centroidfinder;

import java.util.List;

public class FrameProcessor {
    private final List<ImageFrame> frames;

    public FrameProcessor(List<ImageFrame> frames) {
        this.frames = frames;
    }

    public void groupDataProcessor(int threshold, int targetColor, List<FrameGroupData> frameGroupData) {
        ColorDistanceFinder finder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(finder, targetColor, threshold);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder binaryGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);
        for(ImageFrame frame : frames) {
            binaryGroupFinder.findConnectedGroups(frame.image());
        }
    }
}
