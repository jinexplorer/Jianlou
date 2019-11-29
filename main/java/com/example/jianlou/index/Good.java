package com.example.jianlou.index;

public class Good {
    /**
     * 商品类，就是首页的商品的显示的类
     */
    private int photoID,headID;
    private String content,money,user_name;
    public Good(int photoImageID,int headImageID,String string_content,String string_money,String string_user_name){
        photoID=photoImageID;
        headID=headImageID;
        content=string_content;
        money=string_money;
        user_name=string_user_name;
    }
    public int getPhotoID(){
        return photoID;
    }
    public int getHeadID(){
        return headID;
    }

    public String getContent() {
        return content;
    }

    public String getMoney() {
        return money;
    }

    public String getUser_name() {
        return user_name;
    }
}
