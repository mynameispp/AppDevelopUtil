package com.ffzxnet.developutil.ui.scancode;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.base.ui.BaseActivityResultContact;
import com.ffzxnet.developutil.base.ui.CheckPermissionDialogCallBak;
import com.ffzxnet.developutil.ui.scancode.activity.MyCaptureActivity;
import com.ffzxnet.developutil.ui.scancode.activity.MyCaptureByCameraXActivity;
import com.ffzxnet.developutil.ui.scancode.encoding.EncodingHandler;
import com.ffzxnet.developutil.ui.scancode.tools.VersionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

public class TestScanCodeActivity extends BaseActivity {
    @BindView(R.id.scan_code)
    Button scanCode;
    @BindView(R.id.scan_code_result)
    TextView scanCodeResult;
    @BindView(R.id.create_code_ed)
    EditText createCodeEd;
    @BindView(R.id.create_code_img_btn)
    Button createCodeImgBtn;
    @BindView(R.id.create_code_img)
    ImageView createCodeImg;
    @BindView(R.id.save_create_code_img_btn)
    Button saveCreateCodeImgBtn;

    private String SAVE_PIC_PATH;//保存到SD卡
    private Bitmap erCodeBitmap;//生成的二维码图
    private MyHandler myHandler;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_test_sacan_code;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "二维码");

        redirectActivityForResult(new BaseActivityResultContact(), new ActivityResultCallback() {
            @Override
            public void onActivityResult(Object result) {
                if (result instanceof String) {
                    scanCodeResult.setText((String) result);
                }
            }
        });
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @OnClick({R.id.scan_code, R.id.scan_code2, R.id.create_code_img_btn, R.id.save_create_code_img_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scan_code:
                CheckPermissionDialog(new CheckPermissionDialogCallBak() {
                    @Override
                    public void hasPermission(boolean success) {
                        if (success) {
                            Intent intent = new Intent(TestScanCodeActivity.this, MyCaptureActivity.class);
                            resultLauncher.launch(intent);
                        }
                    }
                }, Manifest.permission.CAMERA);
                break;
            case R.id.scan_code2:
                CheckPermissionDialog(new CheckPermissionDialogCallBak() {
                    @Override
                    public void hasPermission(boolean success) {
                        if (success) {
                            Intent intent = new Intent(TestScanCodeActivity.this, MyCaptureByCameraXActivity.class);
                            resultLauncher.launch(intent);
                        }
                    }
                }, Manifest.permission.CAMERA);
                break;
            case R.id.create_code_img_btn:
                //创建二维码
                String content = createCodeEd.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(view.getContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                erCodeBitmap = EncodingHandler.createQRCodeBitmap(content, 150, 150, "utf-8"
                        , "H", "1", Color.BLUE, Color.WHITE);
                createCodeImg.setImageBitmap(erCodeBitmap);
                break;
            case R.id.save_create_code_img_btn:
                //保存创建二维码
                if (null == myHandler) {
                    myHandler = new MyHandler(new SoftReference<Context>(view.getContext()));
                }
//                SAVE_PIC_PATH = view.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
//                        + "/ercode/" + System.currentTimeMillis() + ".jpg";

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SAVE_PIC_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/";
                            if (VersionUtils.isTargetQ(view.getContext())) {
                                saveBitmap(view.getContext(), erCodeBitmap, System.currentTimeMillis() + ".jpg");
                            } else {
                                saveFile(erCodeBitmap, SAVE_PIC_PATH, System.currentTimeMillis() + ".jpg");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            myHandler.sendEmptyMessage(-1);
                        }
                    }
                }).start();
                break;
        }
    }

    public void saveFile(Bitmap bitmap, String path, String bitName) throws Exception {
        File file = new File(path, bitName);
        file.mkdirs();
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
                //保存图片后发送广播通知更新数据库
                // Uri uri = Uri.fromFile(file);
                // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                sendBroadcast(intent);
                myHandler.sendEmptyMessage(1);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBitmap(Context context, Bitmap bitmap, String bitName) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DESCRIPTION, "二维码");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, bitName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        values.put(MediaStore.Images.Media.TITLE, bitName);
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera");

        Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        Uri insertUri = resolver.insert(external, values);
        OutputStream os = null;
        if (insertUri != null) {
            try {
                os = resolver.openOutputStream(insertUri);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
                myHandler.sendEmptyMessage(1);
            } catch (IOException e) {
                e.printStackTrace();
                myHandler.sendEmptyMessage(-1);
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class MyHandler extends Handler {
        SoftReference<Context> contextSoftReference;

        public MyHandler(SoftReference<Context> contextSoftReference) {
            super();
            this.contextSoftReference = contextSoftReference;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(contextSoftReference.get(), "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(contextSoftReference.get(), "无法保存当前图", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != myHandler) {
            myHandler.removeCallbacksAndMessages(null);
            myHandler = null;
        }
        if (erCodeBitmap != null) {
            erCodeBitmap.recycle();
            erCodeBitmap = null;
        }
    }
}
