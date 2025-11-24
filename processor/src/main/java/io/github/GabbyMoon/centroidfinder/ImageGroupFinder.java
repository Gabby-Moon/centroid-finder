/**
 * The ImageGroupFinder interface defines a method for finding connected groups of pixels in a BufferedImage.
 * Implementations of this interface will provide different algorithms for identifying connected groups.
 */
package io.github.GabbyMoon.centroidfinder;

import java.util.List;
import java.awt.image.BufferedImage;

public interface ImageGroupFinder {
    /**
     * Finds connected groups in an image.
     * 
     * The groups are sorted in DESCENDING order according to Group's compareTo method.
     * @param image
     * @return connected groups in an image sorted in descending order
     */
    public List<Group> findConnectedGroups(BufferedImage image);
}
