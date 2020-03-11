package com.example.momsee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class P_permissionActivity extends AppCompatActivity implements View.OnClickListener {

    Button ParPerOK;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_permission);
        ParPerOK = (Button)findViewById(R.id.bt_par_ok);
        ParPerOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bt_par_ok){
            CheckPermission();
        }

    }

    private void CheckPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.CONTROL_LOCATION_UPDATES,
                Manifest.permission.SYSTEM_ALERT_WINDOW
        },100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {startActivity(new Intent(this,Parent_main.class));}
            else
                Toast.makeText(this, "권한 거부", Toast.LENGTH_SHORT).show();
        }
    }
}
