package io.github.GabbyMoon.centroidfinder;

public record FrameGroupData(long timestamp, Group largestGroup) implements Comparable<FrameGroupData>{
    // used comparison from Group class
    @Override
    public int compareTo(FrameGroupData other) {
        int comp = Long.compare(this.timestamp, other.timestamp);
        if (comp != 0) {
            return comp;
        }
        comp = Integer.compare(this.largestGroup.centroid().x(), other.largestGroup.centroid().x());
        if (comp != 0) {
            return comp;
        }
        return Integer.compare(this.largestGroup.centroid().y(), other.largestGroup.centroid().y());
    }

    public String toCsvRow() {
        // used AI to figure out the string formatting to print as seconds rather than microseconds
        return String.format("%.3f,%d,%d",
            this.timestamp / 1_000_000.0,
            this.largestGroup.centroid().x(),
            this.largestGroup.centroid().y());
    }
}
