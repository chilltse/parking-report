package com.example.parkingreport.ui.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.example.parkingreport.ui.login.MainActivity;

public class HomeFragment extends Fragment {

    private UserViewModel viewModel;

    int userID;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button logout = view.findViewById(R.id.btn_logout);

        // User info related
        viewModel =  new ViewModelProvider(this)
                .get(UserViewModel.class);
        userID = viewModel.getUserId();
        Log.d("Fragment", " ####user id: " + userID);

        logout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });

        return view;
    }
}

