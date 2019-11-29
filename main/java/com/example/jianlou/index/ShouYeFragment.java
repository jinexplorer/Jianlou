package com.example.jianlou.index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.jianlou.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class ShouYeFragment extends Fragment {
    // 缓存Fragment view
    private View rootView;
    private static ShouYeFragment shouYeFragment;
    private List<Good> goodList=new ArrayList<>();

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



    @Override
    public void onResume() {
        super.onResume();
        init();
    }
    /**
     * 进行适配器的初始化
     */
    private void init() {
        initGoods();
        RecyclerView recyclerView= getActivity().findViewById(R.id.index_recycler_view);
        StaggeredGridLayoutManager layoutManager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        GoodAdapter adapter =new GoodAdapter(goodList);
        recyclerView.setAdapter(adapter);
    }
    /**
     * 加载商品的基本信息到一个可变数组中去
     */
    private void initGoods() {
        goodList.clear();
        for (int i=0;i<7;i++){
            String photoname="index"+i;
            String content="爱买不买，都是破烂";
            String money="$ "+getRadom();
            String user_name="捡喽用户"+getRadom();
            int id = getResources().getIdentifier(photoname, "mipmap", Objects.requireNonNull(getActivity()).getPackageName());
            Good good=new Good(id,R.mipmap.cat,content,money,user_name);
            goodList.add(good);
        }
    }
    /**
     * 获得一个10000以内的随机数
     */
    private String getRadom(){
        Random random=new Random();
        int length=random.nextInt(10000)+1;
        return String.valueOf(length);
    }
}
