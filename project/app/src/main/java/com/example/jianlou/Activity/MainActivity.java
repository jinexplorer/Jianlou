package com.example.jianlou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.jianlou.R;
import com.example.jianlou.Fragment.SheQuFragment;
import com.example.jianlou.Fragment.ShouYeFragment;
import com.example.jianlou.Fragment.WoDeFragment;
import com.example.jianlou.Fragment.XiaoXiFragment;
import com.example.jianlou.publish.publish.Publish;

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction mTransaction;

    /**
     * 中间的加号的发布类的实例
     */

    private Publish publish;
    /**
     * 四个Fragments，定义一些常量。
     */
    Fragment syFragemnt, sqFragment, xxFragment, wdFragment;
    public static final int VIEW_SHOUYE_INDEX = 0;
    public static final int VIEW_SHEQU_INDEX = 1;
    public static final int VIEW_XIAOXI_INDEX = 2;
    public static final int VIEW_WODE_INDEX = 3;
    private int temp_position_index = -1;


    /**
     * 每个activity创建的时候都要执行的方法
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    /**
     * 执行完onCreate执行的方法，用来将属性和layput绑定。
     */
    private void initView() {
        //绑定四个单选框
        syFragemnt = ShouYeFragment.getNewInstance();
        sqFragment = SheQuFragment.getNewInstance();
        xxFragment = XiaoXiFragment.getNewInstance();
        wdFragment = WoDeFragment.getNewInstance();
        //初始化选择，并选择首页作为默认选项
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.id_fragment_content, syFragemnt);
        mTransaction.commit();
    }

    /**
     * 当执行点击动作的时候的响应
     */
    public void switchView(View view) {
        switch (view.getId()) {
            case R.id.id_nav_btshouye:
                    transview(syFragemnt,VIEW_SHEQU_INDEX);
                break;
            case R.id.id_nav_btshequ:
                    transview( sqFragment,VIEW_SHEQU_INDEX);
                break;
            case R.id.id_nav_btxiaoxi:
                    transview(xxFragment,VIEW_XIAOXI_INDEX);
                break;
            case R.id.id_nav_btwode:
                    transview( wdFragment,VIEW_WODE_INDEX);
                break;
            //当点击中间的加号的时候,创建对象，展示界面
            case R.id.id_nav_btadd:
                if (null == publish) {
                    publish = new Publish(this);
                }
                publish.showPublish(view);
                break;

        }
    }
    /**
     * 根据多选框选择的项，将对应的页面跟换成对应的页面
     * nowindex:当前选择的下标
     * choose：选择替换的framment
     */
    public void transview(Fragment choose,int nowindex){
        if(temp_position_index!=nowindex){
            mTransaction = getSupportFragmentManager().beginTransaction();
            mTransaction.replace(R.id.id_fragment_content, choose);
            mTransaction.commit();
            temp_position_index=nowindex;
        }
    }
}
