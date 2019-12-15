package com.example.jianlou.index;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jianlou.Internet.HttpUtil;
import com.example.jianlou.R;
import com.example.jianlou.staticVar.StaticVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class search extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private ImageView back;
    private EditText search;
    private RadioGroup check;
    private RecyclerView recyclerView;
    private List<Good> goodList;
    private ProgressBar progressBar;
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
        int id=1;
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
        initGood(id);
    }

    private void initGood(int id) {
        progressBar.setVisibility(View.VISIBLE);
        RequestBody requestBody=new FormBody.Builder()
                .add("method",String.valueOf(id))
                .add("value",search.getText().toString())
                .build();
        HttpUtil.sendOkHttpRequest(StaticVar.userUrl,requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                updateUI();
                outputMessage("请求失败，请检查网络");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response(response)){
                    updateUI();
                    parseJSONWITHGSON(response.body().string());
                }
            }

            private void parseJSONWITHGSON(String string) {
                Gson gson =new Gson();
                goodList.clear();
                goodList=gson.fromJson(string,new TypeToken<List<Good>>(){}.getType());
                StaggeredGridLayoutManager layoutManager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                GoodAdapter adapter =new GoodAdapter(goodList);
                recyclerView.setAdapter(adapter);
            }
        });
    }
    private void outputMessage(String message){
        Looper.prepare();
        Toast.makeText(search.this, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    private boolean response(Response response) throws IOException {
        if (response.code() == 200) {
            String responseData;
            if (response.body() != null) {
                responseData = response.body().string();
                switch (responseData) {
                    case "failed":
                        outputMessage("未知错误");
                        break;
                    default:
                        return true;
                }}}
        else {
            outputMessage("服务器故障");
        }
        return false;
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
