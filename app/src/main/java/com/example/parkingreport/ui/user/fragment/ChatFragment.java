package com.example.parkingreport.ui.user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreport.LLM.ChatAdapter;
import com.example.parkingreport.LLM.ChatMessage;
import com.example.parkingreport.LLM.LLMClient;
import com.example.parkingreport.LLM.LLMClientFactory;
import com.example.parkingreport.R;
/**
 * The AI bot fragment
 * @author Yudong Qiu
 */
public class ChatFragment extends Fragment {

    private ChatAdapter chatAdapter;
    private EditText inputEditText;
    private RecyclerView recyclerView;
    private LLMClient llmClient;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        inputEditText = view.findViewById(R.id.inputEditText);
        recyclerView = view.findViewById(R.id.chatRecyclerView);
        Spinner modelSpinner = view.findViewById(R.id.sortSpinner);
        Button sendButton = view.findViewById(R.id.sendButton);

        // Setup RecyclerView with LinearLayoutManager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);

        // Initialize Spinner options for model selection
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Gemini", "OpenAI"}
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(spinnerAdapter);

        // Default LLM model: Gemini
        llmClient = LLMClientFactory.createLLMClient("gemini");

        // Handle model selection changes
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Switch between Gemini and OpenAI models
                String selectedModel = position == 0 ? "gemini" : "openai";
                llmClient = LLMClientFactory.createLLMClient(selectedModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
            }
        });

        // Handle send button click event
        sendButton.setOnClickListener(v -> {
            String message = inputEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                // Add user message to chat view
                chatAdapter.addMessage(new ChatMessage("user", message));
                inputEditText.setText("");
                scrollToBottom();

                // Send message to LLM and add AI's reply to chat view
                llmClient.askQuestion(message, reply -> requireActivity().runOnUiThread(() -> {
                    chatAdapter.addMessage(new ChatMessage("ai", reply));
                    scrollToBottom();
                }));
            }
        });
    }

    /**
     * Scroll RecyclerView to the latest message (bottom).
     */
    private void scrollToBottom() {
        recyclerView.post(() -> recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1));
    }
}
