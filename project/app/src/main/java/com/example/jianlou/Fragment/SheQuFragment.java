package com.example.jianlou.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.jianlou.R;


public class SheQuFragment extends Fragment {
    // 缓存Fragment view
    private View rootView;
    private static SheQuFragment SheQuFragment;
    public SheQuFragment(){}
    public static SheQuFragment getNewInstance(){
        if (SheQuFragment ==null){
            SheQuFragment =new SheQuFragment();
        }
        return SheQuFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_shequ, container, false);
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


