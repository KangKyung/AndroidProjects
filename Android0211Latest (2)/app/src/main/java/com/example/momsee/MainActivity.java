package com.example.momsee;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.momsee.Retrofit.INodeJS;
import com.example.momsee.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {


    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    MaterialEditText edt_email, edt_password;
    MaterialButton btn_register, btn_login;
    SharedPreferences account;
    SharedPreferences.Editor aceditor;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        account = getSharedPreferences("Account", 0);
        aceditor = account.edit();
        try {
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.i("TAGGG", token);
            //init API
            Retrofit retrofit = RetrofitClient.getInstance();
            myAPI = retrofit.create(INodeJS.class);

            //AutoLogin();
            //View
            btn_login = findViewById(R.id.login_button);
            btn_register = findViewById(R.id.register_button);

            edt_email = findViewById(R.id.edt_email);
            edt_password = findViewById(R.id.edt_password);

            btn_login.setOnClickListener(v -> loginUser(edt_email.getText().toString(), edt_password.getText().toString()));
            btn_register.setOnClickListener(v -> registerUser(edt_email.getText().toString(), edt_password.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //파라미터 안에 final String macAdd
    public void AutoLogin(){
        if(!account.getString("email","").equals("")) {
            loginUser(account.getString("email",""), account.getString("password",""));
        }
    }

    private void registerUser(final String email, final String password) {
        final View enter_name_view = LayoutInflater.from(this).inflate(R.layout.enter_name_layout, null);

        new MaterialStyledDialog.Builder(this)
                .setTitle("Register")
                .setDescription("One more step!")
                .setCustomView(enter_name_view)
                .setIcon(R.drawable.ic_user)
                .setNegativeText("Cancel")
                .onNegative((dialog, which) -> dialog.dismiss())
                .setPositiveText("Register")
                .onPositive((dialog, which) -> {
                    MaterialEditText edt_name = enter_name_view.findViewById(R.id.edt_name);
                    compositeDisposable.add(myAPI.registerUser(email, edt_name.getText().toString(), password)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(str -> showToast(str)));
                }).show();

    }

    private void showToast(@NonNull final String msg) {
    }

    private void loginUser(String email, String password) {
        try {
//            compositeDisposable.add(myAPI.loginUser(email, password)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(s -> {
//                    if (s.contains("encrypted_password")) {
//                        //이메일 넘김
//                        Intent intent = new Intent(getApplicationContext(), SelectActivty.class);
//                        intent.putExtra("email", email);
//                        //여기에 유저 정보를 저장하는 객체 삽입
//                        startActivity(intent);
//                        aceditor.putString("email",email);
//                        aceditor.putString("password",password);
//                        aceditor.commit();
//                    }
//
//                            })
//
//            );
            Intent intent = new Intent(getApplicationContext(), SelectActivty.class);
            intent.putExtra("email", email);
            //여기에 유저 정보를 저장하는 객체 삽입
            startActivity(intent);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }


}
