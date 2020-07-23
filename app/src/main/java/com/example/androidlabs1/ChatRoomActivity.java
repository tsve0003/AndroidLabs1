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
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {


        private static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
        public static final String ITEM_SELECTED = "ITEM";
        public static final String ITEM_POSITION = "POSITION";
        public static final String ITEM_IS_SEND = "IS_SEND";
        public static final String ITEM_ID = "ID";
        private ArrayList<MessageModel> list_message = new ArrayList<>();
        private AppCompatActivity parentActivity;
        DatabaseHelper dbHelper;
        ListView theList;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat_room);
            MyListAdapter adpt = new MyListAdapter();
            theList = findViewById(R.id.ListView);
            boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded
            theList.setAdapter(adpt);
            dbHelper = new DatabaseHelper(this);
            viewMessages();


            theList.setOnItemClickListener((list, item, position, id) -> {
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


            theList.setOnItemLongClickListener((parent, view, pos, id) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.alertBuilderTitle));
                builder.setMessage(getString(R.string.alertBuilderMsg1) + (pos + 1) + getString(R.string.alertBuilderMsg2) + id);
                builder.setPositiveButton(getString(R.string.yes), (click, arg) -> {
                    Log.e(ACTIVITY_NAME, "In function: onStart");
                    dbHelper.deleteMessage(list_message.get(pos));
                    list_message.clear();
                    viewMessages();
                    if (list_message.isEmpty()) list_message.add(null);
                    adpt.notifyDataSetChanged();
                    if (isTablet) {
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentLocation);
                        if (fragment != null)
                            this.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }

                });
                builder.setNegativeButton(getString(R.string.no), (click, arg) -> {
                });
                builder.create().show();
                return true;
            });

            EditText textView = findViewById(R.id.ChatEditText);
            Button sendButton = findViewById(R.id.SendBtn);

            sendButton.setOnClickListener(click -> {
                String msg = textView.getText().toString();
                dbHelper.insertData(msg, true);
                list_message.clear();
                viewMessages();
                adpt.notifyDataSetChanged();
                textView.setText("");
            });
            Button receiveButton = findViewById(R.id.receiveButton);
            receiveButton.setOnClickListener(click -> {
                String msg = textView.getText().toString();
                dbHelper.insertData(msg, false);
                list_message.clear();
                viewMessages();
                adpt.notifyDataSetChanged();
                textView.setText("");
            });

        }

        private void viewMessages() {
            Cursor cursor = dbHelper.viewDataDb();
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    MessageModel msg = new MessageModel(cursor.getString(1), cursor.getInt(2) == 1 ? true : false, cursor.getLong(0));
                    list_message.add(msg);
                    theList.setAdapter(new MyListAdapter());
                }
            }
        }




        private class MyListAdapter extends BaseAdapter {

            public MyListAdapter() {
            }

            @Override
            public int getCount() {
                return list_message.size();
            }

            @Override
            public MessageModel getItem(int position) {
                return list_message.get(position);
            }

            @Override
            public long getItemId(int position) {
                return list_message.get(position).getId();
            }

            @Override
            public View getView(int position, View old, ViewGroup parent) {
                TextView messageText;
                if (getItem(position).isSend()) {
                    old = getLayoutInflater().inflate(R.layout.activity_main_send, parent, false);
                    messageText = old.findViewById(R.id.textViewMessage );

                } else {
                    old = getLayoutInflater().inflate(R.layout.activity_main_receive, parent, false);
                    messageText = old.findViewById(R.id.textViewMessage);
                }
                messageText.setText(getItem(position).message);
                return old;
            }
        }
    }