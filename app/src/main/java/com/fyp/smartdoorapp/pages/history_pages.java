package com.fyp.smartdoorapp.pages;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fyp.smartdoorapp.R;
import com.fyp.smartdoorapp.adapters.RecycleAdapter;
import com.fyp.smartdoorapp.loginpage.Login;
import com.fyp.smartdoorapp.loginpage.PutData;

import java.util.ArrayList;
import java.util.List;

public class history_pages extends AppCompatActivity {
    //TextView text_item2;
    RecyclerView histopen, addhist;
    List<String> history, histlist;
    //ArrayList<String> history;
    SwipeRefreshLayout refresh;
    String result;
    LinearLayoutManager linearLayoutManager;

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
        //list = findViewById(R.id.list);
        //histlist = new ArrayList<>();
        histopen = findViewById(R.id.histopen);
        //addhist = findViewById(R.id.addhist);
        //text_item2 = findViewById(R.id.text_item2);

        final String door = smartdoor_locked_pages.door;
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

                    PutData putData = new PutData("http://192.168.43.242/LoginSystem/history.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            result = putData.getResult();
                            {
                                String dropen = ("Door Unlocked : \n" + result);
                                String drclosed = ("Door Locked : \n" + result);
                                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                histopen.setLayoutManager(linearLayoutManager);
                                RecycleAdapter recycleAdapter = new RecycleAdapter(history_pages.this, (ArrayList<String>)history );
                                histopen.setAdapter(recycleAdapter);
                                history.add(dropen);
                                history.add(drclosed);
                                //recycleAdapter.notifyItemChanged(0);
                                recycleAdapter.notifyDataSetChanged();
                                //adapter.notifyDataSetChanged();

                                refresh = findViewById(R.id.refresh);

                                refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                    @Override public void onRefresh() {
                                        //LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(history_pages.this);
                                        //addhist.setLayoutManager(linearLayoutManager1);
                                        //Recycle recycle = new Recycle(getApplicationContext(), (ArrayList<String>) history);
                                        //addhist.setAdapter(recycle);
                                        //histlist.add(list);
                                        history.add(dropen);
                                        history.add(drclosed);
                                        recycleAdapter.notifyItemInserted(history.size());
                                        //adapter.notifyDataSetChanged();
                                        //recycleAdapter.notifyDataSetChanged();
                                        refresh.setRefreshing(false);
                                    }
                                });
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
}