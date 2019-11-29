package com.example.jianlou.shequ;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.jianlou.R;
import com.example.jianlou.index.Good;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
/**
 * 社区界面
 */

public class SheQuFragment extends Fragment {
    // 缓存Fragment view
    private View rootView;
    private static SheQuFragment SheQuFragment;
    private List<Post> postList=new ArrayList<>();

    public SheQuFragment(){}
    public static SheQuFragment getNewInstance(){
        if (SheQuFragment ==null){
            SheQuFragment =new SheQuFragment();
        }
        return SheQuFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_shequ, container, false);
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
     * 初始化瀑布流适配器
     */
    private void init() {
        initPosts();
        RecyclerView recyclerView= Objects.requireNonNull(getActivity()).findViewById(R.id.shequ_recycler_view);
        StaggeredGridLayoutManager layoutManager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        PostAdapter adapter =new PostAdapter(postList);
        recyclerView.setAdapter(adapter);
    }
    /**
     * 初始化社区界面瀑布流的商品的信息的
     */
    private void initPosts() {
        postList.clear();
        //文乃酱的单独初始化
        int id1 = getResources().getIdentifier("shequ0", "mipmap", Objects.requireNonNull(getActivity()).getPackageName());
        Post post1=new Post(id1,R.mipmap.shequ0,"刘进","100000人赞了","#文乃酱是我的#","各位抱歉，文乃酱我就先抱走了","来自我在想peach社区");
        postList.add(post1);
        for (int i=1;i<11;i++){
            String photoname="shequ"+i;
            String content="大家都来看看我发现了什么鬼东西";
            String user_name="捡喽用户"+getRadom();
            int id = getResources().getIdentifier(photoname, "mipmap", Objects.requireNonNull(getActivity()).getPackageName());
            String love = getRadom()+"人赞了";
            String origin = "来自表情包社区";
            String talk = "#我是话题#";
            Post post=new Post(id,R.mipmap.cat,user_name,love,talk,content,origin);
            postList.add(post);
        }
    }
    /**
     * 获得随机数
     */
    private String getRadom(){
        Random random=new Random();
        int length=random.nextInt(10000)+1;
        return String.valueOf(length);
    }
}


