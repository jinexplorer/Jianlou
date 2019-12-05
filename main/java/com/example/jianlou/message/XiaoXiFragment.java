package com.example.jianlou.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.jianlou.Login.Login;
import com.example.jianlou.R;
import com.example.jianlou.staticVar.StaticVar;

import java.util.Objects;


public class XiaoXiFragment extends Fragment {
    // 缓存Fragment view
    private View rootView;
    private static XiaoXiFragment XiaoXiFragment;
    public XiaoXiFragment(){}
    public static XiaoXiFragment getNewInstance(){
        if (XiaoXiFragment ==null){
            XiaoXiFragment =new XiaoXiFragment();
        }
        return XiaoXiFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(StaticVar.isLogin){
        }else{
            Intent intent=new Intent(getContext(), Login.class);
            Objects.requireNonNull(getActivity()).startActivityForResult(intent, StaticVar.LOGIN);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_xiaoxi, container, false);
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
        init();
    }

    private void init() {
        TextView interact = getActivity().findViewById(R.id.message_interact);
        interact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),message_friend.class);
                startActivity(intent);
            }
        });
    }
}

