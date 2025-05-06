package com.example.parkingreport.ui.user.fragment;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
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

        // User icon
        ImageView imageView = view.findViewById(R.id.imageView2);
        String path = user.getProfilePicUrl();  // e.g., "android.resource://com.example.parkingreport/drawable/panda"
        Uri uri = Uri.parse(path);
        String resourceName = uri.getLastPathSegment();  // 提取 "panda"

        int resId = getResources().getIdentifier(resourceName, "drawable", requireContext().getPackageName());
        if (resId != 0) {
            Glide.with(requireContext())
                    .load(resId)
                    .override(120, 120)      // 强制加载尺寸
                    .centerCrop()            // 居中裁剪显示
                    //.circleCrop()         // 如果你想显示圆形头像，取消这行注释
                    .into(imageView);
        } else {
            Log.e("icon", "资源未找到: " + resourceName);
        }

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

