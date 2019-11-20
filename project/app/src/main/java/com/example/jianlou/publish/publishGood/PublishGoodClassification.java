package com.example.jianlou.publish.publishGood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jianlou.R;

import java.util.ArrayList;
import java.util.Objects;


public class PublishGoodClassification extends AppCompatActivity implements View.OnClickListener {
    private static final int CheckNum = 20;
    private int count = 0;
    private static final int maxLimit = 5;
    private CheckBox[] checkBoxes = new CheckBox[CheckNum];
    private ArrayList<String> result = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_good_classification);
        Button push = findViewById(R.id.PublishGoodClassify_publish);
        ImageView back = findViewById(R.id.PublishGoodClassify_back);

        push.setOnClickListener(this);
        back.setOnClickListener(this);

        /**
         * 为每一个复选框添加监听事件
         */
        for (int i = 0; i < CheckNum; i++) {
            String abc = "checkbox" + (i + 1);
            int id = getResources().getIdentifier(abc, "id", getPackageName());
            checkBoxes[i] = findViewById(id);
            PublishGoodActivity.map.put(checkBoxes[i].getText().toString(), String.valueOf(i));
            checkBoxes[i].setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (count == maxLimit && isChecked) {
                    buttonView.setChecked(false);
                    Toast.makeText(getApplicationContext(),
                            "最多选择5个", Toast.LENGTH_SHORT).show();
                } else if (isChecked) {
                    String myCheck = buttonView.getText().toString();
                    result.add(myCheck);
                    count++;

                } else {
                    String myCheck = buttonView.getText().toString();
                    result.remove(myCheck);
                    count--;
                }
            });
        }
        /**
         * 从上一个活动获得已选择的分类
         */
        Intent intent = getIntent();
        String[] get = intent.getStringArrayExtra("more");
        if (get != null) {
            for (String s : get) {
                int j;
                j = Integer.parseInt(Objects.requireNonNull(PublishGoodActivity.map.get(s)).trim());
                checkBoxes[j].setChecked(true);
            }
        }
    }
    /**
     * 响应事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PublishGoodClassify_back:
                finish();
                break;
            case R.id.PublishGoodClassify_publish:
                //将已经选择的分类以String的格式返回给上一个活动
                Intent intent = new Intent();
                String[] finally_result = new String[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    finally_result[i] = result.get(i);
                }
                intent.putExtra("more", finally_result);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
