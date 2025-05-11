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
import com.example.parkingreport.LLM.HuggingFaceLLMClient;
import com.example.parkingreport.LLM.LLMClient;
import com.example.parkingreport.LLM.OpenAILLMClient;
import com.example.parkingreport.R;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEditText = view.findViewById(R.id.inputEditText);
        recyclerView = view.findViewById(R.id.chatRecyclerView);
        Spinner sortSpinner = view.findViewById(R.id.sortSpinner);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);

        // 初始化 Spinner 数据
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"HuggingFace", "OpenAI"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        // 默认使用 HuggingFace
        llmClient = new HuggingFaceLLMClient();

        // 切换模型逻辑
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    llmClient = new HuggingFaceLLMClient();
                } else if (position == 1) {
                    llmClient = new OpenAILLMClient();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 默认不处理
            }
        });

        Button sendButton = view.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String message = inputEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                chatAdapter.addMessage(new ChatMessage("user", message));
                inputEditText.setText("");
                scrollToBottom();

                llmClient.askQuestion(message, reply -> requireActivity().runOnUiThread(() -> {
                    chatAdapter.addMessage(new ChatMessage("ai", reply));
                    scrollToBottom();
                }));
            }
        });
    }

    private void scrollToBottom() {
        recyclerView.post(() -> recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1));
    }
}
