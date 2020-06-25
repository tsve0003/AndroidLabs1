package com.example.androidlabs1;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    ArrayList<MessageModel> list = new ArrayList<>();
    BaseAdapter myAdapter;
    Button sendButton;
    Button receiveButton;
    ListView theList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        theList = findViewById(R.id.aListView);
        theList.setAdapter(myAdapter = new MyListAdapter());
        myAdapter.notifyDataSetChanged();

        sendButton= findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                EditText textEdit = findViewById(R.id.textField);
                MessageModel message = new MessageModel(textEdit.getText().toString(), true);
                list.add(0, message);
                myAdapter.notifyDataSetChanged();
                textEdit.setText("");
            }
        });
        receiveButton = findViewById(R.id.receiveButton);
        receiveButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                EditText textEdit = findViewById(R.id.textField);
                MessageModel message = new MessageModel(textEdit.getText().toString(), false);
                list.add(0, message);
                myAdapter.notifyDataSetChanged();
                textEdit.setText("");
            }
        });
    }
    public void showAlertDialog(View v){
        theList.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ChatRoomActivity.this);
                alert.setTitle("Alert");
                alert.setMessage("Do you want to delete this?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(theList);
                        Toast.makeText(ChatRoomActivity.this, "The selected row is: \n The database id is: ",
                                Toast.LENGTH_SHORT).show();
                        myAdapter.notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create().show();
                return true;
            }
        });
    }
    private class MyListAdapter extends BaseAdapter {
        //ArrayList<String> list;
        @Override
        public int getCount() {
            //     list = new ArrayList<>();
            return list.size();
        }

        @Override
        public MessageModel getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MessageModel message = getItem(position);
            int layout;
            if (message.isSend){
                layout = R.layout.activity_main_send;
            }else{
                layout = R.layout.activity_main_receive;
            }
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(layout, parent, false);
                EditText textEdit = convertView.findViewById(R.id.message);
                textEdit.setText(message.message);
            }
            return convertView;
        }
        }

    }


