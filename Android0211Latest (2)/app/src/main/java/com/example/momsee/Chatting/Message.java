package com.example.momsee.Chatting;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Message extends RealmObject {

    @Required
    private String nickname;
    @Required
    private String message ;

    public  Message(){ }

    public Message(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}