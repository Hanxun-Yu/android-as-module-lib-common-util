package com.example.utils.math.geometry;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<? extends Point> points;

    public List<? extends Point> getPoints() {
        return points;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    private List<Segment> segments;

    public Polygon(List<? extends Point> points) {
        this.points = points;
        createSegment();
    }

    public boolean isPointInArea(Point point) {
        boolean ret = false;

        return ret;
    }

    private Segment createSegment(Point point1, Point point2) {
        Segment ret = null;
        if (point1 == null || point2 == null
                || point1.equals(point2))
            return null;

        ret = new Segment(point1, point2);
        return ret;
    }

    private void createSegment() {
        if (points != null && !points.isEmpty()) {
            segments = new ArrayList<>();
            Segment item = null;
            Point lastPoint = null;
            for (int i = 0; i < points.size(); i++) {
                if (lastPoint != null) {
                    item = createSegment(lastPoint, points.get(i));
                    segments.add(item);
                }
                lastPoint = points.get(i);
            }
        }
    }
}