package com.example.jianlou.message;


import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;

import android.view.MotionEvent;
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
 * 商品类的适配器，用于RecyclerView空间的瀑布平显示
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> mmessageList;
    private boolean longClicked = false;
    private int position;
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView head;
        TextView message, user_name;
        View messageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.message_friend_head);
            user_name = itemView.findViewById(R.id.message_friend_user_name);
            message = itemView.findViewById(R.id.message_friend_message);
            messageView = itemView;
            messageView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(ContextMenu.NONE, 0, ContextMenu.NONE, "置顶聊天");
            menu.add(ContextMenu.NONE, 1, ContextMenu.NONE, "删除聊天");
        }
    }

    public MessageAdapter(List<Message> messageList) {
        mmessageList = messageList;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_friend_recycler, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.messageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (longClicked) {
                        longClicked = false;
                        holder.messageView.setBackgroundResource(R.color.white);
                    } else {
                        holder.messageView.setBackgroundResource(R.color.color_eeeeee);
                        Intent intent=new Intent(v.getContext(),chat.class);
                        intent.putExtra("friend_name",holder.user_name.getText().toString());
                        v.getContext().startActivity(intent);
                    }
                }
                return false;
            }
        });
        holder.messageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                setPosition(position);
                holder.messageView.setBackgroundResource(R.color.color_eeeeee);
                longClicked = true;
                return false;
            }
        });

        return holder;
    }

    /**
     * 修改空间的显示的
     */
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = mmessageList.get(position);
        Picasso.get().load(message.getMessageHeadID()).transform(new CircleTransform()).into(holder.head);
        holder.message.setText(message.getMessageMessage());
        holder.user_name.setText(message.getMessageUser_name());
    }

    @Override
    public int getItemCount() {
        return mmessageList.size();
    }

    public void removeData(int position) {
        mmessageList.remove(position);
        notifyItemRemoved(position);
        if (position != mmessageList.size()) {
            notifyItemRangeChanged(position, mmessageList.size() - position);
        }
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
