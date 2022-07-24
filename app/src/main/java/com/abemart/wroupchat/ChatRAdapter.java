package com.abemart.wroupchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abemart.wroup.common.messages.MessageWrapper;

import java.util.ArrayList;
import java.util.List;

public class ChatRAdapter extends RecyclerView.Adapter<ChatRAdapter.ChatHolder> {

    private ArrayList<MessageWrapper> messages ;


    ChatRAdapter(ArrayList<MessageWrapper> messages){
        this.messages=messages;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_owner, parent, false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        MessageWrapper message = messages.get(position);
        holder.message.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    void updateData(ArrayList<MessageWrapper> lst) {
        messages = lst;
        notifyDataSetChanged();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        TextView message;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.text_view_message_owner);
        }
    }
}
