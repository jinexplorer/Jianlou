package com.example.jianlou.message;

import org.litepal.crud.DataSupport;

public class ChatMessage extends DataSupport {
    private String sender;
    private String content;
    private int type;
    private String time;

    public int getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }


    public String getTime() {
        return time;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }
}
