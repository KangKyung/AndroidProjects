package com.example.momsee.Chatting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.momsee.R;
import com.example.momsee.Retrofit.INodeJS;
import com.example.momsee.Retrofit.RetrofitClient;
import retrofit2.Retrofit;

public class Child_chatting extends Fragment {
    private Button btn;
    public static final String NICKNAME = "usernickname";
    public static String childName = "sechankid";

    INodeJS myAPI;

    public Child_chatting(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
            View rootView = inflater.inflate(R.layout.activity_child_chatting, container, false);
        Retrofit retrofit1 = RetrofitClient.getInstance();
        myAPI = retrofit1.create(INodeJS.class);
        try {
            btn = rootView.findViewById(R.id.enterchat);


            String nickname = childName;

            btn.setOnClickListener(v -> {
                Intent i = new Intent(getActivity(), ChatBoxActivity.class);
                i.putExtra(NICKNAME, nickname);

                startActivity(i);
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    return rootView;
    }

}
