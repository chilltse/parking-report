package com.example.parkingreport.MapTest;
import com.example.parkingreport.utils.PolygonUtil;
import com.google.android.gms.maps.model.LatLng;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;


public class isPointInPolygon {

    // Construct the vertices of a square
    private final List<LatLng> square = Arrays.asList(
            new LatLng(0, 0),
            new LatLng(0, 1),
            new LatLng(1, 1),
            new LatLng(1, 0)
    );

    @Test public void pointInsideSquare_returnsTrue() {
        LatLng p = new LatLng(0.5, 0.5);
        assertTrue(PolygonUtil.isPointInPolygon(p, square));
    }

    @Test public void pointOnEdge_returnsTrue() {
        LatLng p = new LatLng(0, 0.5);
        assertTrue(PolygonUtil.isPointInPolygon(p, square));
    }

    @Test public void pointOutside_returnsFalse() {
        LatLng p = new LatLng(2, 2);
        assertFalse(PolygonUtil.isPointInPolygon(p, square));
    }
}
