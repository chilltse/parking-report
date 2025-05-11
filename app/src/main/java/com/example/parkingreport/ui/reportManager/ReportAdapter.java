package com.example.parkingreport.ui.reportManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.Report;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private final List<Report> reportList;
    private final Context context;
    private String loginAs;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public ReportAdapter(List<Report> reportList, Context context, String loginAs) {
        this.reportList = reportList;
        this.context = context;
        this.loginAs = loginAs;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView plate;
        ImageView picture;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textReportStatus);
            time  = itemView.findViewById(R.id.textReportDate);
            plate = itemView.findViewById(R.id.textReportId);
            picture = itemView.findViewById(R.id.imageThumbnail);
        }
    }

    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReportAdapter.ViewHolder holder, int position) {
        Report item = reportList.get(position);

        holder.plate.setText(item.getCarPlate());
        holder.title.setText(item.getStatus());
        holder.time.setText(fmt.format(item.getTimestamp()));
        Bitmap bitmap = BitmapFactory.decodeFile(item.getReportPicUrl());
        if (bitmap != null) {
            holder.picture.setImageBitmap(bitmap);
        } else {
            holder.picture.setImageResource(R.drawable.logo); // 显示默认图
            Toast.makeText(context, "图片加载失败，使用默认图", Toast.LENGTH_SHORT).show();
        }


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetailActivity.class);
            intent.putExtra("reportId",   item.getID());    // 正确传入 int id
            intent.putExtra("reporterName", item.getUserName()); // 新增传入 userName
            intent.putExtra("plate",      item.getCarPlate());
            intent.putExtra("picUrl", item.getReportPicUrl());
            intent.putExtra("status",     item.getStatus());
            intent.putExtra("time",       fmt.format(item.getTimestamp()));
            intent.putExtra("location",   item.getLocation());
            intent.putExtra("feedback",   item.getFeedback());
            intent.putExtra("reporterID", item.getUserId());
            intent.putExtra("loginAs", loginAs);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
