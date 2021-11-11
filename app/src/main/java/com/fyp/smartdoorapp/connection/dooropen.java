package com.fyp.smartdoorapp.connection;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.smartdoorapp.R;

import java.io.IOException;
import java.util.UUID;

public class dooropen extends AppCompatActivity {
    Button unlock_btn;
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

        Intent dev = getIntent();
        //receive the address of the bluetooth device
        address = dev.getStringExtra(DeviceList.EXTRA_ADDRESS);

        //Display content of smart door
        setContentView(R.layout.smartdoor_unlocked_pages);

        //Call the Button
        unlock_btn = findViewById(R.id.unlock_btn);

        //Call bluetooth method to connect
        new BTConnect().execute();

        //Add listener to button to send command to bluetooth
        unlock_btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                //Method to unlock the door
                unlocked();
            }
        });
    }

    private void unlocked()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("10".toString().getBytes());
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
            progress = ProgressDialog.show(dooropen.this, "Connecting Device...", "Please wait!!");
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
