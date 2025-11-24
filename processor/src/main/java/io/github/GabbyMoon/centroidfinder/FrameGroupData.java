/**
 * A record which represents the data for a single image frame, including the timestamp and the largest group of pixels from
 * a binarized image found in that frame. The timestamp is represented in nanoseconds, and the largest group is an instance of 
 * the Group class. This record implements the Comparable interface to allow sorting based on timestamp, then x-coordinate of 
 * the largest group's centroid, and finally y-coordinate of the largest group's centroid.
 */
package io.github.GabbyMoon.centroidfinder;

public record FrameGroupData(long timestamp, Group largestGroup) implements Comparable<FrameGroupData>{
    /**
     * Compares this FrameGroupData to another based on timestamp, then x-coordinate of the largest group's centroid,
     * and finally y-coordinate of the largest group's centroid.
     * @param other the FrameGroupData to compare against
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
     */
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
    /**
     * Formats the FrameGroupData as a CSV row with the timestamp in seconds (to three decimal places),
     * and the x and y coordinates of the largest group's centroid.
     * @return a CSV formatted string representing the FrameGroupData
     */
    public String toCsvRow() {
        return String.format("%.3f,%d,%d",
            this.timestamp / 1_000_000.0,
            this.largestGroup.centroid().x(),
            this.largestGroup.centroid().y());
    }
}
