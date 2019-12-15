
package com.example.jianlou.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class Register extends AppCompatActivity implements View.OnClickListener {
    private EditText phone_number, password, password_again;
    private ImageView clean_phone, clean_password, show_password, clean_password_again, show_password_again,left,right;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        phone_number = findViewById(R.id.register_phone_number);
        password = findViewById(R.id.register_password);
        password_again = findViewById(R.id.register_password_again);
        clean_phone = findViewById(R.id.register_clean_phone);
        clean_password = findViewById(R.id.register_clean_password);
        clean_password_again = findViewById(R.id.register_clean_password_again);
        show_password = findViewById(R.id.register_show_pwd);
        show_password_again = findViewById(R.id.register_show_pwd_again);
        left=findViewById(R.id.register_left);
        right=findViewById(R.id.register_right);
        progressBar=findViewById(R.id.register_progress);
        Button register = findViewById(R.id.register_register);
        TextView login = findViewById(R.id.register_login);
        TextView contact = findViewById(R.id.register_contact);
        TextView about_us = findViewById(R.id.register_about_us);
        //用来监听文本框的输入，输入的时候，显示一个用于清除所有输入的X号
        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clean_phone.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clean_password.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    left.setImageResource(R.mipmap.login_icon_left_close);
                    right.setImageResource(R.mipmap.login_icon_right_close);
                } else {
                    // 此处为失去焦点时的处理内容
                    left.setImageResource(R.mipmap.login_icon_left);
                    right.setImageResource(R.mipmap.login_icon_right);
                }
            }
        });
        password_again.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clean_password_again.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password_again.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    left.setImageResource(R.mipmap.login_icon_left_close);
                    right.setImageResource(R.mipmap.login_icon_right_close);
                } else {
                    // 此处为失去焦点时的处理内容
                    left.setImageResource(R.mipmap.login_icon_left);
                    right.setImageResource(R.mipmap.login_icon_right);
                }
            }
        });
        clean_phone.setOnClickListener(this);
        clean_password.setOnClickListener(this);
        clean_password_again.setOnClickListener(this);
        show_password.setOnClickListener(this);
        show_password_again.setOnClickListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        contact.setOnClickListener(this);
        about_us.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_clean_phone:
                phone_number.setText(null);
                break;
            case R.id.register_clean_password:
                password.setText(null);
                break;
            case R.id.register_clean_password_again:
                password_again.setText(null);
            case R.id.register_show_pwd:
                //切换密码的可见与不可见
                if (password.getInputType() == 129) {
                    password.setInputType(128);
                    show_password.setImageResource(R.mipmap.pass_visuable);
                } else {
                    password.setInputType(129);
                    show_password.setImageResource(R.mipmap.pass_gone);
                }
                break;
            case R.id.register_show_pwd_again:
                //切换密码的可见与不可见
                if (password_again.getInputType() == 129) {
                    password_again.setInputType(128);
                    show_password_again.setImageResource(R.mipmap.pass_visuable);
                } else {
                    password_again.setInputType(129);
                    show_password_again.setImageResource(R.mipmap.pass_gone);
                }
                break;
            case R.id.register_register:
                RegisterCheck();
                break;
            case R.id.register_login:
                finish();
                break;
            case R.id.register_contact:
                Toast.makeText(Register.this, "不用找了，没有客服", Toast.LENGTH_SHORT).show();
                break;
            case R.id.register_about_us:
                Toast.makeText(Register.this, "站着撸代码的一群人", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 用于注册的时候对输入信息进行基本的过滤和判断
     */
    private void RegisterCheck() {
        String phoneNumber = phone_number.getText().toString();
        String pwd = password.getText().toString().trim();
        String pwd_again = password_again.getText().toString().trim();
        if (phoneNumber.equals("") || pwd.equals("") || pwd_again.equals("")) {
            Toast.makeText(Register.this, "请填写账号密码", Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.length() < 11) {
            Toast.makeText(Register.this, "请填写正确的手机号", Toast.LENGTH_SHORT).show();
        } else if (!pwd.equals(pwd_again)) {
            Toast.makeText(Register.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            RequestBody requestBody = new FormBody.Builder()
                    .add(Table.username, phoneNumber)
                    .add(Table.password, pwd)
                    .build();
            HttpUtil.sendOkHttpRequest(StaticVar.registerUrl, requestBody, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    updateUI();
                    outputMessage("请求失败，请检查网络");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    updateUI();
                    if (response.code() == 200) {
                        String responseData;
                        if (response.body() != null) {
                            responseData = response.body().string();
                            switch (responseData) {
                                case "success":
                                    finish();
                                    break;
                                case "failed":
                                    outputMessage("账号已存在");
                                    break;
                                default:
                                    outputMessage("未知错误");
                            }
                        }
                    } else {
                        outputMessage("服务器故障");
                    }
                }
            });
        }
    }


    /**
     * 用于在子线程中输出提示语
     */
    private void outputMessage(String message) {
        Looper.prepare();
        progressBar.setVisibility(View.GONE);
        Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    private void updateUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
