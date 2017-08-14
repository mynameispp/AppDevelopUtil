package com.ffzxnet.developutil.utils.ui.upimg_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 创建者： feifan.pi 在 2017/6/22.
 */

public class UpImageDialog extends Dialog implements View.OnClickListener {
    //单选数据监听
    private UpImageDialogItemClickInterface singImageInteface;
    //多选数据监听
    private UpImageDialogItemClickForImagesInterface forImagesInterface;

    /**
     * 选择单图
     * @param context
     * @param singImageInteface
     */
    public UpImageDialog(@NonNull Context context, @NonNull UpImageDialogItemClickInterface singImageInteface) {
        this(context, R.style.DialogStyle);
        this.singImageInteface = singImageInteface;
    }

    /**
     * 选择多图
     * @param context
     * @param forImagesInterface
     */
    public UpImageDialog(@NonNull Context context, @NonNull UpImageDialogItemClickForImagesInterface forImagesInterface) {
        this(context, R.style.DialogStyle);
        this.forImagesInterface = forImagesInterface;
    }

    public UpImageDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected UpImageDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_up_image);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setWindowAnimations(R.style.DialogAnimation);
        dialogWindow.setGravity(Gravity.BOTTOM);
//        ViewGroup.LayoutParams params = findViewById(R.id.dialog_up_img_lay).getLayoutParams();
        lp.width = MyApplication.Screen_Width; // 宽度
//        lp.height = params.height; // 高度
        // lp.alpha = 0.7f; // 透明度
        dialogWindow.setAttributes(lp);

        initView();
    }

    /**
     * 初始化数据
     */
    private void initView() {
//        ((TextView) findViewById(R.id.include_dialog_title_name)).setText("请选择图片");
//        findViewById(R.id.include_dialog_title_cancle).setVisibility(View.INVISIBLE);
//        findViewById(R.id.include_dialog_title_ok).setVisibility(View.INVISIBLE);
        //设置点击事件
        findViewById(R.id.dialog_up_img_Album).setOnClickListener(this);
        findViewById(R.id.dialog_up_img_camera).setOnClickListener(this);
        findViewById(R.id.dialog_up_img_cancle).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_up_img_Album:
                //打开相册
                if (null != singImageInteface) {
                    GalleryFinal.openGallerySingle(1000, new GalleryFinal.OnHanlderResultCallback() {
                        @Override
                        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                            singImageInteface.getImgPathFromDialog(resultList.get(0).getPhotoPath());
                        }


                        @Override
                        public void onHanlderFailure(int requestCode, String errorMsg) {
                            ToastUtil.showToastShort(errorMsg);
                        }

                    });
                } else if (null != forImagesInterface) {
                    GalleryFinal.openGalleryMuti(1000, forImagesInterface.getImgMax(), new GalleryFinal.OnHanlderResultCallback() {
                        @Override
                        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                            final List<String> paths = new ArrayList<String>();
                            for (PhotoInfo photoInfo : resultList) {
                                paths.add(photoInfo.getPhotoPath());
                            }
                            forImagesInterface.getImgPathFromDialog(paths);
                        }

                        @Override
                        public void onHanlderFailure(int requestCode, String errorMsg) {
                            ToastUtil.showToastShort(errorMsg);
                        }
                    });
                }
                break;
            case R.id.dialog_up_img_camera:
                //打开照相机
                GalleryFinal.openCamera(1002, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        singImageInteface.getImgPathFromDialog(resultList.get(0).getPhotoPath());
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                        Toast.makeText(MyApplication.getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.dialog_up_img_cancle:
                break;
        }
        dismiss();
    }
}
