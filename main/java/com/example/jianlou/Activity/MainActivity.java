package com.example.jianlou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.jianlou.R;
import com.example.jianlou.shequ.SheQuFragment;
import com.example.jianlou.index.ShouYeFragment;
import com.example.jianlou.my.WoDeFragment;
import com.example.jianlou.message.XiaoXiFragment;
import com.example.jianlou.publish.publish.Publish;
import com.example.jianlou.staticVar.StaticVar;

public class MainActivity extends AppCompatActivity {


    private RadioGroup radioGroup;
    private FragmentTransaction mTransaction;

    /**
     * 中间的加号的发布类的实例
     */

    private Publish publish;
    /**
     * 四个Fragments，定义一些常量。
     */
    Fragment syFragment, sqFragment, xxFragment, wdFragment;
    public static final int VIEW_SHOUYE_INDEX = 0;
    public static final int VIEW_SHEQU_INDEX = 1;
    public static final int VIEW_XIAOXI_INDEX = 2;
    public static final int VIEW_WODE_INDEX = 3;
    private int temp_position_index = -1;
    public static int VIEW_LAST_INDEX = 0;

    /**
     * 每个activity创建的时候都要执行的方法
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_login();
        initView();
    }

    /**
     * 执行完onCreate执行的方法，用来将属性和layput绑定。
     */
    private void initView() {
        radioGroup = findViewById(R.id.id_navcontent);
        //绑定四个单选框
        syFragment = ShouYeFragment.getNewInstance();
        sqFragment = SheQuFragment.getNewInstance();
        xxFragment = XiaoXiFragment.getNewInstance();
        wdFragment = WoDeFragment.getNewInstance();
        //初始化选择，并选择首页作为默认选项
        transview(syFragment, VIEW_SHOUYE_INDEX);
    }

    /**
     * 当执行点击动作的时候的响应
     */
    public void switchView(View view) {
        switch (view.getId()) {
            case R.id.id_nav_btshouye:
                transview(syFragment, VIEW_SHOUYE_INDEX);
                break;
            case R.id.id_nav_btshequ:
                transview(sqFragment, VIEW_SHEQU_INDEX);
                break;
            case R.id.id_nav_btxiaoxi:
                transview(xxFragment, VIEW_XIAOXI_INDEX);
                break;
            case R.id.id_nav_btwode:
                transview(wdFragment, VIEW_WODE_INDEX);
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
    private void transview(Fragment choose, int nowindex) {
        if (temp_position_index != nowindex) {
            VIEW_LAST_INDEX = temp_position_index;
            mTransaction = getSupportFragmentManager().beginTransaction();
            mTransaction.replace(R.id.id_fragment_content, choose);
            mTransaction.commit();
            temp_position_index = nowindex;
        }
    }


    /**
     * 目前主要处理登录时候的成功和不成功返回问题
     * 如何取消登录的话需要回到上一个页面，为此我们一直在保存上一个界面，此时返回。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case StaticVar.LOGIN:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case StaticVar.LOGIN:
                    radioGroup.check(getID(VIEW_LAST_INDEX));
                    transview(getFragment(VIEW_LAST_INDEX), VIEW_LAST_INDEX);
            }
        }
    }

    /**
     * 通过下标找到对应的界面
     */
    private Fragment getFragment(int nowindex) {
        switch (nowindex) {
            case VIEW_SHOUYE_INDEX:
                return syFragment;
            case VIEW_SHEQU_INDEX:
                return sqFragment;
            case VIEW_XIAOXI_INDEX:
                return xxFragment;
            case VIEW_WODE_INDEX:
                return wdFragment;
        }
        return null;
    }

    /**
     * 通过下标找到对应的ID
     */
    private int getID(int nowindex) {
        switch (nowindex) {
            case VIEW_SHOUYE_INDEX:
                return R.id.id_nav_btshouye;
            case VIEW_SHEQU_INDEX:
                return R.id.id_nav_btshequ;
            case VIEW_XIAOXI_INDEX:
                return R.id.id_nav_btxiaoxi;
            case VIEW_WODE_INDEX:
                return R.id.id_nav_btwode;
        }
        return R.id.id_nav_btshouye;
    }

    /**
     * 初始化的时候，从文件中读取登录信息
     */
    private void get_login() {
        SharedPreferences preferences = getSharedPreferences(StaticVar.fileName, MODE_PRIVATE);
        StaticVar.isLogin = preferences.getBoolean(StaticVar.fileIsLogin, false);
        StaticVar.username = preferences.getString(StaticVar.fileUsername, "");
        StaticVar.user_name=preferences.getString(StaticVar.fileUserName,"捡喽用户"+StaticVar.username);
    }
}
