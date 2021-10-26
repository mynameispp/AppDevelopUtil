package com.ffzxnet.developutil.ui.album;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.ui.album.util.GlideEngine;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.rxjava3.functions.Consumer;

public class AlbumActivity extends BaseActivity {
    @BindView(R.id.album_show_image)
    ImageView albumShowImage;
    @BindView(R.id.album_select_single_image)
    Button albumSelectSingleImage;
    @BindView(R.id.album_select_multiple_image)
    Button albumSelectMultipleImage;
    @BindView(R.id.album_camera_image)
    Button albumCameraImage;

    private RxPermissions rxPermissions;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_album;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "相册");
        setToolBarBackground(R.color.white);
        rxPermissions = new RxPermissions(this);
        EasyPhotos.preLoad(this);

        GlideApp.with(albumShowImage)
                .load("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF")
                .into(albumShowImage);
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @OnClick({R.id.album_select_single_image, R.id.album_select_multiple_image, R.id.album_camera_image})
    public void onViewClicked(View view) {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Throwable {
                        if (aBoolean) {
                            switch (view.getId()) {
                                case R.id.album_select_single_image:
                                    takeSinglePicture(1);
                                    break;
                                case R.id.album_select_multiple_image:
                                    takeSinglePicture(9);
                                    break;
                                case R.id.album_camera_image:
                                    takePictureByCamera();
                                    break;
                            }
                        } else {
                            //没有权限
                            ToastUtil.showToastShort("该功能需要权限才能正常使用");
                        }
                    }
                });
    }

    private void takeSinglePicture(int i) {
        EasyPhotos.createAlbum(this, true, false, GlideEngine.getInstance())
                .setCount(i)
                .setMinFileSize(1024 * 10)
                .setMinWidth(500)
                .setMinHeight(500)
                .start(selectCallback);
    }

    private void takePictureByCamera() {
        EasyPhotos.createCamera(this, true)//参数说明：上下文,是否使用宽高数据（false时宽高数据为0，扫描速度更快）
                .setFileProviderAuthority("com.ffzxnet.developutil.fileprovider")
                .start(selectCallback);
    }

    private SelectCallback selectCallback = new SelectCallback() {
        @Override
        public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
            if (photos.size() > 0) {
                for (Photo photo : photos) {
                    LogUtil.e("图像信息：", photo.path);
                }
                GlideApp.with(albumShowImage)
                        .load(photos.get(0).path)
                        .into(albumShowImage);
            }
        }

        @Override
        public void onCancel() {

        }
    };
}
