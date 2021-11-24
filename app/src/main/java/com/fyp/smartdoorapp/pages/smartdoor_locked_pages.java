package com.fyp.smartdoorapp.pages;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fyp.smartdoorapp.R;
import com.fyp.smartdoorapp.connection.DeviceList;
import com.fyp.smartdoorapp.loginpage.Login;
import com.fyp.smartdoorapp.loginpage.PutData;

import java.io.IOException;
import java.util.UUID;

public class smartdoor_locked_pages extends AppCompatActivity {
    Button unlock_btn;
    public static String door;
    public static String address = null;
    private ProgressDialog progress;
    public static BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean BtConnected = false;


    //SPP UUID To look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartdoor_locked_pages);

        Intent dev = getIntent();
        //receive the address of the bluetooth device
        address = dev.getStringExtra(DeviceList.EXTRA_ADDRESS);

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

        new BTConnect().execute();

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

                        door = String.valueOf(psc_text.getText());
                        String username = Login.username;

                        if (!door.equals(""))
                        {
                            Handler handler = new Handler();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String[] field = new String[2];
                                    field[0] = "username";
                                    field[1] = "door";

                                    //Creating array for data
                                    String[] data = new String[2];
                                    data[0] = username;
                                    data[1] = door;

                                    PutData putData = new PutData("http://192.168.50.173:8080/LoginSystem/verifypass.php", "POST", field, data);
                                    if (putData.startPut()) {
                                        if (putData.onComplete()) {
                                            String result = putData.getResult();
                                            if (result.equals("Door Unlocked"))
                                            {
                                                unlocked();
                                                Toast.makeText(smartdoor_locked_pages.this, result, Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(smartdoor_locked_pages.this, smartdoor_unlocked_pages.class));
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
    }

    private void unlocked()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("5".toString().getBytes());
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(),"Failed to unlock, Please Try Again", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class BTConnect extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            //Show a progress dialog
            progress = ProgressDialog.show(smartdoor_locked_pages.this, "Connecting Device...", "Please wait!!");
        }

        @Override
        //The connection is done in background while the progress dialog is shown
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (btSocket == null || !BtConnected)
                {
                    //Get the mobile bluetooth device
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();

                    //Connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);

                    //Create a RFCOMM (SPP) connection
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);

                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

                    //Start the connection
                    btSocket.connect();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        //After the doInBackground function, it checks if everything is fine
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                Toast.makeText(getApplicationContext(),"Connection Failed!!. Please Try again",Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"The Device is Connected", Toast.LENGTH_LONG).show();
                BtConnected = true;
            }
            progress.dismiss();
        }
    }

}
