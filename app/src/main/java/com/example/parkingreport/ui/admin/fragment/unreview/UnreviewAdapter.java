package com.example.parkingreport.ui.admin.fragment.unreview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreport.R;

import java.util.List;

public class UnreviewAdapter extends RecyclerView.Adapter<UnreviewAdapter.ItemViewHolder> {
    private List<ItemForUnre> itemList;
    private Context context;

    public UnreviewAdapter(List<ItemForUnre> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_unreview, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ItemForUnre currentItem = itemList.get(position);
        holder.title.setText(currentItem.getItem());

        // 点击事件：跳转到 DetailActivity
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, Unreview_list_deatilActivity.class);
            intent.putExtra("title", currentItem.getItem());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
