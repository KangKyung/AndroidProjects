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

public class Parent_chatting extends Fragment {             //프래그먼트 상속

    INodeJS myAPI;
    Button btn;

    public static String NICKNAME = "usernickname";

    public  Parent_chatting(){}

    /*이전 액티비티 onCreate()에 있던 내용을 그대로 가져다 씀.*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_parent_chatting, container, false);
        try{
            btn = (Button)rootView.findViewById(R.id.enterchat);
            //  여기에 사용자 이름을 출력 시키기 - 부모, 자녀

            //init API
            Retrofit retrofit1 = RetrofitClient.getInstance();
            myAPI = retrofit1.create(INodeJS.class);

            String nickname = "parent"; // 부모 이름 나오는 부분

            //채팅시작 버튼
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i  = new Intent(getActivity(),ChatBoxActivity.class);
                    i.putExtra(NICKNAME,nickname);
                    startActivity(i);
                }
            });


        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return rootView;
    }
}
