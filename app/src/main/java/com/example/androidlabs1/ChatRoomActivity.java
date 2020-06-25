package com.example.androidlabs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Intent fromProfile = getIntent();
        List<MessageModel> listMessage = new ArrayList<>();
        ListView listView = findViewById(R.id.theListView);
        Button sendBtn = findViewById(R.id.sendBtn);
        Button receiveBtn = findViewById(R.id.receiveBtn);
        EditText typeHere = findViewById(R.id.typeHere);

        sendBtn.setOnClickListener(c -> {
            String message = typeHere.getText().toString();
            MessageModel model = new MessageModel(message, true);
            listMessage.add(model);
            typeHere.setText("");
            ChatAdapter adt = new ChatAdapter(listMessage, getApplicationContext());
            listView.setAdapter(adt);
        });

        receiveBtn.setOnClickListener(c -> {
            String message = typeHere.getText().toString();
            MessageModel model = new MessageModel(message, false);
            listMessage.add(model);
            typeHere.setText("");
            ChatAdapter adt = new ChatAdapter(listMessage, getApplicationContext());
            listView.setAdapter(adt);
        });


        Log.d("ChatRoomActivity", "onCreate");

    }

    public class ChatAdapter extends BaseAdapter {

        private List<MessageModel> messageModels;
        private Context context;
        private LayoutInflater inflater;

        public ChatAdapter(List<MessageModel> messageModels, Context context) {
            this.messageModels = messageModels;
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return messageModels.size();
        }

        @Override
        public Object getItem(int position) {
            return messageModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                if (messageModels.get(position).isSend()) {
                    view = inflater.inflate(R.layout.activity_main_send, null);

                } else {
                    view = inflater.inflate(R.layout.activity_main_receive, null);
                }
                TextView messageText = (TextView) view.findViewById(R.id.textViewMessage);
                messageText.setText(messageModels.get(position).message);
            }
            return view;
        }


    }
}
