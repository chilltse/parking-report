package com.example.parkingreport.ui.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
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
import com.example.parkingreport.ui.user.fragment.Myreport.ReportPageActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap gMap;
    private Marker currentMarker;
    private Button button;

    private UserViewModel viewModel;

    private ReportViewModel reportViewModel;

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Data
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
        button.setOnClickListener(v -> {
            User user =  viewModel.getUser();
            Intent intent = new Intent(getActivity(), ReportPageActivity.class);
            intent.putExtra("userId", user.getID());
            startActivity(intent);
        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        setUpIllegalZone();// Draw all illegal parking areas and instantiate them.
        setUpZoneClickable();//make illegal parking areas clickable.
        setUpGPS(); //Center the map at the user's GPS location.
        setupMapListeners();//Wherever you click, the marker will follow and display the coordinate information

    }


    private void setUpIllegalZone() {
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
        gMap.setOnPolygonClickListener(polygon -> {
            String zoneName = (String) polygon.getTag();  // Get polygon Tag
            if (zoneName != null) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast, null);

                TextView text = layout.findViewById(R.id.toast_text);
                text.setText("NO PARKING HERE: " + zoneName);

                Toast toast = new Toast(requireContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.setGravity(Gravity.CENTER, 0, 0);  // Center the message
                toast.show();

            }
        });
    }


    private void setUpGPS() {
        /*
         *Check whether GPS location permission has been obtained.
         *Please manually set the location in the emulator because the default GPS location of the virtual machine is San Jose, USA.
         */
        Context context = getContext();
        if (context != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gMap.setMyLocationEnabled(true); // display a small blue dot as GPS location
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } // Ask the user for location permission if no permission.
        }

//        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        gMap.addMarker(new MarkerOptions()
                                .position(userLocation)
                                .title("You're here"));// Add a marker at the current position

                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17));//Move the camera to the current position
                    }
                });
    }

    private void setupMapListeners() {
        gMap.setOnMapClickListener(latLng -> {
            if (currentMarker != null) {
                currentMarker.remove();
            }

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title("Location: " + latLng.latitude + ", " + latLng.longitude);

            currentMarker = gMap.addMarker(markerOptions);

            if (currentMarker != null) {
                currentMarker.showInfoWindow();
            }
        });

    }
}