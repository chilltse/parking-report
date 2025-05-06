package com.example.parkingreport.ui.user.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.UserViewModel;

public class HomeFragment extends Fragment {

    private UserViewModel viewModel;
    private User user;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button logout = view.findViewById(R.id.btn_logout);

        // User info related
        viewModel =  new ViewModelProvider(requireActivity())
                .get(UserViewModel.class);
        user = viewModel.getUser();
        Log.d("Fragment", " ####user id: " + user.getID());

        // User name
        TextView userNameTextView = view.findViewById(R.id.textView2);
        userNameTextView.setText(user.getName());

        // User email
        TextView userEmailTextView = view.findViewById(R.id.textView3);
        userEmailTextView.setText(user.getEmail());

        // Logout
        Button logoutButton = view.findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().finish();   // ① 结束当前 Activity
            }
        });

        logout.setOnClickListener(v -> {
            requireActivity().finish();   // ① 结束当前 Activity
        });

        return view;
    }
}

