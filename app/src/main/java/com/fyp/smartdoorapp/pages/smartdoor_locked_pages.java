package com.fyp.smartdoorapp.pages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fyp.smartdoorapp.R;

public class smartdoor_locked_pages extends AppCompatActivity {
    Button unlock_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartdoor_locked_pages);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            try {
                int name = Integer.parseInt(String.valueOf(R.string.smartdoor_text));
                actionBar.setTitle(name);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        unlock_btn = findViewById(R.id.unlock_btn);
        unlock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(smartdoor_locked_pages.this);
                builder.setTitle("Door Passcode");

                final EditText psc_text = new EditText(smartdoor_locked_pages.this);
                psc_text.setInputType(InputType.TYPE_CLASS_PHONE);

                builder.setView(psc_text);

                builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(smartdoor_locked_pages.this, smartdoor_unlocked_pages.class));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }
}
