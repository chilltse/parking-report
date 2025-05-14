package com.example.parkingreport.MapTest;

import com.example.parkingreport.utils.GPS;
import android.location.Location;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GPSTest {

    @Test public void nullLocation_isInvalid() {
        assertFalse(GPS.isValidLocation(null));
    }

    @Test public void zeroZero_isInvalid() {
        Location loc = mock(Location.class);
        when(loc.getLatitude()).thenReturn(0.0);
        when(loc.getLongitude()).thenReturn(0.0);
        assertFalse(GPS.isValidLocation(loc));
    }

    @Test public void insideExcludedZone_isInvalid() {
        Location loc = mock(Location.class);
        // The coordinates are in the San Jose area of 37.4~37.5, -123~ -121
        when(loc.getLatitude()).thenReturn(37.45);
        when(loc.getLongitude()).thenReturn(-122.0);
        assertFalse(GPS.isValidLocation(loc));
    }

    @Test public void normalPoint_isValid() {
        Location loc = mock(Location.class);
        when(loc.getLatitude()).thenReturn(40.0);
        when(loc.getLongitude()).thenReturn(10.0);
        assertTrue(GPS.isValidLocation(loc));
    }
}
