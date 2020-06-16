package com.example.androidlabs1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    EditText emailField;
    SharedPreferences sp;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3);

        emailField = (EditText)findViewById(R.id.edt1);
        sp = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = sp.getString("ReserveName", "Default value");

        emailField.setHint(savedString);

        loginBtn = (Button)findViewById(R.id.login);
        loginBtn.setOnClickListener( c -> {

            Intent profilePage = new Intent(MainActivity.this, ProfileActivity.class);
            //Give directions to go from this page, to SecondActivity
            EditText et = (EditText)findViewById(R.id.edt2);

            profilePage.putExtra("emailTyped", et.getText().toString());

            //Now make the transition:
            startActivityForResult( profilePage, 345);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        //get an editor object
        SharedPreferences.Editor editor = sp.edit();

        //save what was typed under the name "ReserveName"
        String whatWasTyped = emailField.getText().toString();
        editor.putString("ReserveName", whatWasTyped);

        //write it to disk:
        editor.commit();
    }
}

//    SharedPreferences prefs = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_lab3);
//
//        TextView txt = findViewById(R.id.top1);
//        TextView txt2 = findViewById(R.id.top2);
//        EditText editText = findViewById(R.id.edt1);
//        EditText editText2 = findViewById(R.id.edt2);
//
//
//        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
//
//
//        String savedString = prefs.getString("ReserveName", "Default Value");
//
//       editText.setText(savedString);
//        Button btn = findViewById(R.id.login);
//
//            btn.setOnClickListener(bt -> saveSharedPrefs(editText.getText().toString()));
//
//    }
//
//    private void saveSharedPrefs(String stringToSave) {
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("ReserveName", stringToSave);
//        editor.commit();
//    }
//
//}
        /*Button btn = findViewById(R.id.buttom1);
        CheckBox ch = findViewById(R.id.chb);
        ImageButton imageButton = findViewById(R.id.img);
        Switch swt = findViewById(R.id.swt);
        EditText editText = findViewById(R.id.edt);

        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        });
        //
        swt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Snackbar.make(imageButton, "The switch is now", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Undo", v -> swt.setChecked(false))
                    .show();

        });*/




