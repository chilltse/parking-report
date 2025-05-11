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
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private UserViewModel viewModel;

    private User user;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // User info related
        viewModel =  new ViewModelProvider(requireActivity())
                .get(UserViewModel.class);
        user = viewModel.getUser();
        Log.d("Fragment", " ####user: " + user);
//        Log.d("Fragment", " ####user id: " + user.getID());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view =  inflater.inflate(R.layout.fragment_my, container, false);

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


        return view;
    }
}