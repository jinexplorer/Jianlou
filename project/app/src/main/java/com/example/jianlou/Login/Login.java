package com.example.jianlou.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jianlou.Activity.MainActivity;
import com.example.jianlou.Fragment.WoDeFragment;
import com.example.jianlou.R;
import com.example.jianlou.staticVar.StaticVar;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText phone_number,password;
    private ImageView clean_phone,clean_password,show_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        phone_number=findViewById(R.id.login_phone_number);
        password=findViewById(R.id.login_password);
        clean_phone=findViewById(R.id.login_clean_phone);
        clean_password=findViewById(R.id.login_clean_password);
        show_password=findViewById(R.id.login_show_pwd);
        Button login = findViewById(R.id.login_login);
        TextView register = findViewById(R.id.login_register);
        TextView forget_passsword = findViewById(R.id.login_forget_password);
        TextView contact = findViewById(R.id.login_contact);
        TextView about_us = findViewById(R.id.login_about_us);
        
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

        clean_phone.setOnClickListener(this);
        clean_password.setOnClickListener(this);
        show_password.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget_passsword.setOnClickListener(this);
        contact.setOnClickListener(this);
        about_us.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_clean_phone:
                phone_number.setText(null);
                break;
            case R.id.login_clean_password:
                password.setText(null);
                break;
            case R.id.login_show_pwd:
                if(password.getInputType()==129) {
                    password.setInputType(128);
                    show_password.setImageResource(R.mipmap.pass_visuable);
                }else {
                    password.setInputType(129);
                    show_password.setImageResource(R.mipmap.pass_gone);
                }
                break;
            case R.id.login_login:
                loginCheck();
                break;
            case R.id.login_register:
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
                break;
            case R.id.login_forget_password:
                Toast.makeText(Login.this,"蠢货，密码都能忘",Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_contact:
                Toast.makeText(Login.this,"不用找了，没有客服",Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_about_us:
                Toast.makeText(Login.this,"站着撸代码的一群人",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void loginCheck(){
        String phoneNumber=phone_number.getText().toString();
        String pwd=password.getText().toString();
        if(phoneNumber.equals("") || pwd.equals("")){
            Toast.makeText(Login.this,"请填写账号密码",Toast.LENGTH_SHORT).show();
        }else if (phoneNumber.length()<11){
            Toast.makeText(Login.this,"请填写正确的手机号",Toast.LENGTH_SHORT).show();
        }else if(phoneNumber.equals(StaticVar.username)&&pwd.equals(StaticVar.password)){
            Intent intent =new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }else {
            Toast.makeText(Login.this,"账号/密码错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
    }
}
