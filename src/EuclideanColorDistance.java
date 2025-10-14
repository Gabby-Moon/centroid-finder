public class EuclideanColorDistance implements ColorDistanceFinder {
    /**
     * Returns the euclidean color distance between two hex RGB colors.
     * 
     * Each color is represented as a 24-bit integer in the form 0xRRGGBB, where
     * RR is the red component, GG is the green component, and BB is the blue component,
     * each ranging from 0 to 255.
     * 
     * The Euclidean color distance is calculated by treating each color as a point
     * in 3D space (red, green, blue) and applying the Euclidean distance formula:
     * 
     * sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
     * 
     * This gives a measure of how visually different the two colors are.
     * 
     * @param colorA the first color as a 24-bit hex RGB integer
     * @param colorB the second color as a 24-bit hex RGB integer
     * @return the Euclidean distance between the two colors
     */
    @Override
    public double distance(int colorA, int colorB) {
        // isolate red by masking and shifting 16 bits
        // one of the other groups mentioned an alpha channel so masking that just in case
        int r1 = (colorA & 0xFF0000) >> 16;
        int r2 = (colorB & 0xFF0000) >> 16; 

        // isolate green and shifting 8 bits
        int g1 = (colorA & 0x00FF00) >> 8;
        int g2 = (colorB & 0x00FF00) >> 8;

        // isolate blue by masking, no shift required
        int b1 = (colorA & 0x0000FF);
        int b2 = (colorB & 0x0000FF);

        double distance = Math.sqrt(((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2));

        return distance;
    }
}
