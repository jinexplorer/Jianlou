package com.example.jianlou.shequ;

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
import com.example.jianlou.my.RoundTransform;
import com.squareup.picasso.Picasso;
import java.util.List;
/**
 * 社区中发布的帖子的类的RecyclerView瀑布流适配器
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> mPostLisht;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View postView;
        ImageView photo,head;
        TextView user_name,love,talk,content,origin;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.shequ_photo);
            head=itemView.findViewById(R.id.shequ_head_photo);
            content=itemView.findViewById(R.id.shequ_content);
            love=itemView.findViewById(R.id.shequ_love);
            user_name=itemView.findViewById(R.id.shequ_user_name);
            talk=itemView.findViewById(R.id.shequ_talk);
            origin=itemView.findViewById(R.id.shequ_origin);
            postView=itemView;
        }
    }
    public PostAdapter(List<Post> postList){
        mPostLisht=postList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shequ_recycler_good,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.postView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =holder.getAdapterPosition();
                Post post =mPostLisht.get(position);
                Toast.makeText(v.getContext(),"你点击了"+post.getPostUser_name()+"的商品", Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=mPostLisht.get(position);
        Picasso.get().load(post.getPostHeadID()).transform(new CircleTransform()).into(holder.head);
        Picasso.get().load(post.getPostPhotoID()).placeholder(R.mipmap.loading).transform(new RoundTransform(10,0)).into(holder.photo);

        holder.content.setText(post.getPostContent());
        holder.user_name.setText(post.getPostUser_name());
        holder.talk.setText(post.getPostTalk());
        holder.love.setText(post.getPostLove());
        holder.origin.setText(post.getPostOrigin());
    }


    @Override
    public int getItemCount() {
        return mPostLisht.size();
    }
}
