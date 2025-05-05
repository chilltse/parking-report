package com.example.parkingreport.ui.user.fragment;
import com.google.android.gms.maps.model.LatLng;

//Enum some illegal parking areas (only show the method, not practical significance)
public enum IllegalParkingZone {
    MARCUS_CLARK_St(new LatLng[]{
            new LatLng(-35.275707, 149.12666),
            new LatLng(-35.275707, 149.1270),
            new LatLng(-35.27853, 149.12650723),
            new LatLng(-35.27853, 149.1261743),
    }),
    Hanna_Neumann(new LatLng[]{
            new LatLng(-35.2750666, 149.120955),
            new LatLng(-35.2754769, 149.121058),
            new LatLng(-35.2757728, 149.120979),
            new LatLng(-35.2767935, 149.120048),
            new LatLng(-35.2782042, 149.122462),
            new LatLng(-35.2780175, 149.122587),
            new LatLng(-35.2771717, 149.1209758),
            new LatLng(-35.2766692, 149.120473),
            new LatLng(-35.2757274, 149.121191),
    }),
    Bruce_Hall(new LatLng[]{
            new LatLng(-35.2738012, 149.117563),
            new LatLng(-35.2770527, 149.114728),
            new LatLng(-35.2780126, 149.114765),
            new LatLng(-35.2777077, 149.115114),
            new LatLng(-35.2764253, 149.115705),
            new LatLng(-35.2754515, 149.116767),
            new LatLng(-35.2763068, 149.1187130),
            new LatLng(-35.27589083, 149.118964),
            new LatLng(-35.27478529, 149.1173598),
            new LatLng(-35.2736354, 149.1183117),
    }),
    Fellows_Oval(new LatLng[]{
            new LatLng(-35.27796, 149.11901),
            new LatLng(-35.27897, 149.11822),
            new LatLng(-35.27977, 149.11952),
            new LatLng(-35.27868, 149.12036)
    }),
    London_circuit(new LatLng[]{
            new LatLng(-35.279761, 149.127176),
            new LatLng(-35.280252, 149.130515),
            new LatLng(-35.282142, 149.131673),
            new LatLng(-35.281797, 149.132453),
            new LatLng(-35.279745, 149.131179),
            new LatLng(-35.279118, 149.127443),
    }),
    Braddon(new LatLng[]{
            new LatLng(-35.27048, 149.13210),
            new LatLng(-35.27759, 149.13092),
            new LatLng(-35.27818, 149.13272),
            new LatLng(-35.27540, 149.13338),
            new LatLng(-35.27086, 149.13394)
    }),

    MacArthur_Ave(new LatLng[]{
            new LatLng(-35.25953, 149.11654),
            new LatLng(-35.26150, 149.13575),
            new LatLng(-35.26028, 149.13580),
            new LatLng(-35.25801, 149.11513)
    }),

    Gungahlin(new LatLng[]{
            new LatLng(-35.178, 149.137),
            new LatLng(-35.178, 149.150),
            new LatLng(-35.190, 149.150),
            new LatLng(-35.190, 149.137)
    }),
    Mount_Ainslie(new LatLng[]{
            new LatLng(-35.254, 149.164),
            new LatLng(-35.275, 149.146),
            new LatLng(-35.284, 149.166),
            new LatLng(-35.263, 149.182)
    }),
    Wild_Reservation(new LatLng[]{
            new LatLng(-35.243, 148.943),
            new LatLng(-35.465, 148.930),
            new LatLng(-35.588, 149.053),
            new LatLng(-35.458, 149.090),
            new LatLng(-35.371, 149.038),
            new LatLng(-35.290, 149.036),
    }),
    Mount_Black(new LatLng[]{
            new LatLng(-35.283, 149.086),
            new LatLng(-35.280, 149.109),
            new LatLng(-35.253, 149.111),
            new LatLng(-35.250, 149.090),
            new LatLng(-35.270, 149.080),
    }),
    Parliament(new LatLng[]{
            new LatLng(-35.3112, 149.1156),
            new LatLng(-35.3128, 149.1304),
            new LatLng(-35.3033, 149.1299),
            new LatLng(-35.2987, 149.1214),
            new LatLng(-35.3037, 149.1185),
            new LatLng(-35.3094, 149.1143),

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