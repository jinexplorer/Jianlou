package com.example.jianlou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.jianlou.R;
import com.example.jianlou.Fragment.SheQuFragment;
import com.example.jianlou.Fragment.ShouYeFragment;
import com.example.jianlou.Fragment.WoDeFragment;
import com.example.jianlou.Fragment.XiaoXiFragment;
import com.example.jianlou.publish.Publish;

public class MainActivity extends AppCompatActivity {
    /**
     * 底部导航栏的widdget
     */
    private RadioGroup mNavGroup;
    private Publish publish;
    private FragmentTransaction mTransaction;
    /**
     * 五个Fragments
     */
    Fragment syFragemnt, sqFragment, addFragment, xxFragment, wdFragment;
    public static final int VIEW_SHOUYE_INDEX = 0;
    public static final int VIEW_SHEQU_INDEX = 1;
    public static final int VIEW_ADD_INDEX = 2;
    public static final int VIEW_XIAOXI_INDEX = 3;
    public static final int VIEW_WODE_INDEX = 4;
    private int temp_position_index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mNavGroup = (RadioGroup) findViewById(R.id.id_navcontent);
        syFragemnt = ShouYeFragment.getNewInstance();
        sqFragment = SheQuFragment.getNewInstance();

        xxFragment = XiaoXiFragment.getNewInstance();
        wdFragment = WoDeFragment.getNewInstance();
        //显示
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.id_fragment_content, syFragemnt);
        mTransaction.commit();
    }
    public void switchView(View view) {
        switch (view.getId()) {
            case R.id.id_nav_btshouye:
                if (temp_position_index != VIEW_SHOUYE_INDEX) {
                    //显示
                    mTransaction = getSupportFragmentManager().beginTransaction();
                    mTransaction.replace(R.id.id_fragment_content, syFragemnt);
                    mTransaction.commit();
                }
                temp_position_index = VIEW_SHOUYE_INDEX;
                break;
            case R.id.id_nav_btshequ:
                if (temp_position_index != VIEW_SHEQU_INDEX) {
                    //显示
                    mTransaction = getSupportFragmentManager().beginTransaction();
                    mTransaction.replace(R.id.id_fragment_content, sqFragment);
                    mTransaction.commit();
                }
                temp_position_index = VIEW_SHEQU_INDEX;
                break;
            case R.id.id_nav_btadd:
                if (null == publish) {
                    publish = new Publish(this);
                    publish.init();
                }
                publish.showPublish(view,this);
                break;
            case R.id.id_nav_btxiaoxi:
                if (temp_position_index != VIEW_XIAOXI_INDEX) {
                    //显示
                    mTransaction = getSupportFragmentManager().beginTransaction();
                    mTransaction.replace(R.id.id_fragment_content, xxFragment);
                    mTransaction.commit();
                }
                temp_position_index = VIEW_XIAOXI_INDEX;
                break;
            case R.id.id_nav_btwode:
                if (temp_position_index != VIEW_WODE_INDEX) {
                    //显示
                    mTransaction = getSupportFragmentManager().beginTransaction();
                    mTransaction.replace(R.id.id_fragment_content, wdFragment);
                    mTransaction.commit();
                }
                temp_position_index = VIEW_WODE_INDEX;
                break;

        }
    }

}
