package com.example.jianlou.my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.jianlou.Activity.MainActivity;
import com.example.jianlou.R;
import com.example.jianlou.staticVar.StaticVar;
import com.leon.lib.settingview.LSettingItem;

public class Setting extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        LSettingItem mSettingItemOne = findViewById(R.id.setting_person);
        mSettingItemOne.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent =new Intent(Setting.this,SettingPerson.class);
                startActivity(intent);
            }
        });
        ImageView back=findViewById(R.id.setting_back);
        Button missLogin=findViewById(R.id.setting_missLogin);
        back.setOnClickListener(this);
        missLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_back:
                finish();
                break;
            case R.id.setting_missLogin:
                //将登录为设置为未登录，同时修改存储登录信息的文件
                StaticVar.isLogin=false;
                SharedPreferences.Editor editor=getSharedPreferences(StaticVar.fileName,MODE_PRIVATE).edit();
                editor.putBoolean(StaticVar.fileIsLogin,StaticVar.isLogin);
                editor.putString(StaticVar.fileUsername,StaticVar.username);
                editor.apply();
                //强制回到主页，避免在登录之间不停循环
                MainActivity.VIEW_LAST_INDEX=0;
                finish();
                break;
        }
    }
}
