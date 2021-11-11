package com.fyp.smartdoorapp.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.smartdoorapp.connection.DeviceList;
import com.fyp.smartdoorapp.loginpage.Login;
import com.fyp.smartdoorapp.pages.history_pages;
import com.fyp.smartdoorapp.pages.setting_pages;
import com.fyp.smartdoorapp.pages.smartdoor_locked_pages;
import com.fyp.smartdoorapp.services.RealTimeService;
import com.github.angads25.filepicker.view.FilePickerDialog;

import com.fyp.smartdoorapp.R;


public class MainActivity extends AppCompatActivity{

    Button scanButton, history_btn, doorlock_btn, setting_btn;
    private Context context = this;
    private boolean withSysApps = false;
    TextView textview_user;
    String username = Login.username + " Control Panel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        setting_btn = findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, setting_pages.class));
            }
        });

        doorlock_btn = findViewById(R.id.doorlock_btn);
        doorlock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DeviceList.class));
            }
        });

        history_btn = findViewById(R.id.history_btn);
        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , history_pages.class));
            }
        });

        scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ScanActivity.class).putExtra("withSysApps", withSysApps));
            }
        });

        textview_user = findViewById(R.id.textview_user);
        textview_user.setText(username);

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
