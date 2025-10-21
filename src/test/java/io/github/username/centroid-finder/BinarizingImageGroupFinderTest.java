// format pulled from previous assignment
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BinarizingImageGroupFinderTest {
    @Test
    void findConnectedGroupsOneGroup(){
        ColorDistanceFinder finder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(finder, 0x46AF09, 0);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder binaryGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);
        BufferedImage image = new BufferedImage( 10, 10, BufferedImage.TYPE_INT_RGB);
        image.setRGB(1, 1, 0x46AF09);
        image.setRGB(2, 1, 0x46AF09);
        image.setRGB(3, 1, 0x46AF09);
        image.setRGB(1, 2, 0x46AF09);
        image.setRGB(1, 3, 0x46AF09);
        image.setRGB(2, 3, 0x46AF09);
        image.setRGB(3, 2, 0x46AF09);
        image.setRGB(3, 3, 0x46AF09);
        image.setRGB(2, 2, 0x46AF09);

        List<Group> real = binaryGroupFinder.findConnectedGroups(image);
        List<Group> should = new ArrayList<>();
        should.add(new Group(9, new Coordinate(2, 2)));
        Assertions.assertEquals(should, real);
    }

    @Test
    void findConnectedGroupsMultiGroups(){
        ColorDistanceFinder finder = new EuclideanColorDistance();
        int target = 0xFABCDA;
        ImageBinarizer binarizer = new DistanceImageBinarizer(finder, target, 0);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder binaryGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);
        BufferedImage image = new BufferedImage( 10, 10, BufferedImage.TYPE_INT_RGB);

        image.setRGB(1, 1, target);
        image.setRGB(2, 1, target);
        image.setRGB(3, 1, target);
        image.setRGB(1, 2, target);
        image.setRGB(1, 3, target);
        image.setRGB(2, 3, target);
        image.setRGB(3, 2, target);
        image.setRGB(3, 3, target);
        image.setRGB(2, 2, target);
        image.setRGB(9, 6, target);

        List<Group> real = binaryGroupFinder.findConnectedGroups(image);
        List<Group> should = new ArrayList<>();
        should.add(new Group(9, new Coordinate(2, 2)));
        should.add(new Group(1, new Coordinate(9, 6)));
        Assertions.assertEquals(should, real);
    }

    @Test
    void findConnectedGroupsNoGroups(){
        ColorDistanceFinder finder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(finder, 0xFAFAFA, 0);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder binaryGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);
        BufferedImage image = new BufferedImage( 10, 10, BufferedImage.TYPE_INT_RGB);

        List<Group> real = binaryGroupFinder.findConnectedGroups(image);
        List<Group> should = new ArrayList<>();

        Assertions.assertEquals(should, real);
    }
}
