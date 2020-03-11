package com.example.momsee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SelectActivty extends AppCompatActivity implements View.OnClickListener {

    Button Bt_Parent,Bt_Child;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Bt_Parent = findViewById(R.id.bt_parent);
        Bt_Child = findViewById(R.id.bt_child);
        Bt_Parent.setOnClickListener(this);
        Bt_Child.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_parent:{
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE
                )!= PackageManager.PERMISSION_GRANTED)
                    startActivity(new Intent(this,P_permissionActivity.class));
                else {
                    //자식목록을 여기서 긁어와야됨
                    String email = getIntent().getStringExtra("email");
                    Intent intent = new Intent(SelectActivty.this, Parent_main.class);
                    intent.putExtra("email",email);
                   startActivity(intent);
                }
                break;
            }
            case R.id.bt_child:{
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE
                )!= PackageManager.PERMISSION_GRANTED)
                    startActivity(new Intent(this,C_permissionActivity.class));
                else
                    startActivity(new Intent(this, Child_name.class));
                break;
            }
        }

    }
}
