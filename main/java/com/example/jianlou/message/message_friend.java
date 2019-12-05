package com.example.jianlou.message;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.jianlou.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class message_friend extends AppCompatActivity {

    ImageView back;
    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_friend);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        back = findViewById(R.id.message_friend_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initMessage();
        RecyclerView recyclerView = findViewById(R.id.message_friend_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);

    }

    private void initMessage() {
        messageList.clear();
        for (int i = 1; i < 11; i++) {
            String content = "大家都来看看我发现了什么鬼东西";
            String user_name = "捡喽用户" + getRadom();
            Message message = new Message(R.mipmap.shequ0, content, user_name);
            messageList.add(message);
        }
    }

    /**
     * 获得随机数
     */
    private String getRadom() {
        Random random = new Random();
        int length = random.nextInt(10000) + 1;
        return String.valueOf(length);
    }

    /**
     * 响应菜单的
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = messageAdapter.getPosition();
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(message_friend.this, "还没有开发", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                new AlertDialog.Builder(this).setMessage("确认删除该数据？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                messageAdapter.removeData(position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
        }
        return super.onContextItemSelected(item);
    }

}
