package com.example.utils.math.geometry;

public class Point {
        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            boolean ret = false;
            if (obj instanceof Point) {
                Point point = (Point) obj;
                ret = x == point.getX() && y == point.getY();
            } else {
                ret = super.equals(obj);
            }
            return ret;
        }
    }