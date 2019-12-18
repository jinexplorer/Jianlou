package com.example.jianlou.my;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jianlou.R;
import com.example.jianlou.index.good_detail;
import com.squareup.picasso.Picasso;

import java.util.List;
/**
 * 商品类的适配器，用于RecyclerView空间的瀑布平显示
 */
public class PublishAdapter extends RecyclerView.Adapter<PublishAdapter.ViewHolder> {
    private List<Publish> mGoodLisht;


    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView content,money;
        View goodView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.my_publish_photo);
            content=itemView.findViewById(R.id.my_publish_content);
            money=itemView.findViewById(R.id.my_publish_money);
            goodView=itemView;
        }
    }

    public PublishAdapter(List<Publish> goodList){
        mGoodLisht=goodList;
    }
    @NonNull
    @Override
    public PublishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.publish_recycler_good, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.goodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Publish good = mGoodLisht.get(position);
                Intent intent = new Intent(v.getContext(), good_detail.class);
                intent.putExtra("goodsID", good.getGoodID());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }
    /**
     * 修改空间的显示的
     */
    @Override
    public void onBindViewHolder(@NonNull PublishAdapter.ViewHolder holder, int position) {
            ViewHolder myViewHolder=(ViewHolder)holder;
            Publish good=mGoodLisht.get(position);
            Picasso.get().load(good.getPhotoID()).placeholder(R.mipmap.loading).transform(new RoundTransform(10,10)).into(myViewHolder.photo);
            myViewHolder.content.setText(good.getContent());
            myViewHolder.money.setText(good.getMoney());

    }

    @Override
    public int getItemCount() {
        return mGoodLisht.size();
    }

}

