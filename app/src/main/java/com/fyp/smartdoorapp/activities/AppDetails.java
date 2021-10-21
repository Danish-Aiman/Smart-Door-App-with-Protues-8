package com.fyp.smartdoorapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.fyp.smartdoorapp.R;
import com.fyp.smartdoorapp.adapters.ScanResultAdapter;

public class AppDetails extends AppCompatActivity {
    String packageName,scan_mode;
    ImageView appIcon;
    TextView pName;
    Button uninstall;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(this.getString(R.string.app_details));
        }

        if(scan_mode.equalsIgnoreCase("realtime_scan") || scan_mode.equalsIgnoreCase("normal_scan")) {
            uninstall.setVisibility(View.VISIBLE);
            uninstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivityForResult(intent, 1);
                }
            });
        }else{
            uninstall.setVisibility(View.INVISIBLE);
        }

        //packageName=intent.getExtras().getString("packageName");
        pName.setText(packageName);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(6);
        //prediction.setText(this.getString(R.string.prediction_score) + " " + df.format(intent.getExtras().getFloat("prediction")));
        PackageManager pm = AppDetails.this.getPackageManager();
        try {
            appIcon.setImageDrawable(pm.getPackageInfo(packageName, 0).applicationInfo.loadIcon(pm));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PackageManager pm = this.getPackageManager();
        if(requestCode==1 && !isPackageInstalled(packageName,pm)) {
            Toast.makeText(AppDetails.this,this.getString(R.string.uninstall_successful),Toast.LENGTH_LONG).show();
            startActivity(new Intent(AppDetails.this, MainActivity.class));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
//        startActivity(new Intent(this,MainActivity.class));
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //startActivity(new Intent(this,MainActivity.class));
    }
}
