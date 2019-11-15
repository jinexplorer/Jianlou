package com.example.jianlou.publish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.jianlou.R;

public class PublishGoodMoney extends AppCompatActivity implements View.OnClickListener {


    private Button push;
    private ImageView back;
    RadioButton send,distance;
    private EditText m1,m2,m3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_good_money);
        init();
    }

    private void init(){
        push=findViewById(R.id.PublishGoodMoney_publish);
        back=findViewById(R.id.PublishGoodMoney_back);
        send=findViewById(R.id.PublishGoodMoney_send);
        distance=findViewById(R.id.PublishGoodMoney_distance);
        m1=findViewById(R.id.PublishGoodMoney_1);
        m2=findViewById(R.id.PublishGoodMoney_2);
        m3=findViewById(R.id.PublishGoodMoney_3);

        push.setOnClickListener(this);
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        distance.setOnClickListener(this);

    }

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
                break;
            case R.id.PublishGoodMoney_distance:
                m3.setText("按距离估算");
                break;
        }
    }
}
