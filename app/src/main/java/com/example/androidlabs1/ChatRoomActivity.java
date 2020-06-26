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
    ArrayList<MessageModel> list = new ArrayList<>();
    BaseAdapter myAdapter;
    Button sendButton;
    Button receiveButton;
    ListView theList;

    EditText editText;
    List<MessageModel> listMessage = new ArrayList<>();
    Button sendBtn;
    Button receiveBtn;
    ListView listView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        listView = (ListView) findViewById(R.id.ListView);
        editText = (EditText) findViewById(R.id.ChatEditText);
        sendBtn = (Button) findViewById(R.id.SendBtn);
        receiveBtn = (Button) findViewById(R.id.ReceiveBtn);


        sendBtn.setOnClickListener(c -> {
            String message = editText.getText().toString();
            MessageModel model = new MessageModel(message, true);
            listMessage.add(model);
            ChatAdapter adt = new ChatAdapter(listMessage, getApplicationContext());
            listView.setAdapter(adt);
            adt.notifyDataSetChanged();
            editText.setText("");


        });

        receiveBtn.setOnClickListener(c -> {
            String message = editText.getText().toString();
            MessageModel model = new MessageModel(message, false);
            listMessage.add(model);
            editText.setText("");
            ChatAdapter adt = new ChatAdapter(listMessage, getApplicationContext());
            listView.setAdapter(adt);
            adt.notifyDataSetChanged();
            editText.setText("");
        });


        Log.d("ChatRoomActivity", "onCreate");
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


    class ChatAdapter extends BaseAdapter {

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
