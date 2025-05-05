package com.example.parkingreport.utils;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;


public class PolygonUtil {
    public static boolean isPointInPolygon(LatLng point, List<LatLng> polygon) {
        int intersectCount = 0;
        for (int j = 0; j < polygon.size() - 1; j++) {
            LatLng a = polygon.get(j);
            LatLng b = polygon.get(j + 1);

            if (rayCastIntersect(point, a, b)) {
                intersectCount++;
            }
        }
        // 封闭多边形，检查最后一条边
        if (rayCastIntersect(point, polygon.get(polygon.size() - 1), polygon.get(0))) {
            intersectCount++;
        }

        return (intersectCount % 2) == 1; // 奇数表示在内部
    }

    private static boolean rayCastIntersect(LatLng point, LatLng a, LatLng b) {
        double px = point.longitude;
        double py = point.latitude;
        double ax = a.longitude;
        double ay = a.latitude;
        double bx = b.longitude;
        double by = b.latitude;

        if (ay > by) {
            // swap a & b
            double tempX = ax, tempY = ay;
            ax = bx;
            ay = by;
            bx = tempX;
            by = tempY;
        }

        // point is outside of edge
        if (py == ay || py == by) {
            py += 1e-10;
        }

        if (py < ay || py > by) {
            return false;
        }

        if (px >= Math.max(ax, bx)) {
            return false;
        }

        if (px < Math.min(ax, bx)) {
            return true;
        }

        double red = (px - ax) / (bx - ax);
        double blue = (py - ay) / (by - ay);

        return red >= blue;
    }
}
