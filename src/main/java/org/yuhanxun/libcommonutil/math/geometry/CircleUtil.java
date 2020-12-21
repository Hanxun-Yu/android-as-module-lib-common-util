package org.yuhanxun.libcommonutil.math.geometry;

/**
 * Created by yuhanxun
 * 2018/6/12
 */
public class CircleUtil {
    public static boolean isCircleContainsPoint(Point circleCenter, double radius, Point targetPoint) {
        boolean ret;
        double distance = Math.sqrt(Math.pow(circleCenter.getX() - targetPoint.getX(), 2)
                + Math.pow(circleCenter.getY() - targetPoint.getY(), 2));
        ret = radius > distance;
        return ret;
    }
}
