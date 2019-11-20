package com.example.jianlou.publish.publishGood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.jianlou.R;
/**
 * 发布界面的money活动界面
 */
public class PublishGoodMoney extends AppCompatActivity implements View.OnClickListener {


    RadioButton send,distance;
    private EditText m1,m2,m3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_good_money);
        init();
    }
    /**
     * 初始化
     */
    private void init(){
        //绑定属性
        Button push = findViewById(R.id.PublishGoodMoney_publish);
        ImageView back = findViewById(R.id.PublishGoodMoney_back);
        send=findViewById(R.id.PublishGoodMoney_send);
        distance=findViewById(R.id.PublishGoodMoney_distance);
        m1=findViewById(R.id.PublishGoodMoney_1);
        m2=findViewById(R.id.PublishGoodMoney_2);
        m3=findViewById(R.id.PublishGoodMoney_3);
        //监听
        push.setOnClickListener(this);
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        distance.setOnClickListener(this);
        m3.setOnClickListener(this);
    }
    /**
     * 处理对应的输出事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.PublishGoodMoney_back:
                finish();
                break;
            case R.id.PublishGoodMoney_publish:
                Intent intent=new Intent();
                intent.putExtra("money1",m1.getText().toString());
                intent.putExtra("money2",m2.getText().toString());
                intent.putExtra("money3",m3.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.PublishGoodMoney_send:
                m3.setText("包邮");
                m3.setFocusable(false);
                m3.setFocusableInTouchMode(false);
                break;
            case R.id.PublishGoodMoney_distance:
                m3.setText("按距离估算");
                m3.setFocusable(false);
                m3.setFocusableInTouchMode(false);
                break;
            case R.id.PublishGoodMoney_3:
                m3.setFocusableInTouchMode(true);
                m3.setFocusable(true);
                m3.requestFocus();
                send.setChecked(false);
                distance.setChecked(false);
                m3.setText(null);
                break;
        }}
}
