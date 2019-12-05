package com.example.jianlou.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jianlou.R;

import java.util.ArrayList;
import java.util.List;

public class chat extends AppCompatActivity implements View.OnClickListener {

    private List<Msg> msgList=new ArrayList<>();
    private ImageView back;
    private TextView friend_name;
    private EditText message;
    private Button send;
    private RecyclerView recyclerView;
    private MSgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }

    private void init() {
        back=findViewById(R.id.chat_back);
        friend_name=findViewById(R.id.chat_friend_name);
        message=findViewById(R.id.chat_message);
        send=findViewById(R.id.chat_send);
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        Intent intent=getIntent();
        friend_name.setText(intent.getStringExtra("friend_name"));

        initMsg();
        recyclerView=findViewById(R.id.chat_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MSgAdapter(msgList);
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

    private void initMsg() {
        String content1="吃饭了吗？";
        String content2="吃了！";
        String content3="吃饱了吗？";
        String content4="嗯！";
        String content5="真的？";
        String content6="是的！";
        String content7="我说，今天晚上的话，是不是会没事干，要不我们？";
        String content8="我们？我们两个的晚上，会不会那个？";
        String content9="你想到那里去了，我们两个去写软件工程作业的代码如何？";
        msgList.add(new Msg(content1,Msg.TYPE_RECEIVED,R.mipmap.shequ0));
        msgList.add(new Msg(content2,Msg.TYPE_SEND,R.mipmap.cat));
        msgList.add(new Msg(content3,Msg.TYPE_RECEIVED,R.mipmap.shequ0));
        msgList.add(new Msg(content4,Msg.TYPE_SEND,R.mipmap.cat));
        msgList.add(new Msg(content5,Msg.TYPE_RECEIVED,R.mipmap.shequ0));
        msgList.add(new Msg(content6,Msg.TYPE_SEND,R.mipmap.cat));
        msgList.add(new Msg(content7,Msg.TYPE_RECEIVED,R.mipmap.shequ0));
        msgList.add(new Msg(content8,Msg.TYPE_SEND,R.mipmap.cat));
        msgList.add(new Msg(content9,Msg.TYPE_RECEIVED,R.mipmap.shequ0));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_back:
                finish();
                break;
            case R.id.chat_send:
                String content=message.getText().toString();
                if(!content.equals("")){
                    Msg msg =new Msg(content,Msg.TYPE_SEND,R.mipmap.cat);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    recyclerView.scrollToPosition(msgList.size()-1);
                    message.setText("");
                }
                break;
        }
    }
}
