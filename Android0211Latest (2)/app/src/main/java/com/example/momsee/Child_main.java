package com.example.momsee;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.momsee.Chatting.Child_chatting;

public class Child_main extends AppCompatActivity {
    ViewPager vp;
    String email;
    String child_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_child_main);

            /*레이아웃 뷰 변수 할당*/
            vp = findViewById(R.id.vp);
            Button btn_first = findViewById(R.id.btn_mission);
            Button btn_second = findViewById(R.id.btn_chat);

            /*뷰페이저 어댑터할당*/
            vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
            vp.setCurrentItem(0);

            /*각 버튼에 태그 설정(구분 용도)*/
            btn_first.setOnClickListener(movePageListener);
            btn_first.setTag(0);
            btn_second.setOnClickListener(movePageListener);
            btn_second.setTag(1);

            email = getIntent().getStringExtra("email");
            child_name = getIntent().getStringExtra("child_name");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    View.OnClickListener movePageListener = new View.OnClickListener()             //각 버튼에 대한 클릭 리스너
    {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();                 //버튼을 태그로 구분하여 현 뷰페이저의 화면을 전환한다.
            vp.setCurrentItem(tag);
        }
    };

    public class pagerAdapter extends FragmentStatePagerAdapter               //뷰페이저에 들어갈 아이템을 제공하는 어댑터.
    {
        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            try {
                Bundle bundle = new Bundle();        //번들처리
                bundle.putString("email", email);    //이메일 안에 번들추가
                bundle.putString("child_name",child_name);
                switch (position) {
                    case 0:
                        Fragment info = new Child_mission();
                        info.setArguments(bundle);
                        return info;          //자식정보 프래그먼트
                    case 1:
                        Fragment chatting = new Child_chatting();
                        chatting.setArguments(bundle);
                        return chatting;              //채팅 프래그먼트.
                    default:
                        return null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }
}




