package com.example.jianlou.publish.publish;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.jianlou.R;

public class Publish extends PopupWindow implements View.OnClickListener {

    //获得主活动
    private Activity mContext;
    //对背景进行高斯模糊处
    private int statusBarHeight;
    private Bitmap overlay = null;
    private Handler mHandler = new Handler();
    //获得layout文件
    private RelativeLayout layout;


    /**
     * 初始函数，初始活动
     */
    public Publish(Activity context) {
        mContext = context;
        init();
    }
    /**
     * 初始化函数，进行高斯处理的准备
     */
    public void init() {
        //不知道是哈，高斯模糊处理额准备吧
        Rect frame = new Rect();
        DisplayMetrics metrics = new DisplayMetrics();
        //应该是获得当前的见面，因为高斯处理需要对当前的背景图进行处理
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //不知道干嘛的，高宽？？？
        statusBarHeight = frame.top;
        setWidth(metrics.widthPixels);
        setHeight(metrics.heightPixels);
    }
    /**
     * 高斯处理，不懂，借鉴的别人的代码
     */
    private Bitmap blur() {
        if (null == overlay) {
            View view = mContext.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            Bitmap mBitmap = view.getDrawingCache();
            float scaleFactor = 8;
            float radius = 10;
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();
            overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int) (height / scaleFactor), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(mBitmap, 0, 0, paint);
            overlay = FastBlur.doBlur(overlay, (int) radius, true);
        }
        return overlay;
    }

    /**
     * 显示准备图片，不懂，借鉴的别人的代码
     */
    public void showPublish(View anchor) {
        //获得要显示的layout
        layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.fragment_add, null);
        ImageView close = layout.findViewById(R.id.publish_close);
        setContentView(layout);
        close.setOnClickListener(this);
        //显示layout
        showAnimation(layout);
        setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
    }
    /**
     * 显示图片，不懂，借鉴的别人的代码
     */
    private void showAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.publish_close) {
                continue;
            }
            child.setOnClickListener(this);
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
                    fadeAnim.setDuration(300);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(150);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50);
        }

    }
    /**
     * 关闭layout，不懂，借鉴的别人的代码
     */
    public void closeAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.publish_close) {
                continue;
            }
            child.setOnClickListener(this);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, 600);
                    fadeAnim.setDuration(200);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(100);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, (layout.getChildCount() - i - 1) * 30);

            if (child.getId() == R.id.publish_goods) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, (layout.getChildCount() - i) * 30 + 80);
            }
        }

    }
    /**
     * 处理layout中的三个按钮的点击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publish_goods:
                //进入发布活动界面
                Intent intent = new Intent("com.example.jianlou.PublishGoodActivity");
                mContext.startActivity(intent);
                //先关闭了再说，避免返回的时候再回来
                if (isShowing()) {
                    closeAnimation(layout);
                }
                break;
            case R.id.publish_content:
                Toast.makeText(mContext, "莫挨老子", Toast.LENGTH_SHORT).show();
                break;
            case R.id.publish_close:
                if (isShowing()) {
                    closeAnimation(layout);
                }
                break;
            default:
                break;
        }
    }

}
