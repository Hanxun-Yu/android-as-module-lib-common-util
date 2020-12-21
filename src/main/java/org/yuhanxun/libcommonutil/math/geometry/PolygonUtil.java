package org.yuhanxun.libcommonutil.math.geometry;

import java.util.List;

/**
 * Created by yuhanxun
 * 2018/5/17
 */
public class PolygonUtil {


    public Polygon createPolygon(List<? extends Point> points) {
        Polygon ret = null;
        if (points == null || points.isEmpty() || points.size() < 3) {
            return ret;
        }

        ret = new Polygon(points);
        return ret;
    }

    public static boolean isPolygonContainsPoint(List<? extends Point> mPoints, Point point) {
        int nCross = 0;
        for (int i = 0; i < mPoints.size(); i++) {
            Point p1 = mPoints.get(i);
            Point p2 = mPoints.get((i + 1) % mPoints.size());
            if (p1.getY() == p2.getY())
                continue;
            if (point.getY() < Math.min(p1.getY(), p2.getY()))
                continue;
            if (point.getY() >= Math.max(p1.getY(), p2.getY()))
                continue;
            double x = (point.getY() - p1.getY()) * (p2.getX() - p1.getX()) / (p2.getY() - p1.getY()) + p1.getX();
            if (x > point.getX())
                nCross++;
        }
        return (nCross % 2 == 1);
    }

    public static boolean isPointInPolygonBoundary(List<? extends Point> mPoints, Point point) {
        for (int i = 0; i < mPoints.size(); i++) {
            Point p1 = mPoints.get(i);
            Point p2 = mPoints.get((i + 1) % mPoints.size());

            if (point.getY() < Math.min(p1.getY(), p2.getY()))
                continue;
            if (point.getY() > Math.max(p1.getY(), p2.getY()))
                continue;
            if (p1.getY() == p2.getY()) {
                double minX = Math.min(p1.getX(), p2.getX());
                double maxX = Math.max(p1.getX(), p2.getX());
                if ((point.getY() == p1.getY()) && (point.getX() >= minX && point.getX() <= maxX)) {
                    return true;
                }
            } else {
                double x = (point.getY() - p1.getY()) * (p2.getX() - p1.getX()) / (p2.getY() - p1.getY()) + p1.getX();
                if (x == point.getX())
                    return true;
            }
        }
        return false;
    }
}
