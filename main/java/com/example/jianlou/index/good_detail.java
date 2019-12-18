package com.example.jianlou.index;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jianlou.Internet.HttpUtil;
import com.example.jianlou.message.chat;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jianlou.R;
import com.example.jianlou.my.CircleTransform;
import com.example.jianlou.staticVar.StaticVar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class good_detail extends AppCompatActivity implements View.OnClickListener {





    private String goodsID;
    ImageView back,chat,head;
    TextView user_name,money,content,origin_money,send_money,time;
    RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Photo> photoList=new ArrayList<>();
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        goodsID=getIntent().getStringExtra("goodsID");
        init();
    }

    private void init() {
        progressBar=findViewById(R.id.good_detail_bar);
        back=findViewById(R.id.good_detail_back);
        origin_money=findViewById(R.id.good_detail_origin_money);
        send_money=findViewById(R.id.good_detail_send_money);
        chat=findViewById(R.id.good_detail_user_message);
        user_name=findViewById(R.id.good_detail_user_name);
        money=findViewById(R.id.good_detail_money);
        content=findViewById(R.id.good_detail_content);
        recyclerView=findViewById(R.id.good_detail_recycle);
        head=findViewById(R.id.good_detail_photo);
        time=findViewById(R.id.good_detail_time);
        origin_money.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        money.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        back.setOnClickListener(this);
        chat.setOnClickListener(this);
        initPhoto();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void initPhoto() {
        photoList.clear();
        progressBar.setVisibility(View.VISIBLE);

        RequestBody requestBody=new FormBody.Builder()
                .add("goodsID",goodsID)
                .build();
        HttpUtil.sendOkHttpRequest(StaticVar.detailUrl,requestBody ,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                updateUI();
                outputMessage("请求失败，请检查网络");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                response(response);
                updateUI();
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
                        JSONObject jsonObject=new JSONObject(responseData);
                        username=jsonObject.getString("username");
                        String user_name=jsonObject.getString("user_name");
                        String origin_money=jsonObject.getString("origin_money");
                        String send_money=jsonObject.getString("send_money");
                        String money=jsonObject.getString("money");
                        String content=jsonObject.getString("content");
                        String images=jsonObject.getString("images");
                        String time=jsonObject.getString("time");
                        JSONArray jsonArray=new JSONArray(images);
                        for(int i=0;i<jsonArray.length();i++){
                            String string =StaticVar.imageUrl+jsonArray.getJSONObject(i).getString("image");
                            loadImage(string);
                        }
                        updatePhoto(user_name,money,content,origin_money,send_money,time);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }}
        else {
            outputMessage("服务器故障");
        }
    }

    private void loadImage(String string) {
        Uri uri=Uri.parse(string);
            photoList.add(new Photo(uri));
    }

    private void updatePhoto(String name,String mon,String con,String ori,String send,String times){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chat.setVisibility(View.VISIBLE);
                user_name.setText(name);
                money.setText(mon);
                content.setText(con);
                send_money.setText("("+send+")");
                origin_money.setText(ori);
                time.setText("发布于:"+times);
                Picasso.get().load(R.mipmap.cat).transform(new CircleTransform()).into(head);
                PhotoAdapter adapter = new PhotoAdapter(photoList);
                recyclerView.setAdapter(adapter);
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.good_detail_back:
                finish();
                break;
            case R.id.good_detail_user_message:
                Intent intent =new Intent(good_detail.this, com.example.jianlou.message.chat.class);
                intent.putExtra("username",username);
                intent.putExtra("friend_name",user_name.getText().toString());
                startActivity(intent);
                break;
        }
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
        Toast.makeText(good_detail.this, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}
