package com.example.jianlou.staticVar;



public class StaticVar {

    //Url
    public static final String publishUrl="http://10.131.32.240:8000/publish/";
    public static final String userUrl="http://10.131.32.240:8000/user/";
    public static final String registerUrl="http://10.131.32.240:8000/register/";
    public static final String editNameUrl="http://10.131.32.240:8000/editname/";
    public static final String indexUrl="http://10.131.32.240:8000/index/";
    //存储键值对的键
    public static String fileName="Login";
    public static String fileIsLogin="isLogin";
    public static String fileUsername="username";
    public static String fileUserName="user_name";
    //登录信息
    public static boolean isLogin=false;
    public static String username="";
    //预设信息
    public static String setUsername="12345678901";
    public static String setPassword="123456";

    //用户基本信息
    public static String user_name="";
    //常量
    public static final int LOGIN=1;
    public static final int PublishmoneyNUM=2;
    public static final int PublishClassify=3;
}
