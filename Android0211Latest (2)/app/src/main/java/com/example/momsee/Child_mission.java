package com.example.momsee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.momsee.MathGame.MathGameActivity;
import com.example.momsee.Retrofit.INodeJS;
import com.example.momsee.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Child_mission extends Fragment {
    INodeJS myAPI;
    View rootView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView time;
    TextView content;
    public Child_mission(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Retrofit retrofit1 = RetrofitClient.getInstance();
        myAPI = retrofit1.create(INodeJS.class);
        rootView = inflater.inflate(R.layout.activity_child_mission, container, false);

        TextView remain = (TextView)rootView.findViewById(R.id.textRemain);
        SharedPreferences setting = getContext().getSharedPreferences("Set",0);
        remain.setText(String.valueOf(setting.getLong("Freetime",0)));


        Button MathGame = rootView.findViewById(R.id.MathGame);

        MathGame.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), MathGameActivity.class);
            startActivity(i);

        });

        Button Upload = rootView.findViewById(R.id.btnUpload);
        Upload.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent  = new Intent(getContext(),CameraActivity.class);
                intent.putExtra("email",getArguments().getString("email"));
                intent.putExtra("name",getArguments().getString("name"));
                startActivity(intent);
            }
        });


        try {
            mission_receive_mission_cont(getArguments().getString("email"), getArguments().getString("child_name"));
            mission_receive_mission_time(getArguments().getString("email"), getArguments().getString("child_name"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return rootView;
    }

    public void mission_receive_mission_cont(String email,String child_name){
        compositeDisposable.add(myAPI.mission_receive_mission_cont(email,child_name)//email이란 변수 생성후 받은 이메일을 변수에 저장하기
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    content = rootView.findViewById(R.id.txtContents);
                    content.setText(s);
                })
        );
    }
    public void mission_receive_mission_time(String email,String child_name){
        compositeDisposable.add(myAPI.mission_receive_mission_time(email,child_name)//email이란 변수 생성후 받은 이메일을 변수에 저장하기
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    time = rootView.findViewById(R.id.txtTime);
                    time.setText(s);
                })
        );
    }
    public static class mBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("received","received");
        }
    };
}
