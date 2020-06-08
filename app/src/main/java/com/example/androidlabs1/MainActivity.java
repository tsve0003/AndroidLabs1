package com.example.androidlabs1;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);
        TextView txt = findViewById(R.id.top);
        Button btn = findViewById(R.id.buttom1);
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

        });

    }
}
