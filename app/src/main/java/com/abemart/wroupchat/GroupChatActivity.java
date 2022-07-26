package com.abemart.wroupchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abemart.wroup.client.WroupClient;
import com.abemart.wroup.common.WiFiP2PInstance;
import com.abemart.wroup.common.WroupDevice;
import com.abemart.wroup.common.listeners.ClientConnectedListener;
import com.abemart.wroup.common.listeners.ClientDisconnectedListener;
import com.abemart.wroup.common.listeners.DataReceivedListener;
import com.abemart.wroup.common.messages.MessageWrapper;
import com.abemart.wroup.service.WroupService;

import java.util.ArrayList;


public class GroupChatActivity extends AppCompatActivity implements DataReceivedListener, ClientConnectedListener, ClientDisconnectedListener {

    public static final String EXTRA_GROUP_NAME = "groupNameExtra";
    public static final String EXTRA_IS_GROUP_OWNER = "isGroupOwnerExtra";

    private ListView listViewChat;
    //private ChatAdapter chatAdapter;


    private ChatRAdapter adapter;
    private ArrayList<MessageWrapper> messageList;

    private String groupName;
    private boolean isGroupOwner = false;

    private WroupService wroupService;
    private WroupClient wroupClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        messageList=new ArrayList<MessageWrapper>();

       // chatAdapter = new ChatAdapter(getApplicationContext(), new ArrayList<MessageWrapper>(), WiFiP2PInstance.getInstance(getApplicationContext()).getThisDevice());

        Intent startIntent = getIntent();
        groupName = startIntent.getStringExtra(EXTRA_GROUP_NAME);
        isGroupOwner = startIntent.getBooleanExtra(EXTRA_IS_GROUP_OWNER, false);

        if (isGroupOwner) {
            wroupService = WroupService.getInstance(getApplicationContext());
            wroupService.setDataReceivedListener(this);
            wroupService.setClientDisconnectedListener(this);
            wroupService.setClientConnectedListener(this);
        } else {
            wroupClient = WroupClient.getInstance(getApplicationContext());
            wroupClient.setDataReceivedListener(this);
            wroupClient.setClientDisconnectedListener(this);
            wroupClient.setClientConnectedListener(this);
        }

      //  listViewChat = (ListView) findViewById(R.id.list_view_group_chat);
        Button btnSend = (Button) findViewById(R.id.button_send_message);
        final EditText editTextMessage = (EditText) findViewById(R.id.edit_text_chat_message);

        //listViewChat.setAdapter(chatAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageStr = editTextMessage.getText().toString();
                if (messageStr != null && !messageStr.isEmpty()) {
                    MessageWrapper normalMessage = new MessageWrapper();
                    normalMessage.setMessage(editTextMessage.getText().toString());
                    normalMessage.setMessageType(MessageWrapper.MessageType.NORMAL);

                    if (isGroupOwner) {
                        wroupService.sendMessageToAllClients(normalMessage);
                    } else {
                        wroupClient.sendMessageToAllClients(normalMessage);
                    }

                    //chatAdapter.add(normalMessage);
                    messageList.add(normalMessage);
                    adapter.updateData(messageList);
                    editTextMessage.setText("");
                    Log.d("TAG", normalMessage.toString());
                }
            }
        });

        setActionBarTitle(groupName);
        setTitle(groupName);
        setupRecyclerView();
    }

    private void setActionBarTitle(String title) {
        if (getActionBar() != null) {
            getActionBar().setTitle(title);
        }
    }

    @Override
    public void onDataReceived(final MessageWrapper messageWrapper) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // chatAdapter.add(messageWrapper);
                messageList.add(messageWrapper);
                adapter.updateData(messageList);
            }
        });
    }

    @Override
    public void onClientConnected(final WroupDevice wroupDevice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.device_connected, wroupDevice.getDeviceName()), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClientDisconnected(final WroupDevice wroupDevice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.device_disconnected, wroupDevice.getDeviceName()), Toast.LENGTH_LONG).show();
                wroupService.disconnect();
                wroupClient.disconnect();
            }
        });
    }

    private void setupRecyclerView(){
        RecyclerView messagesRV=findViewById(R.id.list_view_group_chat);
        messagesRV.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        adapter=new ChatRAdapter(new ArrayList<MessageWrapper>());
        messagesRV.setAdapter(adapter);

    }

    protected void onDestroy() {
        super.onDestroy();

        if (wroupService != null) {
            wroupService.disconnect();
        }

        if (wroupClient != null) {
            wroupClient.disconnect();
        }
    }
}
