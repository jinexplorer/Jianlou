package com.example.jianlou.my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jianlou.Internet.HttpUtil;
import com.example.jianlou.R;
import com.example.jianlou.staticVar.Table;
import com.example.jianlou.staticVar.StaticVar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * 设置项中的修改昵称的界面活动类
 */
public class EditName extends AppCompatActivity implements View.OnClickListener {
    private EditText user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        init();
    }

    private void init() {
        ImageView back = findViewById(R.id.edit_name_back);
        user_name = findViewById(R.id.edit_name_content);
        Button save = findViewById(R.id.edit_name_save);
        user_name.setText(StaticVar.user_name);
        user_name.requestFocus();
        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_name_back:
                finish();
                break;
            case R.id.edit_name_save:
                StaticVar.user_name = user_name.getText().toString();
                change_information();
                RequestBody requestBody = new FormBody.Builder()
                        .add(Table.username, StaticVar.username)
                        .add(Table.user_name, user_name.getText().toString())
                        .build();
                HttpUtil.sendOkHttpRequest(StaticVar.editNameUrl, requestBody, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        outputMessage("请求失败，请检查网络");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 200) {
                            String responseData;
                            if (response.body() != null) {
                                responseData = response.body().string();
                                switch (responseData) {
                                    case "success":
                                        outputMessage("修改成功");
                                        finish();
                                        break;
                                    default:
                                        outputMessage("修改失败");
                                }
                            }
                        } else {
                            outputMessage("服务器故障");
                        }
                    }
                });
                finish();
                break;
        }
    }
    /**
     * 用于在子线程中输出提示语
     */
    private void outputMessage(String message) {
        Looper.prepare();
        Toast.makeText(EditName.this, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    private void change_information(){
        SharedPreferences.Editor editor=getSharedPreferences(StaticVar.fileName,MODE_PRIVATE).edit();
        editor.putString(StaticVar.fileUserName,StaticVar.user_name);
        editor.apply();
    }
}
