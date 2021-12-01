package com.fyp.smartdoorapp.loginpage;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.smartdoorapp.activities.MainActivity;
import com.fyp.smartdoorapp.connection.DeviceList;
import com.fyp.smartdoorapp.pages.setting_pages;
import com.fyp.smartdoorapp.services.RealTimeService;
import com.google.android.material.textfield.TextInputEditText;

import com.fyp.smartdoorapp.R;

public class Login extends AppCompatActivity{

    TextInputEditText TextInputEditTextUsername, TextInputEditTextPassword;
    Button buttonLogin;
    TextView TextViewSignUp;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    public static String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        TextInputEditTextUsername = findViewById(R.id.username);
        TextInputEditTextPassword = findViewById(R.id.pass);

        buttonLogin = findViewById(R.id.loginbtn);

        TextViewSignUp = findViewById(R.id.signUpText);

        progressBar = findViewById(R.id.progress);

        TextViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isServiceRunning(RealTimeService.class)) {
                startService(new Intent(this, RealTimeService.class));
            }
        }


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final String username, password;

                username = String.valueOf(TextInputEditTextUsername.getText());
                password = String.valueOf(TextInputEditTextPassword.getText());

                if (!username.equals("") && !password.equals(""))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            PutData putData = new PutData("http://192.168.43.242/LoginSystem/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.equals("Login Success"))
                                    {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
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
                    Toast.makeText(getApplicationContext(), "All Fields Are Required!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isServiceRunning(Class serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
