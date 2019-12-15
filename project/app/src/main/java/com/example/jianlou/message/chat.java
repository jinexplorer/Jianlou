package com.example.jianlou.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jianlou.Internet.HttpUtil;
import com.example.jianlou.R;
import com.example.jianlou.staticVar.StaticVar;
import com.example.jianlou.staticVar.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chat extends AppCompatActivity implements View.OnClickListener {

    private List<Msg> msgList=new ArrayList<>();
    private ImageView back,reload;
    private TextView friend_name;
    private EditText message;
    private Button send;
    private RecyclerView recyclerView;
    private MSgAdapter adapter;
    private String username;
    private ProgressBar progressBar;
    private Timer timer = new Timer();
    private Timer timer2 = new Timer();
    private int lastTime=60;
    private String nowtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        timer.schedule(task,0,500);
        timer2.schedule(task2,0,1000);
    }

    private void init() {
        back=findViewById(R.id.chat_back);
        friend_name=findViewById(R.id.chat_friend_name);
        message=findViewById(R.id.chat_message);
        send=findViewById(R.id.chat_send);
        reload=findViewById(R.id.chat_reload);
        progressBar=findViewById(R.id.chat_bar);
        reload.setOnClickListener(this);
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        friend_name.setText(intent.getStringExtra("friend_name"));
        List<ChatMessage> chatMessages= DataSupport.select("content","type")
                .where("sender=?",username).order("time").find(ChatMessage.class);
        for(int i=0;i<chatMessages.size();i++){
            msgList.add(new Msg(chatMessages.get(i).getContent(),chatMessages.get(i).getType(),R.mipmap.cat));
        }

        if(msgList.size()==0){
            nowtime="";
        }else {
            List<ChatMessage> chatMessages2= DataSupport.select("time")
                    .where("sender=?",username).order("time desc").find(ChatMessage.class);
            for(int i=0;i<chatMessages2.size();i++){
                nowtime=chatMessages2.get(i).getTime();
            }
        }
        recyclerView=findViewById(R.id.chat_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter= new MSgAdapter(msgList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING ) {
                    InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(inputMethodManager!=null){
                        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    private void initMsg() {
        updateUI1();
        RequestBody requestBody=new FormBody.Builder()
                .add(Table.cookie,StaticVar.cookie)
                .add(Table.Username, username)
                .add("time",nowtime)
                .build();
        System.out.println(nowtime);
        HttpUtil.sendOkHttpRequest(StaticVar.chatUrl,requestBody, new Callback() {
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
                 String responseData = response.body().string();
                if(responseData.equals("failed")){
                    outputMessage("服务器错误");
                }else {
                    try {
                        JSONArray jsonArray=new JSONArray(responseData);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String username=jsonObject.getString("sender");
                            String content = jsonObject.getString("message");
                            String time = jsonObject.getString("send_time");
                            nowtime=time;
                            ChatMessage chatMessage=new ChatMessage();
                            if(username.equals(this.username)){
                                chatMessage.setContent(content);
                                chatMessage.setType(Msg.TYPE_RECEIVED);
                                chatMessage.setTime(time);
                                chatMessage.setSender(username);
                                chatMessage.save();
                                Msg msg =new Msg(content,Msg.TYPE_RECEIVED,R.mipmap.cat);
                                msgList.add(msg);
                            }else {
                                chatMessage.setContent(content);
                                chatMessage.setType(Msg.TYPE_SEND);
                                chatMessage.setTime(time);
                                chatMessage.setSender(username);
                                chatMessage.save();
                                Msg msg =new Msg(content,Msg.TYPE_SEND,R.mipmap.cat);
                                msgList.add(msg);
                            }
                        }
                        updateRecycle();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
    }

    private void updateRecycle() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter= new MSgAdapter(msgList);
                recyclerView.setAdapter(adapter);
                adapter.notifyItemInserted(msgList.size()-1);
                recyclerView.scrollToPosition(msgList.size()-1);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_back:
                finish();
                break;
            case R.id.chat_send:
                pushMessage();
                break;
            case R.id.chat_reload:
                initMsg();
                break;
        }
    }

    private void pushMessage() {
        lastTime=60;
        String content=message.getText().toString();
        if(!content.equals("")){
            RequestBody requestBody=new FormBody.Builder()
                    .add(Table.cookie,StaticVar.cookie)
                    .add(Table.Username, username)
                    .add(Table.message,content)
                    .build();
            HttpUtil.sendOkHttpRequest(StaticVar.pushUrl,requestBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    outputMessage("请求失败，请检查网络");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    updateUI();
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            String responseData = response.body().string();
                            if(responseData.equals("failed")){
                                outputMessage("服务器错误,未发送");
                            }else {
                            }
                        }}
                    else {
                        outputMessage("服务器故障");
                    }
                }
            });
            message.setText("");
        }
    }
    private void updateUI1(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
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
        Toast.makeText(chat.this, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if(lastTime>0){
                initMsg();
            }else {
                timer.cancel();
                task2.cancel();
            }
        }
    };

    TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            lastTime--;
        }
    };
}
