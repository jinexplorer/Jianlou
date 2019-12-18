package com.example.jianlou.index;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.jianlou.R;
import com.example.jianlou.my.CircleTransform;
import com.example.jianlou.my.RoundTransform;
import com.squareup.picasso.Picasso;

import java.util.List;
/**
 * 商品类的适配器，用于RecyclerView空间的瀑布平显示
 */
public class GoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Good> mGoodLisht;
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public static final int LOADING = 1;
    // 加载完成
    public static final int LOADING_COMPLETE = 2;
    // 加载到底
    public static final int LOADING_END = 3;

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView photo,head;
        TextView content,money,user_name;
        View goodView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.index_photo);
            head=itemView.findViewById(R.id.index_head_photo);
            content=itemView.findViewById(R.id.index_content);
            money=itemView.findViewById(R.id.index_money);
            user_name=itemView.findViewById(R.id.index_user_name);
            goodView=itemView;
        }
    }
    private class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;

        FootViewHolder(View itemView) {
            super(itemView);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
        }
    }




    public GoodAdapter(List<Good> goodList){
        mGoodLisht=goodList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_recycler_good, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            holder.goodView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Good good = mGoodLisht.get(position);
                    Intent intent = new Intent(v.getContext(), good_detail.class);
                    intent.putExtra("goodsID", good.getGoodID());
                    v.getContext().startActivity(intent);
                }
            });
            return holder;
        }else if(viewType==TYPE_FOOTER){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_refresh_footer,parent,false);
            return new FootViewHolder(view);
        }
        return null;
    }
    /**
     * 修改空间的显示的
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder=(MyViewHolder)holder;
            Good good=mGoodLisht.get(position);
            Picasso.get().load(good.getPhotoID()).placeholder(R.mipmap.loading).transform(new RoundTransform(10,10)).into(myViewHolder.photo);
            Picasso.get().load(good.getHeadID()).placeholder(R.mipmap.loading).transform(new CircleTransform()).into(myViewHolder.head);
            myViewHolder.content.setText(good.getContent());
            myViewHolder.money.setText(good.getMoney());
            myViewHolder.user_name.setText(good.getUser_name());
        }else  if(holder instanceof FootViewHolder){
            FootViewHolder footViewHolder =(FootViewHolder)holder;
            switch (loadState){
                case LOADING:
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                case LOADING_COMPLETE:
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                case LOADING_END:
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.VISIBLE);
                    break;
                    default:
                        break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return mGoodLisht.size()+1;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }



    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            int position = holder.getLayoutPosition();
            //如果是上拉加载更多类型，则设置setFullSpan为true，那么它就会占一行
            if (getItemViewType(position) == TYPE_FOOTER) {
                params.setFullSpan(true);
            }else {
                params.setFullSpan(false);
            }
        }
    }

}
