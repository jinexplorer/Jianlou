package com.example.jianlou.my;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jianlou.Internet.HttpUtil;
import com.example.jianlou.R;
import com.example.jianlou.index.EndlessRecyclerOnScrollListener;
import com.example.jianlou.index.Good;
import com.example.jianlou.index.GoodAdapter;
import com.example.jianlou.index.search;
import com.example.jianlou.staticVar.StaticVar;
import com.example.jianlou.staticVar.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class myPublish extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private RecyclerView recyclerView;
    private List<Publish> goodList=new ArrayList<>();
    private PublishAdapter goodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish);
        init();
    }



    private void init() {
        back=findViewById(R.id.my_publish_back);
        recyclerView=findViewById(R.id.my_publish_recycle);
        back.setOnClickListener(this);
        StaggeredGridLayoutManager layoutManager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        goodAdapter=new PublishAdapter(goodList);
        recyclerView.setAdapter(goodAdapter);
        initGood();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_publish_back:
                finish();
                break;
        }
    }

    private void initGood() {
        goodList.clear();
        RequestBody requestBody=new FormBody.Builder()
                .add(Table.cookie,StaticVar.cookie)
                .build();
        HttpUtil.sendOkHttpRequest(StaticVar.MyUrl,requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                outputMessage("请求失败，请检查网络");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
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
                            String money=jsonObject.getString("money");
                            String content = jsonObject.getString("content");
                            String goodID = jsonObject.getString("goodsID");
                            String string = StaticVar.imageUrl+jsonObject.getString("image");
//                            byte[] bitmapArray = android.util.Base64.decode(string, Base64.DEFAULT);
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                            Uri uri=Uri.parse(string);
                            Publish good=new Publish(uri,content,money,goodID);
                            goodList.add(good);
                        }
                        updateGood();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }}
        else {
            outputMessage("服务器故障");
        }
    }
    private void outputMessage(String message){
        Looper.prepare();
        Toast.makeText(myPublish.this, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }


    private void updateGood(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                goodAdapter = new PublishAdapter(goodList);
                recyclerView.setAdapter(goodAdapter);
            }
        });
    }

}
