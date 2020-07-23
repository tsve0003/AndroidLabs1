package com.example.androidlabs1;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    DatabaseHelper dbOpener;
    SQLiteDatabase db;
    private static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_IS_SEND = "IS_SEND";
    public static final String ITEM_ID = "ID";
    private ArrayList<MessageModel> list_message = new ArrayList<>();
    private AppCompatActivity parentActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        listView = (ListView) findViewById(R.id.ListView);
        listView.setAdapter(adt = new ChatAdapter());
        editText = (EditText) findViewById(R.id.ChatEditText);
        dbOpener = new DatabaseHelper(this);

        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

//        viewData();


        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);

        sendBtn = (Button) findViewById(R.id.SendBtn);
        receiveBtn = (Button) findViewById(R.id.ReceiveBtn);
        listView.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, list_message.get(position).message);
            dataToPass.putBoolean(ITEM_IS_SEND, list_message.get(position).isSend());
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, list_message.get(position).id);

            if (isTablet) {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            } else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });



        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getResources().getString(R.string.alertBuilderTitle))
                    .setMessage(getResources().getString(R.string.alertBuilderMsg1) + " " + position + "\n" + getResources().getString(R.string.alertBuilderMsg2) + " " + id)

                    .setPositiveButton(getResources().getString(R.string.yes), (click, arg) -> {
                        MessageModel selectedMessage = listMessage.get(position);
                        dbOpener.deleteMessage(selectedMessage);
                        //dbOpener.deleteMessage(position);
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

        sendBtn.setOnClickListener(c -> {
            String message = editText.getText().toString();
//              MessageModel model = new MessageModel(message, true);
//              listMessage.add(model);
            //            listView.setAdapter(adt);
//               editText.setText(null);
//                adt.notifyDataSetChanged();
            if (!message.equals("")){

                dbOpener.insertData(message, true);
                editText.setText("");
                listMessage.clear();
                viewData();
            }
        });

        receiveBtn.setOnClickListener(c -> {
            String message = editText.getText().toString();
            if (!message.equals("")) {
                dbOpener.insertData(message, false);
                editText.setText("");
                listMessage.clear();
                viewData();
            }
//            MessageModel model = new MessageModel(message, false);
//            listMessage.add(model);
//            listView.setAdapter(adt);
//            editText.setText(null);
//           adt.notifyDataSetChanged();
        });

        listMessage.clear();
        viewData();
       // listView.setAdapter(adt = new ChatAdapter());
    }

        private void viewData(){
            Cursor cursor = dbOpener.viewDataDb();

            if (cursor.getCount() != 0){
                while (cursor.moveToNext()){
                    MessageModel model = new MessageModel(cursor.getString(1), cursor.getInt(2)==0?true:false, cursor.getInt(0));
                    listMessage.add(model);
//                    ChatAdapter adt = new ChatAdapter();
                    listView.setAdapter(adt);
                    adt.notifyDataSetChanged();

                }
            }






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
            return getItem(position). getId();
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