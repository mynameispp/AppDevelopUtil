package com.ffzxnet.developutil.ui.scancode.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivityResultContact;
import com.ffzxnet.developutil.ui.scancode.tools.EaseFileUtils;
import com.ffzxnet.developutil.ui.scancode.tools.VersionUtils;
import com.ffzxnet.developutil.ui.scancode.view.ViewfinderView;

import java.io.File;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.Nullable;

public class MyCaptureActivity extends BaseCaptureActivity implements SurfaceHolder.Callback {
    private ImageView back;
    private TextView scanner_toolbar_album;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_scancode;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        super.createdViewByBase(savedInstanceState);
        redirectActivityForResult(new BaseActivityResultContact(), new ActivityResultCallback() {
            @Override
            public void onActivityResult(Object result) {
                if (null != result) {
                    onActivityResultForLocalPhotos((Intent) result);
                }
            }
        });
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_content);
        this.back = (ImageView) findViewById(R.id.scanner_toolbar_back);
        scanner_toolbar_album = (TextView) findViewById(R.id.scanner_toolbar_album);
        this.back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyCaptureActivity.this.finish();
            }
        });
        scanner_toolbar_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //本地图片，打开本地相册，建议扫码是让用户裁剪二维码
                Intent intent = null;
                if (VersionUtils.isTargetQ(view.getContext())) {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                } else {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                intent.setType("image/*");
                resultLauncher.launch(intent);
            }
        });
    }

    @Override
    protected void onClickTitleBack() {

    }

    /**
     * 选择本地图片处理结果
     *
     * @param data
     */
    protected void onActivityResultForLocalPhotos(@Nullable Intent data) {
        if (data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                String filePath = EaseFileUtils.getFilePath(this, selectedImage);
                Log.e("图片路径", filePath);
                if (!TextUtils.isEmpty(filePath) && new File(filePath).exists()) {
                    scanAlbunUrl(filePath);
                } else {
                    Toast.makeText(this, "扫描失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

