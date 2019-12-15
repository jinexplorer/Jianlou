package com.example.jianlou.index.classfiy;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.jianlou.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * author：wangzihang
 * date： 2017/8/8 19:15
 * desctiption：category demo
 * e-mail：wangzihang@xiaohongchun.com
 */
public class classify extends AppCompatActivity {

    private List<String> menuList = new ArrayList<>();
    private List<CategoryBean.DataBean> homeList = new ArrayList<>();
    private List<Integer> showTitle;

    private ListView lv_menu;
    private ListView lv_home;

    private MenuAdapter menuAdapter;
    private HomeAdapter homeAdapter;
    private int currentItem;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classfiy);
        Fresco.initialize(this);
        initView();
        loadData();
    }


    private void loadData() {

        String json = getJson(this, "category.json");
        CategoryBean categoryBean = JSONObject.parseObject(json,CategoryBean.class);
        showTitle = new ArrayList<>();
        for (int i = 0; i < categoryBean.getData().size(); i++) {
            CategoryBean.DataBean dataBean = categoryBean.getData().get(i);
            menuList.add(dataBean.getModuleTitle());
            showTitle.add(i);
            homeList.add(dataBean);
        }
        menuAdapter.notifyDataSetChanged();
        homeAdapter.notifyDataSetChanged();
    }

    private void initView() {
        lv_menu = findViewById(R.id.lv_menu);
        lv_home =  findViewById(R.id.lv_home);
        back=findViewById(R.id.classify_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        menuAdapter = new MenuAdapter(this, menuList);
        lv_menu.setAdapter(menuAdapter);
        homeAdapter = new HomeAdapter(this, homeList);
        lv_home.setAdapter(homeAdapter);

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuAdapter.setSelectItem(position);
                menuAdapter.notifyDataSetInvalidated();
                lv_home.setSelection(showTitle.get(position));
            }
        });
        lv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(classify.this,homeList.get(position).getMoreText(),Toast.LENGTH_SHORT).show();
            }
        });

        lv_home.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    return;
                }
                int current = showTitle.indexOf(firstVisibleItem);
//				lv_home.setSelection(current);
                if (currentItem != current && current >= 0) {
                    currentItem = current;
                    menuAdapter.setSelectItem(currentItem);
                    menuAdapter.notifyDataSetInvalidated();
                }
            }
        });
    }

    /**
     * 得到json文件中的内容
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
