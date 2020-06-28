package com.example.androidlabs1;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private  ArrayList<MessageModel> listMessage = new ArrayList<>();
    private  Button sendBtn;
    private  Button receiveBtn;
    private  ListView listView;
    private  EditText editText;
    private ChatAdapter adt;
    private  SwipeRefreshListener refresher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        SwipeRefreshListener refresher = findViewById( R.id.refresh );

        listView = (ListView) findViewById(R.id.ListView);
        listView.setAdapter(adt = new ChatAdapter());
        editText = (EditText) findViewById(R.id.ChatEditText);


        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);

        sendBtn = (Button) findViewById(R.id.SendBtn);
        receiveBtn = (Button) findViewById(R.id.ReceiveBtn);

        listView.setOnItemLongClickListener((parent, view, position, id)-> {
            AlertDialog.Builder  alert = new AlertDialog.Builder(this);
            alert.setTitle("Do you want to delete this?" )
            .setMessage("The selected row is: " + position + "\n" + "The database id : " + id)
            //strmsg += "The selected row is:" + position + "\n";
            //strmsg += "The selected message is:" + listMessage.get(position).message + "\n";

           //alert.setMessage("The selected row is:" + position);
           //alert.setMessage("The selected message is:" + listMessage.get(position).message);
           //alert.setMessage("The database id :");
           .setPositiveButton("Yes",(click, arg)->{
              listMessage.remove(position);
              adt.notifyDataSetChanged();
           })
            .setNegativeButton("No", (click, arg) -> {
            });
            if(listMessage.get(position).isSend) {
                alert.setView(getLayoutInflater().inflate(R.layout.activity_main_send, null) );
            } else {
                alert.setView(getLayoutInflater().inflate(R.layout.activity_main_receive, null) );
            }

            alert.create().show();

        return true;
    });

        sendBtn.setOnClickListener(c -> {
            String message = editText.getText().toString();
            MessageModel model = new MessageModel(message, true);
            listMessage.add(model);
            listView.setAdapter(adt);
            adt.notifyDataSetChanged();
            editText.setText("");
        });

        receiveBtn.setOnClickListener(c -> {
            String message = editText.getText().toString();
            MessageModel model = new MessageModel(message, false);
            listMessage.add(model);
            listView.setAdapter(adt);
            adt.notifyDataSetChanged();
            editText.setText("");
        });


        Log.d("ChatRoomActivity", "onCreate");


    }


    class ChatAdapter extends BaseAdapter {



        @Override
        public int getCount() {
            return listMessage.size();
        }

        @Override
        public MessageModel getItem(int position) {
            return listMessage.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           MessageModel message = (MessageModel) getItem(position);
            View newView = convertView;
            LayoutInflater inflater = getLayoutInflater();

            if(newView == null) {
                if (message.isSend) {
                    newView = inflater.inflate(R.layout.activity_main_send, parent, false);
                }
                else {
                    newView = inflater.inflate(R.layout.activity_main_receive, parent, false);
                }
            }
            TextView  messageText = (TextView)newView.findViewById(R.id.textViewMessage);
            messageText.setText(listMessage.get(position).message);


            return newView;
        }

    }
    }


