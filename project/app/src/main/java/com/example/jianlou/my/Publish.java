package com.example.jianlou.my;
import android.net.Uri;

public class Publish {
    /**
     * 商品类，就是首页的商品的显示的类
     */
    private Uri photoID;
    private String content,money,goodID;
    public Publish(Uri photoImageID,String string_content,String string_money,String goodID){
        photoID=photoImageID;
        content=string_content;
        money=string_money;
        this.goodID=goodID;
    }
    public Uri getPhotoID(){
        return photoID;
    }

    public String getContent() {
        return content;
    }

    public String getMoney() {
        return money;
    }

    public String getGoodID() {
        return goodID;
    }
}
