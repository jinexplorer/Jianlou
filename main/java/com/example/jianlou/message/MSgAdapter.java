package com.example.jianlou.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jianlou.R;
import com.example.jianlou.my.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MSgAdapter extends RecyclerView.Adapter<MSgAdapter.ViewHolder> {
    private List<Msg> mMsgList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Msg msg=mMsgList.get(position);
        if(msg.getMsgType()==Msg.TYPE_RECEIVED){
            //收到消息布局
            Picasso.get().load(msg.getMsgHeadID()).transform(new CircleTransform()).into(holder.leftHead);
            holder.leftlayout.setVisibility(View.VISIBLE);
            holder.rightlayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getMsgContent());
        }else if(msg.getMsgType()==Msg.TYPE_SEND){
            //发出消息布局
            Picasso.get().load(msg.getMsgHeadID()).transform(new CircleTransform()).into(holder.rightHead);
            holder.rightlayout.setVisibility(View.VISIBLE);
            holder.leftlayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getMsgContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftlayout;
        RelativeLayout rightlayout;
        TextView leftMsg,rightMsg;
        ImageView leftHead,rightHead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftlayout=itemView.findViewById(R.id.chat_left_layout);
            rightlayout=itemView.findViewById(R.id.chat_right_layout);
            leftMsg=itemView.findViewById(R.id.chat_left_message);
            rightMsg=itemView.findViewById(R.id.chat_right_message);
            leftHead=itemView.findViewById(R.id.chat_left_photo);
            rightHead=itemView.findViewById(R.id.chat_right_photo);
        }
    }
    public MSgAdapter(List<Msg> mMsgList) {
        this.mMsgList=mMsgList;
    }
}
