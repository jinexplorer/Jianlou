package com.example.jianlou.index;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jianlou.Internet.HttpUtil;
import com.example.jianlou.R;
import com.example.jianlou.staticVar.StaticVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class search extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private ImageView back;
    private EditText search;
    private RadioGroup check;
    private RecyclerView recyclerView;
    private List<Good> goodList=new ArrayList<>();
    private ProgressBar progressBar;
    private int NORMAL_LOADING=0;
    private int LOADING_MORE=1;
    private int maxPage=0;
    private int nowPage=1;
    private int id=1;
    private int state=NORMAL_LOADING;
    private GoodAdapter goodAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init() {
        Intent intent=getIntent();
        String result=intent.getStringExtra("result");
        back=findViewById(R.id.search_back);
        search=findViewById(R.id.search_search);
        check=findViewById(R.id.search_check);
        recyclerView=findViewById(R.id.search_recycle_view);
        progressBar=findViewById(R.id.search_progress);
        back.setOnClickListener(this);
        search.setText(result);
        search.setOnClickListener(this);
        check.setOnCheckedChangeListener(this);
        StaggeredGridLayoutManager layoutManager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        goodAdapter=new GoodAdapter(goodList);
        recyclerView.setAdapter(goodAdapter);
        initGood();

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                goodAdapter.setLoadState(GoodAdapter.LOADING);
                if (nowPage <= maxPage) {
                    // 模拟获取网络数据，延时1s
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    state=LOADING_MORE;
                                    initGood();
                                    goodAdapter.setLoadState(GoodAdapter.LOADING_COMPLETE);
                                }
                            });
                        }
                    }, 0);
                } else {
                    // 显示加载到底的提示
                    goodAdapter.setLoadState(GoodAdapter.LOADING_END);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_back:
                finish();
                break;
            case R.id.search_search:
                Intent intent=new Intent(search.this,index_search.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.search_check1:
                id=1;
                break;
            case R.id.search_check2:
                id=2;
                break;
            case R.id.search_check3:
                id=3;
                break;
        }

        goodAdapter.setLoadState(GoodAdapter.LOADING_COMPLETE);
        state=NORMAL_LOADING;
        nowPage=1;
        maxPage=0;
        initGood();
    }

    private void initGood() {
        progressBar.setVisibility(View.VISIBLE);
        if(state==NORMAL_LOADING){
            goodList.clear();
        }
        RequestBody requestBody=new FormBody.Builder()
                .add("method",String.valueOf(id))
                .add("q",search.getText().toString())
                .add("page",String.valueOf(nowPage))
                .build();
        HttpUtil.sendOkHttpRequest(StaticVar.searchUrl,requestBody, new okhttp3.Callback() {
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
                        nowPage++;
                        JSONArray jsonArray=new JSONArray(responseData);
                        for(int i=0;i<jsonArray.length()-1;i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String money=jsonObject.getString("money");
                            String content = jsonObject.getString("content");
                            String goodID = jsonObject.getString("goodsID");
                            String user_name=jsonObject.getString("user_name");
                            String string = StaticVar.imageUrl+jsonObject.getString("image");
//                            byte[] bitmapArray = android.util.Base64.decode(string, Base64.DEFAULT);
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                            Uri uri=Uri.parse(string);
                            Good good=new Good(uri,R.mipmap.cat,content,money,user_name,goodID);
                            goodList.add(good);
                        }
                        JSONObject jsonObject=jsonArray.getJSONObject(jsonArray.length()-1);
                        maxPage=Integer.parseInt(jsonObject.getString("max_page"));
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
        Toast.makeText(search.this, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
    private void updateUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
