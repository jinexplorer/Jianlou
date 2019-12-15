package com.example.jianlou.index;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jianlou.R;
import com.example.jianlou.my.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;
/**
 * 商品类的适配器，用于RecyclerView空间的瀑布平显示
 */
public class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.ViewHolder> {
    private List<Good> mGoodLisht;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photo,head;
        TextView content,money,user_name;
        View goodView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.index_photo);
            head=itemView.findViewById(R.id.index_head_photo);
            content=itemView.findViewById(R.id.index_content);
            money=itemView.findViewById(R.id.index_money);
            user_name=itemView.findViewById(R.id.index_user_name);
            goodView=itemView;
        }
    }
    public GoodAdapter(List<Good> goodList){
        mGoodLisht=goodList;
    }
    @NonNull
    @Override
    public GoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_recycler_good,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.goodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =holder.getAdapterPosition();
                Good good =mGoodLisht.get(position);
                Intent intent=new Intent(v.getContext(),good_detail.class);
                intent.putExtra("goodsID",good.getGoodID());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }
    /**
     * 修改空间的显示的
     */
    @Override
    public void onBindViewHolder(@NonNull GoodAdapter.ViewHolder holder, int position) {
        Good good=mGoodLisht.get(position);
        holder.photo.setImageBitmap(good.getPhotoID());
        Picasso.get().load(good.getHeadID()).transform(new CircleTransform()).into(holder.head);
        holder.content.setText(good.getContent());
        holder.money.setText(good.getMoney());
        holder.user_name.setText(good.getUser_name());
    }

    @Override
    public int getItemCount() {
        return mGoodLisht.size();
    }
}
