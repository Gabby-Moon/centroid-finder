// format pulled from previous assignment
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DfsBinaryGroupFinderTest {
    @Test
    void test_locateSingleItem() {

        // example image
        int[][] image = {
            {1, 1, 0, 0, 0},
            {1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
        };

        //setting up coordinate sets to work through logic for for finding group centroid
        Coordinate pixel = new Coordinate(0, 0);
        Coordinate pixel2 = new Coordinate(1, 0);
        Coordinate pixel3 = new Coordinate(0, 1);
        Coordinate pixel4 = new Coordinate(1, 1);
        Set<Coordinate> pixelSet = new HashSet<>();

        pixelSet.add(pixel);
        pixelSet.add(pixel2);
        pixelSet.add(pixel3);
        pixelSet.add(pixel4);

        // find centroid
        Coordinate centroid = helper_CentroidFInder(pixelSet);

        // build a group
        Group groupOne = new Group(4, centroid);
        List<Group> groupList = new ArrayList<>();
        groupList.add(groupOne);

        // build an instance of the dfs group finder so we can test it
        DfsBinaryGroupFinder dfsGroupFinder = new DfsBinaryGroupFinder();
        List<Group> testGroupList = dfsGroupFinder.findConnectedGroups(image);


        // since these are comparable, we can use assertEquals
        Assertions.assertEquals(groupList, testGroupList);
    }

    Coordinate helper_CentroidFInder(Set<Coordinate> pixelSet) {
        int xTotal = 0;
        int yTotal = 0;
        int size = 0;
        for (Coordinate singlePixel : pixelSet) {
            xTotal+=singlePixel.x();
            yTotal+=singlePixel.y();
            size+=1;
        }

        int xCenter = xTotal / size;
        int yCenter = yTotal / size;

        return new Coordinate(xCenter, yCenter);
    }
}
