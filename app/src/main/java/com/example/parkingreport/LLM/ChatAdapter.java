package com.example.parkingreport.LLM;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreport.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter for displaying chat messages between the user and AI.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    // A list to hold all chat messages
    private final List<ChatMessage> messages = new ArrayList<>();

    /**
     * Adds a new chat message to the list and notifies the adapter to update.
     *
     * @param msg The new ChatMessage to be added
     */
    public void addMessage(ChatMessage msg) {
        messages.add(msg);
        notifyItemInserted(messages.size() - 1); // Notify that a new item was inserted at the end
    }

    /**
     * ViewHolder class that holds reference to the message TextView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.messageText); // Bind to messageText in XML
        }
    }

    /**
     * Returns the type of view for a given position.
     * 0 for user messages, 1 for AI messages.
     */
    @Override
    public int getItemViewType(int position) {
        return messages.get(position).sender.equals("user") ? 0 : 1;
    }

    /**
     * Inflates the appropriate layout (user or AI) and creates a ViewHolder.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = (viewType == 0) ? R.layout.item_message_user : R.layout.item_message_ai;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Binds the data (message text) to the ViewHolder.
     */
    @Override
    public void onBindViewHolder(ViewHolder h, int i) {
        h.textView.setText(messages.get(i).message);
    }

    /**
     * Returns the total number of messages in the list.
     */
    @Override
    public int getItemCount() {
        return messages.size();
    }
}