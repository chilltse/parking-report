package com.example.parkingreport.ui.user.fragment.Myreport;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreport.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private List<ReportItem> reportList;
    private Context context;

    public ReportAdapter(List<ReportItem> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView id;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textReportStatus);
            time = itemView.findViewById(R.id.textReportDate);
            id = itemView.findViewById(R.id.textReportId);
        }
    }

    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReportAdapter.ViewHolder holder, int position) {
        ReportItem item = reportList.get(position);
        holder.id.setText(String.valueOf(item.getId()));
        holder.title.setText(item.getTitle());
        holder.time.setText(item.getTime());

        // 这里设置点击事件
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetailActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("time", item.getTime());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}

