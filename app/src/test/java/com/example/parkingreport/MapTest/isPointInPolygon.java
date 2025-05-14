package com.example.parkingreport.MapTest;
import com.example.parkingreport.utils.PolygonUtil;
import com.google.android.gms.maps.model.LatLng;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Unit tests for the GPS utility class, specifically the isValidLocation(...) method.
 * This method checks whether a given Location object has valid latitude and longitude,
 * and whether it falls outside of the excluded coordinates (default 0,0 or San Jose area).
 *
 * Authored by Larry Wang u7807744
 */

public class isPointInPolygon {

    // Construct the vertices of a square
    private final List<LatLng> square = Arrays.asList(
            new LatLng(0, 0),
            new LatLng(0, 1),
            new LatLng(1, 1),
            new LatLng(1, 0)
    );

    // Point is inside
    @Test public void pointInsideSquare_returnsTrue() {
        LatLng p = new LatLng(0.5, 0.5);
        assertTrue(PolygonUtil.isPointInPolygon(p, square));
    }

    // Point lies exactly on one edge of the square
    @Test public void pointOnEdge_returnsTrue() {
        LatLng p = new LatLng(0, 0.5);
        assertTrue(PolygonUtil.isPointInPolygon(p, square));
    }

    //Point is outside
    @Test public void pointOutside_returnsFalse() {
        LatLng p = new LatLng(2, 2);
        assertFalse(PolygonUtil.isPointInPolygon(p, square));
    }

    //Point is outside
    @Test public void pointOutside2_returnsFalse() {
        LatLng p = new LatLng(25, 0.5);
        assertFalse(PolygonUtil.isPointInPolygon(p, square));
    }
}
