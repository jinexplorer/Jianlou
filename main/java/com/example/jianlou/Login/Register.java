
package com.example.jianlou.Login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jianlou.R;
import com.example.jianlou.staticVar.StaticVar;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private EditText phone_number,password,password_again;
    private ImageView clean_phone,clean_password,show_password,clean_password_again,show_password_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
    private void init(){
        phone_number=findViewById(R.id.register_phone_number);
        password=findViewById(R.id.register_password);
        password_again=findViewById(R.id.register_password_again);
        clean_phone=findViewById(R.id.register_clean_phone);
        clean_password=findViewById(R.id.register_clean_password);
        clean_password_again=findViewById(R.id.register_clean_password_again);
        show_password=findViewById(R.id.register_show_pwd);
        show_password_again=findViewById(R.id.register_show_pwd_again);

        Button register = findViewById(R.id.register_register);
        TextView login = findViewById(R.id.register_login);
        TextView contact = findViewById(R.id.register_contact);
        TextView about_us = findViewById(R.id.register_about_us);

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
        switch (v.getId()){
            case R.id.register_clean_phone:
                phone_number.setText(null);
                break;
            case R.id.register_clean_password:
                password.setText(null);
                break;
            case R.id.register_clean_password_again:
                password_again.setText(null);
            case R.id.register_show_pwd:
                if(password.getInputType()==129) {
                    password.setInputType(128);
                    show_password.setImageResource(R.mipmap.pass_visuable);
                }else {
                    password.setInputType(129);
                    show_password.setImageResource(R.mipmap.pass_gone);
                }
                break;
            case R.id.register_show_pwd_again:
                if(password_again.getInputType()==129) {
                    password_again.setInputType(128);
                    show_password_again.setImageResource(R.mipmap.pass_visuable);
                }else {
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
                Toast.makeText(Register.this,"不用找了，没有客服",Toast.LENGTH_SHORT).show();
                break;
            case R.id.register_about_us:
                Toast.makeText(Register.this,"站着撸代码的一群人",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void RegisterCheck(){
        String phoneNumber=phone_number.getText().toString();
        String pwd=password.getText().toString();
        String pwd_again=password_again.getText().toString();
        if(phoneNumber.equals("") || pwd.equals("") || pwd_again.equals("")){
            Toast.makeText(Register.this,"请填写账号密码",Toast.LENGTH_SHORT).show();
        }else if (phoneNumber.length()<11){
            Toast.makeText(Register.this,"请填写正确的手机号",Toast.LENGTH_SHORT).show();
        }else if(!pwd.equals(pwd_again)){
            Toast.makeText(Register.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
        }else {
            StaticVar.username=phoneNumber;
            StaticVar.password=pwd;
            Toast.makeText(Register.this,"注册成功",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
