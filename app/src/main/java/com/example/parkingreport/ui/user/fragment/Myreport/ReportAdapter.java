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

    private final List<ReportItem> reportList;
    private final Context context;

    public ReportAdapter(List<ReportItem> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView plate;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textReportStatus);
            time  = itemView.findViewById(R.id.textReportDate);
            plate = itemView.findViewById(R.id.textReportId);
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
        ReportItem item = reportList.get(position);

        holder.plate.setText(item.getPlate());
        holder.title.setText(item.getStatus());
        holder.time.setText(item.getTime());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetailActivity.class);
            intent.putExtra("reportId",   item.getReportId());    // 正确传入 int id
            intent.putExtra("plate",      item.getPlate());
            intent.putExtra("status",     item.getStatus());
            intent.putExtra("time",       item.getTime());
            intent.putExtra("location",   item.getLocation());
            intent.putExtra("feedback",   item.getFeedback());
            intent.putExtra("reporterName", item.getReporterName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
