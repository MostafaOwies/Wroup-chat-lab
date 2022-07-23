package com.abemart.wroupchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abemart.wroup.common.WroupDevice;
import com.abemart.wroup.common.messages.MessageWrapper;

import java.util.List;


public class ChatAdapter extends ArrayAdapter<MessageWrapper> {

    private static class ChatAdapterHolder {
        TextView txtViewUsername;
        TextView txtViewMessage;
    }

    private static class ChatAdapterOwnerHolder {
        TextView txtViewMessage;
    }

    private final WroupDevice currentDevice;

    public ChatAdapter(Context context, List<MessageWrapper> messages, WroupDevice currentDevice) {
        super(context, 0, messages);
        this.currentDevice = currentDevice;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MessageWrapper message = getItem(position);

        try {
            ChatAdapterOwnerHolder chatAdapterOwnerHolder;
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_chat_owner, parent, false);
            chatAdapterOwnerHolder = new ChatAdapterOwnerHolder();
            chatAdapterOwnerHolder.txtViewMessage = (TextView) convertView.findViewById(R.id.text_view_message_owner);
            convertView.setTag(chatAdapterOwnerHolder);
            chatAdapterOwnerHolder.txtViewMessage.setText(message.getMessage());

        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
