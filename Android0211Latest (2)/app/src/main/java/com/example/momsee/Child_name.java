package com.example.momsee;

import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.momsee.FCMUse.PolicyManager;
import com.example.momsee.Retrofit.INodeJS;
import com.example.momsee.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class Child_name extends AppCompatActivity {
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Button btn_child_login;
    EditText edt_child_email, edt_child_name;

    private PolicyManager policyManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_name);

        btn_child_login = findViewById(R.id.btn_child_login);

        edt_child_email = findViewById(R.id.edt_child_email);
        edt_child_name = findViewById(R.id.edt_child_name);
        if(!Settings.canDrawOverlays(this.getApplicationContext())){

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);
        }
        AppOpsManager appOps = (AppOpsManager)getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), getPackageName());
        boolean granted = false;
        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        if (granted == false)
        {
            // 권한이 없을 경우 권한 요구 페이지 이동
            Intent intents = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intents);
        }


        policyManager = new PolicyManager(this);

        if (!policyManager.isAdminActive()) {
            Intent activateDeviceAdmin = new Intent(
                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            activateDeviceAdmin.putExtra(
                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    policyManager.getAdminComponent());
            activateDeviceAdmin
                    .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "After activating admin, you will be able to block application uninstallation.");
            startActivityForResult(activateDeviceAdmin,
                    PolicyManager.DPM_ACTIVATION_REQUEST_CODE);
        }

        //init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);
        btn_child_login.setOnClickListener(v -> loginUser_child(edt_child_email.getText().toString(), edt_child_name.getText().toString()));
    }

    private void loginUser_child(String email, String child_name) {
//        compositeDisposable.add(myAPI.loginUser_child(email, child_name)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(s -> {
//                    if (s.contains("child_name")) {
//                        Intent intent = new Intent(Child_name.this, Child_main.class);
//                        intent.putExtra("email",email);
//                        intent.putExtra("child_name",child_name);
//
//                        startActivity(intent);
//                    }
//                }));
        Intent intent = new Intent(Child_name.this, Child_main.class);
        intent.putExtra("email",email);
        intent.putExtra("child_name",child_name);
        startActivity(intent);

    }
}
