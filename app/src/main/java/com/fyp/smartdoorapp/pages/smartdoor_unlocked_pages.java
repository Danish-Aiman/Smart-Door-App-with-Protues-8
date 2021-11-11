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
import com.fyp.smartdoorapp.loginpage.PutData;

import java.io.IOException;
import java.util.UUID;

public class smartdoor_unlocked_pages extends AppCompatActivity {
    Button lock_btn;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean BtConnected = false;

    //SPP UUID To look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartdoor_unlocked_pages);

        address =smartdoor_locked_pages.address;

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            try{
                int name = Integer.parseInt(String.valueOf(R.string.smartdoor_text));
                actionBar.setTitle(name);
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        }

        new BTConnect().execute();

        lock_btn = findViewById(R.id.lock_btn);
        lock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String door = smartdoor_locked_pages.door;


                if (!door.equals(""))
                {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[1];
                            field[0] = "door";

                            //Creating array for data
                            String[] data = new String[1];
                            data[0] = door;

                            PutData putData = new PutData("http://192.168.50.173:8080/LoginSystem/verifypass.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Door Unlocked"))
                                    {
                                        locked();
                                        Toast.makeText(smartdoor_unlocked_pages.this, "Door Locked", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(smartdoor_unlocked_pages.this, smartdoor_locked_pages.class));
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
    }

    private void locked()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("0".toString().getBytes());
            }
            catch (IOException e)
            {
                message("Failed to unlock, Please Try Again");
            }
        }
    }

    //Fastest way to call Toast
    private void message(String text)
    {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    private class BTConnect extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            //Show a progress dialog
            progress = ProgressDialog.show(smartdoor_unlocked_pages.this, "Connecting Device...", "Please wait!!");
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
                //if the try failed, you can check the exception here
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        //After the doInBackground functiom, it checks if everything is fine
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                message("Connection Failed!!. Is it a SPP Bluetooth? Please Try again.");
                finish();
            }
            else
            {
                message("The Device is Connected.");
                BtConnected = true;
            }
            progress.dismiss();
        }
    }
}
