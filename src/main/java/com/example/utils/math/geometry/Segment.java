package com.example.utils.math.geometry;

public class Segment {
    public Segment(Point[] twoPoints) {
        this.point_1 = twoPoints[0];
        this.point_2 = twoPoints[1];
    }

    public Segment(Point point1, Point point2) {
        this.point_1 = point1;
        this.point_2 = point2;
    }

    /**
     * @return if y == y return leftPoint
     */
    public Point getTopPoint() {
        if (point_1.getY() == point_2.getY())
            return getLeftPoint();
        return null;

    }

    /**
     * @return if y == y return rightPoint
     */
    public Point getDownPoint() {
        return null;

    }

    /**
     * @return if x == x return topPoint
     */
    public Point getLeftPoint() {
        return null;
    }

    /**
     * @return if x == x return bottomPoint
     */
    public Point getRightPoint() {
        return null;

    }

    public boolean isPointOnLine() {
        boolean ret = false;
        return ret;
    }

    private Point point_1;
    private Point point_2;
}