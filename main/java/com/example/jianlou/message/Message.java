package com.example.jianlou.message;

public class Message {
    /**
     * 消息界面
     */
    private int headID;
    private String message,user_name,username;

    public Message(int headImageID,String string_message,String string_user_name,String username){
        headID=headImageID;
        message=string_message;
        user_name=string_user_name;
        this.username=username;
    }

    public int getMessageHeadID() {
        return headID;
    }

    public String getMessageUser_name() {
        return user_name;
    }

    public String getMessageMessage() {
        return message;
    }

    public String geMessagetUsername() {
        return username;
    }
}