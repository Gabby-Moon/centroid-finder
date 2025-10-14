// format pulled from previous assignment
import static org.junit.Assert.assertEquals;

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
        List<Coordinate> pixelSet = new ArrayList<>();

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

        // build the dfs group finder so we can test it
        DfsBinaryGroupFinder dfsGroupFinder = new DfsBinaryGroupFinder();
        List<Group> testGroupList = dfsGroupFinder.findConnectedGroups(image);


        // since these are comparable, we can use assertEquals
        Assertions.assertEquals(groupList, testGroupList);
    }

    @Test
    void test_centerFinder() {
        DfsBinaryGroupFinder dfsGroupFinder = new DfsBinaryGroupFinder();

        List<Coordinate> points = new ArrayList<>();
        points.add(new Coordinate(0, 0));
        points.add(new Coordinate(1, 0));
        points.add(new Coordinate(2, 0));
        points.add(new Coordinate(0, 1));
        points.add(new Coordinate(1, 1));
        points.add(new Coordinate(2, 1));
        points.add(new Coordinate(0, 2));
        points.add(new Coordinate(1, 2));
        points.add(new Coordinate(2, 2));

        // Coordinate center = helper_CentroidFInder(points);
        Coordinate center = dfsGroupFinder.centerFinder(points);

        // center of a 3x3 grid starting at 0, 0 will be 1, 1
        assertEquals(center, new Coordinate(1, 1));
    }

    @Test
    void test_GroupCorrectlySorted() {
        DfsBinaryGroupFinder dfsGroupFinder = new DfsBinaryGroupFinder();

        int[][] image = {
                // a 
            // y, x 0  1  2  3  4  5  6  7  8
            /* 0 */{1, 1, 1, 0, 0, 0, 0, 1, 1}, // e
            /* 1 */{1, 1, 1, 0, 0, 0, 0, 1, 1},
            /* 2 */{1, 1, 1, 0, 0, 0, 0, 0, 0},
            /* 3 */{0, 0, 0, 0, 0, 1, 1, 0, 0}, // d
            /* 4 */{0, 0, 0, 0, 0, 1, 1, 0, 0},
            /* 5 */{0, 0, 0, 0, 0, 0, 0, 0, 0}, // c
            /* 6 */{0, 0, 0, 0, 0, 0, 0, 0, 0},
            /* 7 */{1, 1, 0, 1, 1, 0, 0, 0, 0}, // b
            /* 8 */{1, 1, 0, 1, 1, 0, 0, 0, 0},
        };


        Group a = new Group(9, new Coordinate(1, 1));
        Group b = new Group(4, new Coordinate(0, 7));
        Group c = new Group(4, new Coordinate(3, 7));
        Group d = new Group(4, new Coordinate(5, 3));
        Group e = new Group(4, new Coordinate(7, 0));
        List<Group> testGroup = new ArrayList<>();
        testGroup.add(a);
        testGroup.add(b);
        testGroup.add(c);
        testGroup.add(d);
        testGroup.add(e);

        // sorted group for this set based on size then x, then y, should be a, b, c, d, e order
        List<Group> group = dfsGroupFinder.findConnectedGroups(image);
        assertEquals(group, testGroup);
    }

    Coordinate helper_CentroidFInder(List<Coordinate> pixelSet) {
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
