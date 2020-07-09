package com.example.androidlabs1;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<MessageModel> listMessage = new ArrayList<>();
    private Button sendBtn;
    private Button receiveBtn;
    private ListView listView;
    private EditText editText;
    private ChatAdapter adt;
    DatabaseHelper db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        listView = (ListView) findViewById(R.id.ListView);
        listView.setAdapter(adt = new ChatAdapter());
        editText = (EditText) findViewById(R.id.ChatEditText);
        db = new DatabaseHelper(this);


        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);

        sendBtn = (Button) findViewById(R.id.SendBtn);
        receiveBtn = (Button) findViewById(R.id.ReceiveBtn);

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getResources().getString(R.string.alertBuilderTitle))
                    .setMessage(getResources().getString(R.string.alertBuilderMsg1) + " " + position + "\n" + getResources().getString(R.string.alertBuilderMsg2) + " " + id)

                    .setPositiveButton(getResources().getString(R.string.yes), (click, arg) -> {
                        listMessage.remove(position);
                        adt.notifyDataSetChanged();
                    })
                    .setNegativeButton(getResources().getString(R.string.no), (click, arg) -> {
                    });
            if (listMessage.get(position).isSend) {
                alert.setView(getLayoutInflater().inflate(R.layout.activity_main_send, null));
            } else {
                alert.setView(getLayoutInflater().inflate(R.layout.activity_main_receive, null));
            }


            alert.create().show();

            return true;
        });
    }

        private void viewData(){
            Cursor cursor = db.viewData();

            if (cursor.getCount() != 0){
                while (cursor.moveToNext()){
                    MessageModel model = new MessageModel(cursor.getString(1), cursor.getInt(2)==0?true:false);
                    listMessage.add(model);
                    ChatAdapter adt = new ChatAdapter();
                    listView.setAdapter(adt);

                }
            }

        sendBtn.setOnClickListener(c -> {
            String message = editText.getText().toString();
            MessageModel model = new MessageModel(message, true);
            listMessage.add(model);
            listView.setAdapter(adt);
            editText.setText(null);
            adt.notifyDataSetChanged();
        });

        receiveBtn.setOnClickListener(c -> {
            String message = editText.getText().toString();
            MessageModel model = new MessageModel(message, false);
            listMessage.add(model);
            listView.setAdapter(adt);
            editText.setText(null);
            adt.notifyDataSetChanged();
        });

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
//            View newView = convertView;
            LayoutInflater inflater = getLayoutInflater();

            if(convertView == null) {
                if (message.isSend) {
                    convertView = inflater.inflate(R.layout.activity_main_send, parent, false);
                }
                else {
                    convertView = inflater.inflate(R.layout.activity_main_receive, parent, false);
                }
            }
            TextView  messageText = (TextView)convertView.findViewById(R.id.textViewMessage);
            messageText.setText(listMessage.get(position).message);


            return convertView;
        }

    }
}