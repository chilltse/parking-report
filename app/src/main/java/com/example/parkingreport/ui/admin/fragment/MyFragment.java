package com.example.parkingreport.ui.admin.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
/**
 * @author Yudong Qiu u7937030
 * - MyFragment displays the admin user's profile information.
 * - Shows current user's name and email retrieved from UserViewModel.
 * - Provides a logout button to close the current activity.
 * - Uses shared ViewModel to access user data across fragments.
 * - Designed as a simple profile fragment for admin interface.
 */
public class MyFragment extends Fragment {
    private User user;

    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModel and retrieve user information
        UserViewModel viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        user = viewModel.getUser();
        Log.d("MyFragment", "User: " + user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        // Display user name
        TextView userNameTextView = view.findViewById(R.id.textView2);
        userNameTextView.setText(user.getName());

        // Display user email
        TextView userEmailTextView = view.findViewById(R.id.textView3);
        userEmailTextView.setText(user.getEmail());

        // Logout button click event
        Button logoutButton = view.findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(view1 -> {
            requireActivity().finish(); // Close current Activity
        });

        return view;
    }
}
