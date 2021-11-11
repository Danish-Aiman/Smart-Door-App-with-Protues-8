package com.fyp.smartdoorapp.pages;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.smartdoorapp.R;
import com.fyp.smartdoorapp.adapters.RecycleAdapter;
import com.fyp.smartdoorapp.loginpage.PutData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class history_pages extends AppCompatActivity {
    //ListView histopen;;
    //ArrayList<String> history ;
    //ArrayAdapter<String> arrayAdapter;
    RecyclerView histopen;
    List<String>history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_screen);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            try{
                int name = Integer.parseInt(String.valueOf(R.string.history_string));
                actionBar.setTitle(name);
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        }

        history = new ArrayList<>();
        //histopen = findViewById(R.id.histopen);
        histopen = findViewById(R.id.histopen);

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

                    PutData putData = new PutData("http://192.168.50.173:8080/LoginSystem/history.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            {
                                String listed = ("Door Unlocked : \n" + result).toString();
                                history.add(listed);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                histopen.setLayoutManager(linearLayoutManager);
                                RecycleAdapter recycleAdapter = new RecycleAdapter(history_pages.this, (ArrayList<String>)history );
                                recycleAdapter.updatedata((ArrayList<String>) history);
                                histopen.setAdapter(recycleAdapter);
                                //boolean array = history.add(list);
                               // arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.textcolor_pages, Collections.singletonList(list));
                                //arrayAdapter.setNotifyOnChange(array);
                                //histopen.setAdapter(arrayAdapter);
                            }
                            /*
                            else
                            {
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                             */
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
}