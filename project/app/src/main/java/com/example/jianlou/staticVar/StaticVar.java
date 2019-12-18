package com.example.jianlou.staticVar;



public class StaticVar {

    //Url
    private static final String rootUrl="http://10.131.32.240:8000";
    public static final String publishUrl=rootUrl+"/publish/";
    public static final String userUrl=rootUrl+"/user/login/";
    public static final String registerUrl=rootUrl+"/user/register/";
    public static final String editNameUrl=rootUrl+"/user/editname/";
    public static final String friendUrl=rootUrl+"/chats/friends/";
    public static final String chatUrl=rootUrl+"/chats/";
    public static final String pushUrl=rootUrl+"/chats/push/";
    public static final String indexUrl=rootUrl+"/index/";
    public static final String searchUrl=rootUrl+"/search/";
    public static final String detailUrl=rootUrl+"/goods/detail/";
    public static final String imageUrl=rootUrl+"/image/";
    public static final String MyUrl=rootUrl+"/mygoods/";
    //存储键值对的键
    public static String fileName="session";
    public static String fileCookiename="cookie";
    public static String fileUserName="user_name";

    //预设信息
    public static String setUsername="12345678901";
    public static String setPassword="123456";

    //用户基本信息
    public static String cookie="";
    public static String user_name="";
    //常量
    public static final int LOGIN=1;
    public static final int PublishmoneyNUM=2;
    public static final int PublishClassify=3;
}
