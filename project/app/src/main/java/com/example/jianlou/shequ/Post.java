package com.example.jianlou.shequ;
/**
 * 社区中发布的帖子的类
 */
public class Post {
    private int photoID,headID;
    private String user_name,love,talk,content,origin;
    public Post(int photoImageID,int headImageID,String string_user_name,String string_love,String string_talk,String string_content, String string_origin){
        photoID=photoImageID;
        headID=headImageID;
        love=string_love;
        talk=string_talk;
        origin=string_origin;
        content=string_content;
        user_name=string_user_name;
    }
    public int getPostPhotoID(){
        return photoID;
    }
    public int getPostHeadID(){
        return headID;
    }

    public String getPostContent() {
        return content;
    }

    public String getPostUser_name() {
        return user_name;
    }

    public String getPostLove() {
        return love;
    }

    public String getPostTalk() {
        return talk;
    }

    public String getPostOrigin() {
        return origin;
    }
}
