package com.example.jianlou.my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.jianlou.R;
import com.example.jianlou.staticVar.StaticVar;
import com.leon.lib.settingview.LSettingItem;
import com.squareup.picasso.Picasso;

public class SettingPerson extends AppCompatActivity implements View.OnClickListener {
    private LSettingItem username,user_name;
    ImageView back,photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_person);
    }

    private void init() {
        back=findViewById(R.id.setting_person_back);
        photo=findViewById(R.id.setting_person_photo);
        username=findViewById(R.id.setting_person_username);
        user_name=findViewById(R.id.setting_person_user_name);
        username.setRightText(StaticVar.username);
        user_name.setRightText(StaticVar.user_name);
        user_name.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent=new Intent(SettingPerson.this,EditName.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(this);
        Picasso.get().load(R.mipmap.cat).transform(new CircleTransform()).into(photo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_person_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
