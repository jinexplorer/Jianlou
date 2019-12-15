package com.example.jianlou.index;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.jianlou.Internet.HttpUtil;
import com.example.jianlou.R;
import com.example.jianlou.index.classfiy.classify;
import com.example.jianlou.staticVar.StaticVar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import android.util.Base64;

public class ShouYeFragment extends Fragment implements WaveSwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    // 缓存Fragment view
    private View rootView;
    private static ShouYeFragment shouYeFragment;
    private List<Good> goodList=new ArrayList<>();
    private RecyclerView recyclerView;
    private boolean first=true;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private TextView all,phone,game,book,pet;

    public ShouYeFragment(){}
    public static ShouYeFragment getNewInstance(){
        if (shouYeFragment ==null){
            shouYeFragment =new ShouYeFragment();
        }
        return shouYeFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_shouye, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }
    /**
     * 更新
     */
    @Override
    public void onRefresh() {
        refresh();
    }
    private void refresh(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 更新が終了したらインジケータ非表示
                initGoods();
                mWaveSwipeRefreshLayout.setWaveColor(0xFF000000+new Random().nextInt(0xFFFFFF)); // Random assign
            }
        }, 1000);
    }
    /**
     * 进行适配器的初始化
     */
    @Override
    public void onStart() {
        super.onStart();
        if(first){
            first=false;
            all=getView().findViewById(R.id.index_all_classfiy);
            phone=getView().findViewById(R.id.index_phone);
            game=getView().findViewById(R.id.index_game);
            book=getView().findViewById(R.id.index_book);
            pet=getView().findViewById(R.id.index_pet);
            all.setOnClickListener(this);
            phone.setOnClickListener(this);
            game.setOnClickListener(this);
            book.setOnClickListener(this);
            pet.setOnClickListener(this);
            mWaveSwipeRefreshLayout = getView().findViewById(R.id.index_swipe);
            mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
            mWaveSwipeRefreshLayout.setOnRefreshListener(this);
            mWaveSwipeRefreshLayout.setWaveColor(Color.argb(100,255,0,0));
            mWaveSwipeRefreshLayout.setRefreshing(true);
            recyclerView= getView().findViewById(R.id.index_recycler_view);
            StaggeredGridLayoutManager layoutManager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            EditText search=getView().findViewById(R.id.index_search);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(),index_search.class);
                    startActivity(intent);
                }
            });

            refresh();
        }
    }



    private void initGoods() {
        goodList.clear();
        HttpUtil.sendOkHttpRequest(StaticVar.indexUrl,new Callback() {
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
                            JSONArray jsonArray=new JSONArray(responseData);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String money=jsonObject.getString("money");
                                String content = jsonObject.getString("content");
                                String goodID = jsonObject.getString("goodsID");
                                String user_name=jsonObject.getString("user_name");
                                String string = jsonObject.getString("image");
                                byte[] bitmapArray = android.util.Base64.decode(string, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                                Good good=new Good(bitmap,R.mipmap.cat,content,money,user_name,goodID);
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
    private void updateGood(){
        (getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GoodAdapter goodAdapter = new GoodAdapter(goodList);
                recyclerView.setAdapter(goodAdapter);
            }
        });
    }

    private void updateUI(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void outputMessage(String message){
        Looper.prepare();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.index_all_classfiy:
                Intent intent=new Intent(getContext(), classify.class);
                getActivity().startActivity(intent);
                break;
            case R.id.index_phone:
                startactivity("手机");
                break;
            case R.id.index_game:
                startactivity("游戏");
                break;
            case R.id.index_book:
                startactivity("图书");
                break;
            case R.id.index_pet:
                startactivity("宠物");
                break;
        }
    }

    private void startactivity(String string) {
        Intent intent=new Intent(getContext(),search.class);
        intent.putExtra("result",string);
        startActivity(intent);
    }

}
