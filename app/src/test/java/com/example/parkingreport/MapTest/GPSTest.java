package com.example.parkingreport.MapTest;

import com.example.parkingreport.utils.GPS;
import android.location.Location;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the GPS utility class, specifically the isValidLocation(...) method.
 * This method checks whether a given Location object has valid latitude and longitude,
 * and whether it falls outside of the excluded coordinates (default 0,0 or San Jose area).
 *
 * Authored by Larry Wang u7807744
 */

public class GPSTest {

    // Test that a null Location object is considered invalid.
    @Test public void nullLocation_isInvalid() {
        assertFalse(GPS.isValidLocation(null));
    }

    // Test that the default Location (0.0, 0.0) is rejected.
    @Test public void zeroZero_isInvalid() {
        Location loc = mock(Location.class);
        when(loc.getLatitude()).thenReturn(0.0);
        when(loc.getLongitude()).thenReturn(0.0);
        assertFalse(GPS.isValidLocation(loc));
    }

    // Test a location inside the hardcoded exclusion zone (San Jose-like coordinates).
    @Test public void insideExcludedZone_isInvalid() {
        Location loc = mock(Location.class);
        // The coordinates are in the San Jose area of 37.4~37.5, -123~ -121
        when(loc.getLatitude()).thenReturn(37.45);
        when(loc.getLongitude()).thenReturn(-122.0);
        assertFalse(GPS.isValidLocation(loc));
    }

    // Test a valid location far away from exclusions.
    @Test public void normalPoint_isValid() {
        Location loc = mock(Location.class);
        when(loc.getLatitude()).thenReturn(40.0);
        when(loc.getLongitude()).thenReturn(10.0);
        assertTrue(GPS.isValidLocation(loc));
    }
}
