package com.example.parkingreport.ui.user.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.parkingreport.R;
import com.example.parkingreport.ui.user.fragment.Myreport.ReportPageActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap gMap;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
            Intent intent = new Intent(getActivity(), ReportPageActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // set a marker pointed to Hanna Neumann by default, and zoom in.
        LatLng location = new LatLng(-35.27545560329495, 149.119274398116432);
        googleMap.addMarker(new MarkerOptions().position(location).title("Hanna Neumann").snippet("This is the correct GPS coordinate")
        );
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
    }
}