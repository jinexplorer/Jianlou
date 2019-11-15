package com.example.jianlou.publish;

import android.Manifest;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.jianlou.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PublishGoodActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = PublishGoodActivity.class.getSimpleName();
    private List<LocalMedia> selectList = new ArrayList<>();
    private RecyclerView publishGood_addphoto;
    private GridImageAdapter adapter;
    private int maxSelectNum = 8;
    private ImageView publishGood_back;
    private TextView money1,more1;
    private EditText content;
    private  Button publish;
    private RelativeLayout money,more;

    private int themeId;
    private int chooseMode = PictureMimeType.ofAll();
    private int statusBarColorPrimaryDark;
    private int upResId, downResId;

    private static final int PublishmoneyNUM=1;
    private static final int PublishClassify=2;
    private String[] result1;
    private int num=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_good);

        publishGood_addphoto = findViewById(R.id.PublishGood_addPhoto);
        publishGood_back = findViewById(R.id.PublishGood_back);
        content=findViewById(R.id.PublishGood_content);
        publish=findViewById(R.id.PublishGood_publish);
        money=findViewById(R.id.Publishmoney);
        more=findViewById(R.id.Publishmore);
        money1=findViewById(R.id.Publishmoney_1);
        more1=findViewById(R.id.Publishmore_1);


        publishGood_back.setOnClickListener(this);
        publish.setOnClickListener(this);
        money.setOnClickListener(this);
        more.setOnClickListener(this);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(PublishGoodActivity.this, 4, GridLayoutManager.VERTICAL, false);
        publishGood_addphoto.setLayoutManager(manager);
        adapter = new GridImageAdapter(PublishGoodActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        publishGood_addphoto.setAdapter(adapter);
        themeId = R.style.picture_QQ_style;
        statusBarColorPrimaryDark = R.color.blue;
        upResId = R.drawable.arrow_up;
        downResId = R.drawable.arrow_down;
        adapter.setOnItemClickListener((position, v) -> {
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:
                        // 预览视频
                        PictureSelector.create(PublishGoodActivity.this).externalPictureVideo(media.getPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:
                        // 预览音频
                        PictureSelector.create(PublishGoodActivity.this).externalPictureAudio(media.getPath());
                        break;
                    default:
                        // 预览图片 可自定长按保存路径
                        PictureSelector.create(PublishGoodActivity.this)
                                .themeStyle(themeId)
                                .openExternalPreview(position, selectList);
                        break;
                }
            }
        });
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(PublishGoodActivity.this, PictureMimeType.ofImage());
                } else {
                    Toast.makeText(PublishGoodActivity.this,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
                PictureSelector.create(PublishGoodActivity.this)
                        .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .cameraFileName("")// 使用相机时保存至本地的文件名称,注意这个只在拍照时可以使用，选图时不要用
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                        .isSingleDirectReturn(false)// 单选模式下是否直接返回
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(true)// 是否可预览视频
                        .enablePreviewAudio(false) // 是否可播放音频
                        .isCamera(true)// 是否显示拍照按钮
                        .isChangeStatusBarFontColor(false)// 是否关闭白色状态栏字体颜色
                        .setStatusBarColorPrimaryDark(statusBarColorPrimaryDark)// 状态栏背景色
                        .setUpArrowDrawable(upResId)// 设置标题栏右侧箭头图标
                        .setDownArrowDrawable(downResId)// 设置标题栏右侧箭头图标
                        .isOpenStyleCheckNumMode(true)// 是否开启数字选择模式 类似QQ相册
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .enableCrop(false)// 是否裁剪
                        .compress(true)// 是否压缩
                        .synOrAsy(false)//同步true或异步false 压缩 默认同步
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .isGif(true)// 是否显示gif图片
                        .openClickSound(false)// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
                        .cutOutQuality(90)// 裁剪输出质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectList) {
                        Log.i(TAG, "压缩---->" + media.getCompressPath());
                        Log.i(TAG, "原图---->" + media.getPath());
                        Log.i(TAG, "裁剪---->" + media.getCutPath());
                        Log.i(TAG, "Android Q 特有Path---->" + media.getAndroidQToPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
                case PublishmoneyNUM:
                    String m1=data.getStringExtra("money1");
                    String m2=data.getStringExtra("money2");
                    String m3=data.getStringExtra("money3");
                    m1=m1.equals("")?"0":m1;
                    m3=m3.equals("")?"0":m3;
                    money1.setTextColor(Color.rgb(0, 0, 0));
                    String result=m1+"  运费("+m3+")";
                    money1.setText(result);
                    break;
                case PublishClassify:
                    num=data.getIntExtra("num",0);
                    if(num>0) {
                        result1 = data.getStringArrayExtra("select");
                        String output = new String();
                        for (int i = 0; i < num; i++) {
                            output += result1[i]+"/";
                        }
                        if(num>3){
                            more1.setTextSize(12);
                        }
                        more1.setTextColor(Color.rgb(0, 0, 0));
                        more1.setText(output);
                    }

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.PublishGood_publish:
                String publish_content = content.getText().toString().trim();
                if (publish_content.length() == 0 && selectList.size()==0) {
                    Toast.makeText(this, "必须填写物品书名或选择照片", Toast.LENGTH_SHORT).show();
                }else if(money1.getText().toString().equals("0  运费(0)")||money1.getText().toString().equals("")){
                    Toast.makeText(this, "必须填写一个价格", Toast.LENGTH_SHORT).show();
                }else if(money1.getText().toString().equals("")){
                    Toast.makeText(this, "请至少选择一个分类", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "发布成功,程序员在加班加点制作之后界面中！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.PublishGood_back:
                Intent intent=new Intent();
                setResult(RESULT_CANCELED,intent);
                finish();
                break;
            case R.id.Publishmoney:
                Intent intent1=new Intent(PublishGoodActivity.this,PublishGoodMoney.class);
                startActivityForResult(intent1,PublishmoneyNUM);
                break;
            case R.id.Publishmore:
                Intent intent2=new Intent(PublishGoodActivity.this,PublishGoodClassification.class);
                if(num>0){
                    intent2.putExtra("num",num);
                    intent2.putExtra("more1",result1);
                }
                else{
                    intent2.putExtra("num",0);
                }
                startActivityForResult(intent2,PublishClassify);
                break;
        }
    }

}
