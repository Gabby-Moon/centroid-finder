package main.java.io.github.GabbyMoon.centroidfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {
   /**
    * Finds connected pixel groups of 1s in an integer array representing a binary image.
    * 
    * The input is a non-empty rectangular 2D array containing only 1s and 0s.
    * If the array or any of its subarrays are null, a NullPointerException
    * is thrown. If the array is otherwise invalid, an IllegalArgumentException
    * is thrown.
    *
    * Pixels are considered connected vertically and horizontally, NOT diagonally.
    * The top-left cell of the array (row:0, column:0) is considered to be coordinate
    * (x:0, y:0). Y increases downward and X increases to the right. For example,
    * (row:4, column:7) corresponds to (x:7, y:4).
    *
    * The method returns a list of sorted groups. The group's size is the number 
    * of pixels in the group. The centroid of the group
    * is computed as the average of each of the pixel locations across each dimension.
    * For example, the x coordinate of the centroid is the sum of all the x
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * Similarly, the y coordinate of the centroid is the sum of all the y
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * The division should be done as INTEGER DIVISION.
    *
    * The groups are sorted in DESCENDING order according to Group's compareTo method.
    * 
    * @param image a rectangular 2D array containing only 1s and 0s
    * @return the found groups of connected pixels in descending order
    */
    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        List<Group> groupList = new ArrayList<>();

        boolean[][] visit = new boolean[image.length][image[0].length];
        findConnectedGroupsHelper(image, groupList, visit);
        return groupList;
    }
    public void findConnectedGroupsHelper(int[][] image, List<Group> groupList, boolean[][] visit) {
        
        for(int y = 0; y < image.length; y++) {
            for(int x = 0; x < image[y].length; x++) {
                List<Coordinate> points = new ArrayList<>();
                if(image == null || image[y] == null) {
                    throw new NullPointerException();
                }
                if(image[y][x] != 1 && image[y][x] != 0) {
                    throw new IllegalArgumentException();
                }
                if(!visit[y][x] && image[y][x] == 1) {
                    dfs(image, groupList, visit, points, y, x);


                    Coordinate center = centerFinder(points);
                    Group group = new Group(points.size(), center);
                    groupList.add(group);
                    //System.out.println(groupList.toString());
                    //System.out.println();
                }
            }
        }
        Collections.sort(groupList, Comparator.reverseOrder());
        
        // if(x + 1 < image.length){
        //     findConnectedGroupsHelper(image, groups, visit, x + 1, y);
        // }
        // if(y + 1 < image[x].length) {
        //     findConnectedGroupsHelper(image, groups, visit, x, y + 1);
        // }
    }
    // public void dfs(int[][] image, List<Group> groupsList, boolean[][] visit, List<Coordinate> points, int y, int x) {
    //     if(visit[y][x]){
    //         return;
    //     }
    //     points.add(new Coordinate(x, y));
    //     visit[y][x] = true;
    //     List<Coordinate> moves = movesFinder(image, y, x);
    //     for (Coordinate coordinate : moves) {
    //         if(!visit[coordinate.y()][coordinate.x()]) {
    //             dfs(image, groupsList, visit, points, coordinate.y(), coordinate.x());
    //         }
            
    //     }
    // }
    public void dfs(int[][] image, List<Group> groupList, boolean[][] visit, List<Coordinate> points, int y, int x) {
        Queue<Coordinate> checks = new LinkedList<>();
        checks.add(new Coordinate(x, y));

        // no stack overflow because of queues
        while (!checks.isEmpty()) {
            Coordinate curr = checks.poll();
            if(visit[curr.y()][curr.x()]){
                continue;
            }
            points.add(curr);
            visit[curr.y()][curr.x()] = true;
            List<Coordinate> moves = movesFinder(image, curr.y(), curr.x());
            for(Coordinate coordinate : moves) {
                if(!visit[coordinate.y()][coordinate.x()]) {
                    checks.add(coordinate);
                }
            }
            
        }
    }

    public List<Coordinate> movesFinder(int[][] image, int y, int x) {
        int[][] moves = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
        };

        List<Coordinate> moveOptions = new ArrayList<>();

        for(int[] move : moves) {
            int newX = x + move[0];
            int newY = y + move[1];
            if(newY >= 0 && newY < image.length && newX >= 0 && newX < image[newY].length && image[newY][newX] == 1) {
                moveOptions.add(new Coordinate(newX, newY));
            }
        }
        return moveOptions;
    }
    public Coordinate centerFinder(List<Coordinate> points) {
        int xTotal = 0;
        int yTotal = 0;
        for(Coordinate point : points) {
            xTotal += point.x();
            yTotal += point.y();
        }
        xTotal = xTotal / points.size();
        yTotal = yTotal / points.size();

        return new Coordinate(xTotal, yTotal);
    }
}
