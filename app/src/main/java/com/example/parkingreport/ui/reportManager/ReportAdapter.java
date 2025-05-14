package com.example.parkingreport.ui.reportManager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.Report;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter class for displaying a list of Reports in a RecyclerView.
 */
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private final List<Report> reportList;
    private final Context context;
    private String loginAs;

    // Date formatter for displaying report timestamps
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /**
     * Constructor to initialize adapter with report data and context.
     * @param reportList List of reports to display
     * @param context Application context
     * @param loginAs Current user role (e.g., Admin/User)
     */
    public ReportAdapter(List<Report> reportList, Context context, String loginAs) {
        this.reportList = reportList;
        this.context = context;
        this.loginAs = loginAs;
    }

    /**
     * ViewHolder class to hold UI components for each report item.
     */
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

    /**
     * Inflate the item layout and create a ViewHolder instance.
     */
    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Update the report data in the adapter.
     * This method clears old data and adds new ones.
     * For performance optimization, DiffUtil can be used instead.
     */
    public void updateData(List<Report> newReports) {
        reportList.clear();
        reportList.addAll(newReports);
        // Consider using DiffUtil here for better performance
        // notifyDataSetChanged();
    }

    /**
     * Bind the data to each item view in the RecyclerView.
     */
    @Override
    public void onBindViewHolder(ReportAdapter.ViewHolder holder, int position) {
        Report item = reportList.get(position);

        // Set car plate, report status, and timestamp
        holder.plate.setText(item.getCarPlate());
        holder.title.setText(item.getStatus());
        holder.time.setText(fmt.format(item.getTimestamp()));

        // Handle item click: open ReportDetailActivity with report details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetailActivity.class);
            intent.putExtra("reportId", item.getID());           // Report ID
            intent.putExtra("reporterName", item.getUserName()); // Reporter Name
            intent.putExtra("plate", item.getCarPlate());        // Car Plate
            intent.putExtra("picUrl", item.getReportPicUrl());   // Picture URL
            intent.putExtra("status", item.getStatus());         // Report Status
            intent.putExtra("time", fmt.format(item.getTimestamp())); // Timestamp
            intent.putExtra("location", item.getLocation());     // Location
            intent.putExtra("feedback", item.getFeedback());     // Feedback
            intent.putExtra("reporterID", item.getUserId());     // Reporter ID
            intent.putExtra("loginAs", loginAs);                 // Current User Role
            context.startActivity(intent);
        });
    }

    /**
     * Return the total number of report items.
     */
    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
