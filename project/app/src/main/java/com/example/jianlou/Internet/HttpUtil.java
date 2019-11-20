package com.example.jianlou.Internet;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
/**
 * 网络通信的类
 */
public class HttpUtil {
    /**
     * 网络通信的GET方式
     */
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client =new OkHttpClient();
        Request request = new Request.Builder()
                .url(address).build();
        client.newCall(request).enqueue(callback);

    }
    /**
     * 网络通信的POST方式
     */
    public static void sendOkHttpRequest(String address, RequestBody requestBody,okhttp3.Callback callback){
        OkHttpClient client =new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }
}
