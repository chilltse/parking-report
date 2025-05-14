package com.example.parkingreport.ui.user.fragment;

import static com.example.parkingreport.utils.PolygonUtil.isPointInPolygon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.example.parkingreport.ui.reportManager.ReportPageActivity;
import com.example.parkingreport.utils.GPS;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import android.Manifest;

import java.util.Arrays;

/**
 * Fragment responsible for displaying the map interface.
 * Integrates GPS location, renders illegal parking zones as polygons,
 * handles tap-to-report logic, and updates map camera and markers accordingly.
 *
 * Authored by Larry Wang u7807744
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap gMap;
    private Marker currentMarker;
    private Button button;
    private UserViewModel viewModel;
    private ReportViewModel reportViewModel;
    private User user;
    public static final int REQUEST_LOCATION = 1;


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        attemptLocate(); // check GPS permission and attempt to locate user's location
        setUpIllegalZone();// Draw all illegal parking areas and instantiate them.
        setUpZoneClickable();//make illegal parking areas clickable.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel =  new ViewModelProvider(requireActivity())
                .get(UserViewModel.class);

        return inflater.inflate(R.layout.fragment_map, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //create map view.
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        button = view.findViewById(R.id.button);
        //Logic to allow taking photos only in illegal parking areas
        button.setOnClickListener(v -> {
            Context context = requireContext();
            boolean hasFineLocation = ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean hasCoarseLocation = ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if (!hasFineLocation || !hasCoarseLocation) {
                popToast("Please enable GPS permission in phone setting to report");
                return; // hint for open permission
            }

            GPS.getCurrentLocation(requireContext(), new GPS.GpsCallback() {
                @Override
                public void onLocationReady(double lat, double lng) {
                    LatLng userLocation = new LatLng(lat, lng);

                    boolean isInIllegalZone = false;
                    for (IllegalParkingZone zone : IllegalParkingZone.values()) {
                       //check if location is in illegal polygon
                        if (isPointInPolygon(userLocation, Arrays.asList(zone.getVertices()))) {
                            isInIllegalZone = true;
                            break;
                        }
                    }

                    if (isInIllegalZone) {
                        User user = viewModel.getUser();
                        Intent intent = new Intent(getActivity(), ReportPageActivity.class);
                        intent.putExtra("userId", user.getID());
                        intent.putExtra("userName", user.getName());
                        startActivity(intent);
                    } else {
                        popToast("Not an illegal parking area, unable to report");
                    }
                }
            });
        });

    }



    private void setUpIllegalZone() {
        // For each defined zone in enum, draw polygon on the map in red
        for (IllegalParkingZone zone : IllegalParkingZone.values()) {
            Polygon polygon = gMap.addPolygon(new PolygonOptions()
                    .add(zone.getVertices())
                    .strokeColor(Color.RED)
                    .fillColor(0x44FF0000)  // Translucent red.
                    .clickable(true)         // set clickable.
            );
            polygon.setTag(zone.getZoneName());  // Set polygon Tag.
        }
    }

    private void setUpZoneClickable() {
        //make each polygon zone clickable
        gMap.setOnPolygonClickListener(polygon -> {
            String zoneName = (String) polygon.getTag();  // Get polygon Tag
            if (zoneName != null) {
                popToast("NO PARKING HERE: " + zoneName);
            }
        });
    }



    @Override
    //check permission result
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                attemptLocate();//set up map if permitted
            } else {
                popToast("Location permission denied");
            }
        }
    }


    // Request GPS location or ask for permission if needed
    public void attemptLocate() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            GPS.getCurrentLocation(requireContext(), (lat, lng) -> {
                LatLng userLocation = new LatLng(lat, lng);
                gMap.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .title("You're here"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17));
            });
        } else {
            // Apply to Fragment without permission
            requestPermissions(
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_LOCATION
            );
        }
    }

    //Helper : pop a toast at the middle of the screen
    public void popToast(String string){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(string);
        Toast toast = new Toast(requireContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);  // Center the message
        toast.show();
    }

}
