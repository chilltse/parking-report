package com.example.parkingreport.ui.admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreport.R;
import com.example.parkingreport.ui.admin.fragment.review.Item;
import com.example.parkingreport.ui.admin.fragment.review.ItemAdapter;
import com.example.parkingreport.ui.admin.fragment.unreview.ItemForUnre;
import com.example.parkingreport.ui.admin.fragment.unreview.UnreviewAdapter;

import java.util.ArrayList;
import java.util.List;

public class unreFragment extends Fragment {

    private RecyclerView recyclerView;


    private UnreviewAdapter adapter;

    public unreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unreview_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化 RecyclerView
        recyclerView = view.findViewById(R.id.recycle2);

        List<ItemForUnre> itemList = new ArrayList<>();
        itemList.add(new ItemForUnre("Test 4"));
        itemList.add(new ItemForUnre("Test 5"));
        itemList.add(new ItemForUnre("Test 6"));

        adapter = new UnreviewAdapter(itemList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
