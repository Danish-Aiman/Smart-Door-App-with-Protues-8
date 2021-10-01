package com.fyp.antispywareapp.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.antispywareapp.activities.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import com.fyp.antispywareapp.R;

public class Login extends AppCompatActivity {

    TextInputEditText TextInputEditTextUsername, TextInputEditTextPassword;
    Button buttonLogin;
    TextView TextViewSignUp;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username, password;

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
                            PutData putData = new PutData("http://192.168.50.173:8080/LoginSystem/login.php", "POST", field, data);
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
}