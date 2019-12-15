package com.example.jianlou.message;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jianlou.Internet.HttpUtil;
import com.example.jianlou.Login.Login;
import com.example.jianlou.R;
import com.example.jianlou.staticVar.StaticVar;
import com.example.jianlou.staticVar.Table;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class message_friend extends AppCompatActivity {

    ImageView back;
    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_friend);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        back = findViewById(R.id.message_friend_back);
        progressBar=findViewById(R.id.message_friend_bar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initMessage();
        recyclerView = findViewById(R.id.message_friend_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void initMessage() {
        progressBar.setVisibility(View.VISIBLE);
        messageList.clear();
        RequestBody requestBody=new FormBody.Builder()
                .add(Table.cookie,StaticVar.cookie)
                .build();
        HttpUtil.sendOkHttpRequest(StaticVar.friendUrl,requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                updateUI();
                outputMessage("请求失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                updateUI();
                response(response);
            }
        });
    }

    private void response(Response response) throws IOException {
        if (response.code() == 200) {
            if (response.body() != null) {
                String responseData = response.body().string();
                if(responseData.equals("failed")){
                    outputMessage("服务器错误");
                }else {
                    try {
                        JSONArray jsonArray=new JSONArray(responseData);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String username=jsonObject.getString("username");
                            String content = jsonObject.getString("message");
                            String user_name = jsonObject.getString("user_name");
                            Message message = new Message(R.mipmap.shequ0, content, user_name,username);
                            messageList.add(message);
                        }
                        updateRecycle();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }}
        else {
            outputMessage("服务器故障");
        }
    }

    private void updateRecycle() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter = new MessageAdapter(messageList);
                recyclerView.setAdapter(messageAdapter);
            }
        });
    }


    /**
     * 获得随机数
     */
    private String getRadom() {
        Random random = new Random();
        int length = random.nextInt(10000) + 1;
        return String.valueOf(length);
    }

    /**
     * 响应菜单的
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = messageAdapter.getPosition();
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(message_friend.this, "还没有开发", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                new AlertDialog.Builder(this).setMessage("确认删除该数据？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                messageAdapter.removeData(position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
        }
        return super.onContextItemSelected(item);
    }
    private void updateUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void outputMessage(String message){
        Looper.prepare();
        Toast.makeText(message_friend.this, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

}
