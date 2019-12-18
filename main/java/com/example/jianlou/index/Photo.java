package com.example.jianlou.index;
import android.net.Uri;

public class Photo {
    /**
     * 商品类，就是首页的商品的显示的类
     */
    private Uri photoID;
    public Photo(Uri photoImageID){
        photoID=photoImageID;
    }
    public Uri getPhotoPhotoID(){
        return photoID;
    }
}

