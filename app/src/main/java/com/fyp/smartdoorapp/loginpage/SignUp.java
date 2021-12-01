package com.fyp.smartdoorapp.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.smartdoorapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class SignUp extends AppCompatActivity {

  TextInputEditText TextInputEditTextUsername, TextInputEditTextPassword, TextInputEditTextPasscode;
  Button buttonSignUp;
  TextView TextViewLogin;
  ProgressBar progressBar;

  public static String username, password, door;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_screen);

    TextInputEditTextUsername = findViewById(R.id.username);
    TextInputEditTextPassword = findViewById(R.id.password);
    TextInputEditTextPasscode = findViewById(R.id.door_passcode);

    buttonSignUp = findViewById(R.id.buttonSignUp);

    TextViewLogin = findViewById(R.id.loginText);

    progressBar = findViewById(R.id.progress);

    TextViewLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
      }
    });

    buttonSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        username = String.valueOf(TextInputEditTextUsername.getText());
        password = String.valueOf(TextInputEditTextPassword.getText());
        door = String.valueOf(TextInputEditTextPasscode.getText());

        if (!username.equals("") && !password.equals(""))
        {
          progressBar.setVisibility(View.VISIBLE);
          Handler handler = new Handler();
          handler.post(new Runnable() {
            @Override
            public void run() {
              String[] field = new String[3];
              field[0] = "username";
              field[1] = "password";
              field[2] = "door";

              //Creating array for data
              String[] data = new String[3];
              data[0] = username;
              data[1] = password;
              data[2] = door;

              PutData putData = new PutData("http://192.168.43.242/LoginSystem/signup.php", "POST", field, data);
              if (putData.startPut()) {
                if (putData.onComplete()) {
                  progressBar.setVisibility(View.GONE);
                  String result = putData.getResult();
                  if (result.equals("Sign Up Success"))
                  {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
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