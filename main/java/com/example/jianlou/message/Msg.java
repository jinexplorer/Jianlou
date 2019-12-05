package com.example.jianlou.message;

public class Msg {
    public static final int TYPE_RECEIVED =0;
    public static final int TYPE_SEND =1;
    private String content;
    private int headID,type;

    public Msg(String content,int type,int headID){
        this.content=content;
        this.type=type;
        this.headID=headID;
    }

    public int getMsgHeadID() {
        return headID;
    }

    public int getMsgType() {
        return type;
    }

    public String getMsgContent() {
        return content;
    }
}
