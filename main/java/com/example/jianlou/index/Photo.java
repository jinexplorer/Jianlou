package com.example.jianlou.index;

import android.graphics.Bitmap;

public class Photo {
    /**
     * 商品类，就是首页的商品的显示的类
     */
    private Bitmap photoID;
    public Photo(Bitmap photoImageID){
        photoID=photoImageID;
    }
    public Bitmap getPhotoPhotoID(){
        return photoID;
    }
}

