/**
 * Defines a record which represents a location in an image or array.
 * The top-left cell of the array/image (row:0, column:0) is considered to be coordinate (x:0, y:0).
 * Y increases downward and X increases to the right.
 *
 * For example, (row:4, column:7) corresponds to (x:7, y:4).
 */
package io.github.GabbyMoon.centroidfinder;

/**
 * A record representing a coordinate in an image or array.
 */
public record Coordinate(int x, int y) {}
