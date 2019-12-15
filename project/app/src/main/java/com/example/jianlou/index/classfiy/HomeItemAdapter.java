package com.example.jianlou.index.classfiy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jianlou.R;
import com.example.jianlou.index.index_search;
import com.example.jianlou.index.search;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * author：wangzihang
 * date： 2017/8/8 19:15
 * desctiption：
 * e-mail：wangzihang@xiaohongchun.com
 */

public class HomeItemAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryBean.DataBean.DataListBean> foodDatas;

    public HomeItemAdapter(Context context, List<CategoryBean.DataBean.DataListBean> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
    }


    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 10;
        }
    }

    @Override
    public Object getItem(int position) {
        return foodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryBean.DataBean.DataListBean subcategory = foodDatas.get(position);
        Viewhold viewhold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home_category, null);
            viewhold = new Viewhold();
            viewhold.tv_name = (TextView) convertView.findViewById(R.id.item_home_name);
            viewhold.iv_icon = (SimpleDraweeView) convertView.findViewById(R.id.item_album);
            convertView.setTag(viewhold);
        } else {
            viewhold = (Viewhold) convertView.getTag();
        }
        viewhold.tv_name.setText(subcategory.getTitle());
        Viewhold finalViewhold = viewhold;
        viewhold.iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, search.class);
                intent.putExtra("result",finalViewhold.tv_name.getText().toString());
                context.startActivity(intent);
            }
        });
        Uri uri = Uri.parse(subcategory.getImgURL());
        viewhold.iv_icon.setImageURI(uri);
        return convertView;

    }

    private static class Viewhold {
        private TextView tv_name;
        private SimpleDraweeView iv_icon;
    }

}
