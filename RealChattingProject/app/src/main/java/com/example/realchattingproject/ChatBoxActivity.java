package com.example.realchattingproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class ChatBoxActivity extends AppCompatActivity {
    public RecyclerView myRecylerView ;
    public List<Message> MessageList ;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messagetxt ;
    public Button send ;
    //declare socket object
    private Socket socket;
    private Realm realm;
    private Message message_Main;

    private String title;
    private String formattedDate;

    public String Nickname ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        messagetxt = (EditText) findViewById(R.id.message) ;
        send = (Button)findViewById(R.id.send);
        // get the nickame of the user
        Nickname= (String)getIntent().getExtras().getString(MainActivity.NICKNAME);
        //connect you socket client to the server

        // 내장 디비 연결 중..
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        RealmResults<Message> realmResults = realm.where(Message.class)
                .findAllAsync();

        for(Message message : realmResults) {
            MessageList.add(new Message(message.getNickname() ,message.getMessage())); // 이부분 부터 ㄱㄱ

            chatBoxAdapter = new ChatBoxAdapter(MessageList);
            myRecylerView.setAdapter(chatBoxAdapter);
        }

        try {
            socket = IO.socket("http://10.20.25.139:3000");
            socket.connect();
            socket.emit("join", Nickname);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        //setting up recyler
        MessageList = new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());



        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                if(!messagetxt.getText().toString().isEmpty()){ // 메시지를 보낼 때
                    socket.emit("messagedetection",Nickname,messagetxt.getText().toString());

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    formattedDate = df.format(c);
                    title = messagetxt.getText().toString();
                    Intent add = new Intent();
                    add.putExtra("title",title);
                    add.putExtra("time",formattedDate);
                    setResult(RESULT_OK,add);



                    messagetxt.setText(" ");
                }


            }
        });

        //implementing socket listeners
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(ChatBoxActivity.this,data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(ChatBoxActivity.this,data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event




                            //String nicknameRecord = data.getString("senderNickname");
                            //String messageRecord = data.getString("message");
                            //String time = data.getString("time");
                            //Toast.makeText(this,nicknameRecord + "," + time,Toast.LENGTH_SHORT).show(); 안되넹 ㅋ
//
//                            realm.beginTransaction();
//                            message_Main = realm.createObject(Message.class);
//                            message_Main.setNickname(nicknameRecord);
//                            message_Main.setNickname(messageRecord);
//
//                            realm.commitTransaction();
//
//                            RealmResults<Message> realmResults = realm.where(Message.class)
//                                    .equalTo("text",nicknameRecord)
//                                    .findAllAsync();

                            //MessageList.add(new Message(nicknameRecord, messageRecord));
                            //chatBoxAdapter = new ChatBoxAdapter(MessageList);
                            //myRecylerView.setAdapter(chatBoxAdapter);





                                //  여기부분 어떻게든 싸워 보자 ㅠㅠ





                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");

                            String time = data.getString("time");
                            //Toast.makeText(this,nicknameRecord + "," + time,Toast.LENGTH_SHORT).show(); 안되넹 ㅋ


                            realm.beginTransaction();
                            message_Main = realm.createObject(Message.class);
                            message_Main.setNickname(nickname);
                            message_Main.setNickname(message);

                            realm.commitTransaction();

                            RealmResults<Message> realmResults = realm.where(Message.class)
                                    .equalTo("text",nickname)
                                    .findAllAsync();


                            // make instance of message

                            Message m = new Message(nickname,message);


                            //add the message to the messageList

                            MessageList.add(m);

                            // add the new updated list to the dapter : 새로운 메시지를 수신할 때
                            chatBoxAdapter = new ChatBoxAdapter(MessageList);

                            // notify the adapter to update the recycler view

                            chatBoxAdapter.notifyDataSetChanged();

                            //set the adapter for the recycler view

                            myRecylerView.setAdapter(chatBoxAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.disconnect();
    }
}