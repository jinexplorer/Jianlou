package com.example.jianlou.shequ;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jianlou.R;
import com.example.jianlou.my.CircleTransform;
import com.squareup.picasso.Picasso;
import java.util.List;
/**
 * 社区中发布的帖子的类的RecyclerView瀑布流适配器
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> mPostLisht;

    static class ViewHolder extends RecyclerView.ViewHolder{
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
        }
    }
    public PostAdapter(List<Post> postList){
        mPostLisht=postList;
    }
    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shequ_recycler_good,parent,false);
        PostAdapter.ViewHolder holder=new PostAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=mPostLisht.get(position);
        Picasso.get().load(post.getPostHeadID()).transform(new CircleTransform()).into(holder.head);
        holder.photo.setImageResource(post.getPostPhotoID());
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
