package com.fyp.smartdoorapp.pages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.fyp.smartdoorapp.R;
import com.fyp.smartdoorapp.loginpage.Login;
import com.fyp.smartdoorapp.loginpage.PutData;
import com.fyp.smartdoorapp.loginpage.SignUp;
import com.fyp.smartdoorapp.services.RealTimeService;

public class setting_pages extends AppCompatActivity {
    Button passcode_btn, delete_btn, logout_btn;
    public static SwitchCompat realtime_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_pages);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            try{
                int name = Integer.parseInt(String.valueOf(R.string.setting_string));
                actionBar.setTitle(name);
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        }

        passcode_btn = findViewById(R.id.passcode_btn);
        passcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText psc_text, oldpsc;

                AlertDialog.Builder builder = new AlertDialog.Builder(setting_pages.this);
                builder.setTitle("Door Passcode");

                Context context = setting_pages.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                oldpsc = new EditText(setting_pages.this);
                oldpsc.setHint("Current Passcode");
                oldpsc.setInputType(InputType.TYPE_CLASS_PHONE);
                layout.addView(oldpsc);

                psc_text = new EditText(setting_pages.this);
                psc_text.setHint("New Passcode");
                psc_text.setInputType(InputType.TYPE_CLASS_PHONE);
                layout.addView(psc_text);

                builder.setView(layout);

                builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {

                        final String username = Login.username;
                        final String door = String.valueOf(oldpsc.getText());
                        final String newdoor = String.valueOf(psc_text.getText());

                        if (!door.equals(""))
                        {
                            Handler handler = new Handler();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String[] field = new String[3];
                                    field[0] = "username";
                                    field[1] = "door";
                                    field[2] = "newdoor";

                                    //Creating array for data
                                    String[] data = new String[3];
                                    data[0] = username;
                                    data[1] = door;
                                    data[2] = newdoor;

                                    PutData putData = new PutData("http://192.168.23.38:8080/LoginSystem/update.php", "POST", field, data);
                                    if (putData.startPut()) {
                                        if (putData.onComplete()) {
                                            String result = putData.getResult();
                                            if (result.equals("Passcode has been changed"))
                                            {
                                                Toast.makeText(setting_pages.this, result, Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(setting_pages.this, setting_pages.class));
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Try Again Later", Toast.LENGTH_SHORT).show();
                        }
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

        delete_btn = findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                String username = Login.username;

                if (!username.equals(""))
                {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[1];
                            field[0] = "username";

                            //Creating array for data
                            String[] data = new String[1];
                            data[0] = username;

                            PutData putData = new PutData("http://192.168.23.38:8080/LoginSystem/delete.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Successfully Delete Account"))
                                    {
                                        Toast.makeText(setting_pages.this, result, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(setting_pages.this, Login.class));
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logout_btn = findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(setting_pages.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(setting_pages.this, Login.class));
            }
        });


        realtime_switch = findViewById(R.id.realtime_switch);

        SharedPreferences sharedPreferences = getSharedPreferences("Realtime_Switch",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        realtime_switch.setChecked(sharedPreferences.getBoolean("realtime",true));

        realtime_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startService(new Intent(getApplicationContext(), RealTimeService.class));
                    }
                    editor.putBoolean("realtime",true);
                    editor.apply();
                }
                else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        stopService(new Intent(getApplicationContext(), RealTimeService.class));
                    }
                    editor.putBoolean("realtime",false);
                    editor.apply();
                }
                editor.commit();
            }
        });

    }
}
