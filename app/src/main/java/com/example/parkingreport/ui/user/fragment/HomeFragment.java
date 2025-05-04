package com.example.parkingreport.ui.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.example.parkingreport.ui.login.MainActivity;

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

        // Change password
        Button changePwdBtn = view.findViewById(R.id.btn_change_password);
        changePwdBtn.setOnClickListener(v -> {
            EditText newPwd = view.findViewById(R.id.newPwd);
            String pwd = newPwd.getText().toString().trim();
            if(pwd.equals("")){
                newPwd.setError("Please input password here!");
            }else{
                viewModel.changeUserPassword(user.getID(),pwd);
                Toast.makeText(requireContext(), "You have changed your password!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

