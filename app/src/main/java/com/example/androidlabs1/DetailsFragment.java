package com.example.androidlabs1;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_details, container, false);

        //show the message
        TextView message = (TextView) result.findViewById(R.id.messageText);
        message.setText("Message = \"" + dataFromActivity.getString(ChatRoomActivity.ITEM_SELECTED) + "\"");

        //show the id:
        TextView idView = (TextView) result.findViewById(R.id.idText);
        idView.setText("ID = " + dataFromActivity.getLong(ChatRoomActivity.ITEM_ID));

        //show isSend:
        CheckBox chk = (CheckBox) result.findViewById(R.id.checkBox);
        if ((dataFromActivity.getBoolean(ChatRoomActivity.ITEM_IS_SEND))) {
            chk.setChecked(true);
            chk.setText(R.string.checkbox_send);
        } else {
            chk.setChecked(false);
            chk.setText(R.string.checkbox_receive);
        }
        chk.setChecked(dataFromActivity.getBoolean(ChatRoomActivity.ITEM_IS_SEND));

        // get the delete button, and add a click listener:
        Button finishButton = (Button) result.findViewById(R.id.buttonHide);
        finishButton.setOnClickListener(clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity) context;
    }
}
