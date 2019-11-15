package com.example.jianlou.publish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jianlou.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PublishGoodClassification extends AppCompatActivity implements View.OnClickListener {
    private static final int CheckNum = 20;
    private int count = 0;
    private int i = 0;
    private static final int maxLimit = 5;
    private CheckBox[] checkBoxes = new CheckBox[CheckNum];
    private ArrayList<String> result=new ArrayList<String>();
    private Button push;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_good_classification);
        push=findViewById(R.id.PublishGoodClassify_publish);
        back=findViewById(R.id.PublishGoodClassify_back);

        push.setOnClickListener(this);
        back.setOnClickListener(this);


        for (i = 0; i < CheckNum; i++) {
            String abc="checkbox"+Integer.toString(i+1);
            int id = getResources().getIdentifier(abc, "id", getPackageName());
            checkBoxes[i] = findViewById(id);
            staticvar.map.put(checkBoxes[i].getText().toString(),String.valueOf(i));
            checkBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (count == maxLimit && isChecked) {
                        buttonView.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "最多选择5个", Toast.LENGTH_SHORT).show();
                    } else if (isChecked) {

                        String myCheck = buttonView.getText().toString();
                        result.add(myCheck);
                        count++;

                    } else if (!isChecked) {
                        String myCheck = buttonView.getText().toString();
                        result.remove(myCheck);
                        count--;
                    }
                }
            });
        }
        Intent intent=getIntent();
        int num=intent.getIntExtra("num",0);
        if(num>0){
            String[] get=intent.getStringArrayExtra("more1");
            for(int i=0;i<num;i++){
                int j=Integer.parseInt(staticvar.map.get(get[i]).trim());
                checkBoxes[j].setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.PublishGoodClassify_back:
                finish();
                break;
            case R.id.PublishGoodClassify_publish:
                Intent intent=new Intent();
                String[] finally_result=new String[result.size()];
                for(int i=0;i<result.size();i++){
                    finally_result[i]=result.get(i);
                }

                intent.putExtra("num",result.size());
                intent.putExtra("select",finally_result);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
