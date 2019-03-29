package com.example.realchattingproject;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Message extends RealmObject {

    private String nickname;
    private String message ;

    @Override
    public String toString() {
        return "Memo{" +
                "text='" + text + '\'' +
                '}';
    }

    @Required
    private String text;

    public  Message(){ this.text = "아무값도 없습니다."; }

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

