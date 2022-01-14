package com.ffzxnet.developutil.ui.album;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.GlideApp;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.base.ui.CheckPermissionDialogCallBak;
import com.ffzxnet.developutil.ui.album.util.GlideEngine;
import com.ffzxnet.developutil.utils.tools.FileSizeUtil;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.constant.Type;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.mabeijianxi.smallvideorecord2.LocalMediaCompress;
import com.mabeijianxi.smallvideorecord2.model.AutoVBRMode;
import com.mabeijianxi.smallvideorecord2.model.LocalMediaConfig;
import com.mabeijianxi.smallvideorecord2.model.OnlyCompressOverBean;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class AlbumActivity extends BaseActivity {
    @BindView(R.id.album_show_image)
    ImageView albumShowImage;
    @BindView(R.id.album_select_single_image)
    Button albumSelectSingleImage;
    @BindView(R.id.album_select_multiple_image)
    Button albumSelectMultipleImage;
    @BindView(R.id.album_camera_image)
    Button albumCameraImage;
    @BindView(R.id.album_compress_image)
    ImageView albumCompressImage;
    @BindView(R.id.album_video_origin_size)
    TextView albumVideoOriginSize;
    @BindView(R.id.album_video_compress_size)
    TextView albumVideoCompressSize;


    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_album;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "相册");
        setToolBarBackground(R.color.white);
        EasyPhotos.preLoad(this);

        GlideApp.with(albumShowImage)
                .load("https://t7.baidu.com/it/u=3624649723,387536556&fm=193&f=GIF")
                .into(albumShowImage);
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @OnClick({R.id.album_select_single_image, R.id.album_select_multiple_image, R.id.album_camera_image, R.id.album_select_video
            , R.id.album_compress_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.album_select_single_image:
                CheckPermissionDialog(new CheckPermissionDialogCallBak() {
                    @Override
                    public void hasPermission(boolean success) {
                        if (success) {
                            takeSinglePicture(1);
                        }
                    }
                }, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case R.id.album_select_multiple_image:
                CheckPermissionDialog(new CheckPermissionDialogCallBak() {
                    @Override
                    public void hasPermission(boolean success) {
                        if (success) {
                            takeSinglePicture(9);
                        }
                    }
                }, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case R.id.album_camera_image:
                //拍照
                CheckPermissionDialog(new CheckPermissionDialogCallBak() {
                                          @Override
                                          public void hasPermission(boolean success) {
                                              if (success) {
                                                  takePictureByCamera();
                                              }
                                          }
                                      }, Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case R.id.album_select_video:
                //本地视频
                CheckPermissionDialog(new CheckPermissionDialogCallBak() {
                                          @Override
                                          public void hasPermission(boolean success) {
                                              if (success) {
                                                  takeLocalVideo();
                                              }
                                          }
                                      }
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case R.id.album_compress_image:
                String path = "压缩路径：" + view.getTag(R.id.tagId_1);
                ToastUtil.showToastLong(path);
                LogUtil.e("压缩路径", path);
                break;
        }
    }

    private void takeSinglePicture(int i) {
        EasyPhotos.createAlbum(this, false, false, GlideEngine.getInstance())
                .setCount(i)
//                .setMinFileSize(1024 * 10)//最小文件大小
                .setMinWidth(500)
                .setMinHeight(500)
                .setVideo(true)//显示视频
                .start(selectCallback);
    }

    private void takePictureByCamera() {
        EasyPhotos.createCamera(this, true)//参数说明：上下文,是否使用宽高数据（false时宽高数据为0，扫描速度更快）
                .setFileProviderAuthority("com.ffzxnet.developutil.fileprovider")
                .start(selectCallback);
    }

    private void takeLocalVideo() {
        EasyPhotos.createAlbum(this, false, false, GlideEngine.getInstance())//参数说明：上下文,是否使用宽高数据（false时宽高数据为0，扫描速度更快）
                .onlyVideo()
                .setCount(1)
                .start(selectCallback);
    }

    private SelectCallback selectCallback = new SelectCallback() {
        @Override
        public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
            if (photos.size() > 0) {
                for (Photo photo : photos) {
                    LogUtil.e("图像信息：", photo.path);
                }

                if (photos.get(0).type.contains(Type.VIDEO)) {
                    //视频
                    compress(photos.get(0).path);
                    GlideApp.with(albumShowImage)
                            .load(photos.get(0).path)
                            .into(albumShowImage);
                } else {
                    GlideApp.with(albumShowImage)
                            .load(photos.get(0).path)
                            .into(albumShowImage);
                }
            }
        }

        @Override
        public void onCancel() {

        }
    };

    private void compress(String filePath) {
        albumVideoOriginSize.setText(FileSizeUtil.getAutoFileOrFilesSize(filePath));
        LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
        final LocalMediaConfig config = buidler
                .setVideoPath(filePath)
                .captureThumbnailsTime(1)
                .doH264Compress(new AutoVBRMode())
                .setFramerate(15)
                .setScale(1.0f)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLoadingDialog(true, "压缩中...");
                    }
                });
                OnlyCompressOverBean onlyCompressOverBean = new LocalMediaCompress(config).startCompress();
                if (onlyCompressOverBean.isSucceed()) {
                    File video = new File(onlyCompressOverBean.getVideoPath());
                    LogUtil.e("ssssssssssssss", onlyCompressOverBean.getVideoPath() + "=======");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLoadingDialog(false);
                            GlideApp.with(albumCompressImage)
                                    .load(onlyCompressOverBean.getPicPath())
                                    .into(albumCompressImage);
                            albumCompressImage.setTag(R.id.tagId_1, onlyCompressOverBean.getVideoPath());
                            albumVideoCompressSize.setText(FileSizeUtil.getAutoFileOrFilesSize(video.getAbsolutePath()));
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLoadingDialog(false);
                            ToastUtil.showToastShort("压缩失败");
                        }
                    });
                }

            }
        }).start();
    }
}
