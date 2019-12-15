package com.example.jianlou.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.jianlou.Login.Login;
import com.example.jianlou.R;
import com.example.jianlou.staticVar.StaticVar;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class WoDeFragment extends Fragment implements View.OnClickListener {
    // 缓存Fragment view
    private View rootView;
    private  static WoDeFragment WoDeFragment;
    private TextView username,user_name;
    private ImageView setting,photo;


    public  WoDeFragment(){}
    public static WoDeFragment getNewInstance(){
        if (WoDeFragment ==null){
            WoDeFragment =new WoDeFragment();
        }
        return WoDeFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_wode, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void init() {
        username=getActivity().findViewById(R.id.username);
        user_name=getActivity().findViewById(R.id.user_name);
        setting=getActivity().findViewById(R.id.setting);
        photo=getActivity().findViewById(R.id.photo);

        setting.setOnClickListener(this);
        username.setText(StaticVar.cookie);
        user_name.setText(StaticVar.user_name);
        Picasso.get().load(R.mipmap.cat).transform(new CircleTransform()).into(photo);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(StaticVar.cookie.equals("")){Intent intent=new Intent(getContext(), Login.class);
            Objects.requireNonNull(getActivity()).startActivityForResult(intent,StaticVar.LOGIN);
        }else{
            init();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting:
                Intent intent=new Intent(getContext(),Setting.class);
                startActivity(intent);
        }
    }
}

