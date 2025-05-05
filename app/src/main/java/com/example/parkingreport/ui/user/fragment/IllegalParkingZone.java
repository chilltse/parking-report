package com.example.parkingreport.ui.user.fragment;
import com.google.android.gms.maps.model.LatLng;

//Enum some illegal parking areas (only show the method, not practical significance)
public enum IllegalParkingZone {
    HARRISON(new LatLng[]{
            new LatLng(-35.178, 149.137),
            new LatLng(-35.178, 149.150),
            new LatLng(-35.190, 149.150),
            new LatLng(-35.190, 149.137)
    }),
    CANBERRA_CBD(new LatLng[]{
            new LatLng(-35.276, 149.122),
            new LatLng(-35.276, 149.140),
            new LatLng(-35.295, 149.140),
            new LatLng(-35.295, 149.122)
    }),
    DEPOT_MARKS(new LatLng[]{
            new LatLng(-35.314, 149.110),
            new LatLng(-35.314, 149.125),
            new LatLng(-35.330, 149.125),
            new LatLng(-35.330, 149.110)
    });
    private final LatLng[] vertices;

    IllegalParkingZone(LatLng[] vertices) {
        this.vertices = vertices;
    }

    public LatLng[] getVertices() {
        return vertices;
    }

    public String getZoneName() {
        return this.name();  //Returns the name of the Enum directly (HARRISON, CANBERRA_CITY, etc.)
    }
}