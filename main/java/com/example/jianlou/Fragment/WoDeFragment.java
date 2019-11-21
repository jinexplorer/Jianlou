package com.example.jianlou.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.jianlou.Activity.MainActivity;
import com.example.jianlou.Login.Login;
import com.example.jianlou.R;
import com.example.jianlou.staticVar.StaticVar;

import java.util.Objects;


public class WoDeFragment extends Fragment {
    // 缓存Fragment view
    private View rootView;
    private  static WoDeFragment WoDeFragment;


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
        if(StaticVar.isLogin){
        }else{
            Intent intent=new Intent(getContext(), Login.class);
            Objects.requireNonNull(getActivity()).startActivityForResult(intent,MainActivity.LOGIN);
        }
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
    @Override
    public void onResume() {
        super.onResume();
    }


}

