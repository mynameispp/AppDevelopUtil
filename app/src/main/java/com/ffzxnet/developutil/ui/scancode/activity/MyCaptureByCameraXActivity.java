package com.ffzxnet.developutil.ui.scancode.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.base.ui.BaseActivityResultContact;
import com.ffzxnet.developutil.base.ui.CheckPermissionDialogCallBak;
import com.ffzxnet.developutil.ui.scancode.camera.PlanarYUVLuminanceSource;
import com.ffzxnet.developutil.ui.scancode.decoding.RGBLuminanceSource;
import com.ffzxnet.developutil.ui.scancode.tools.EaseFileUtils;
import com.ffzxnet.developutil.ui.scancode.tools.VersionUtils;
import com.ffzxnet.developutil.ui.scancode.view.ViewfinderView;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyCaptureByCameraXActivity extends BaseActivity {
    private MediaPlayer mediaPlayer;
    private boolean playBeep = true;
    private static final float BEEP_VOLUME = 0.1F;
    private boolean vibrate = true;
    private Bitmap scanBitmap;
    public static final int RESULT_CODE_QR_SCAN = 161;
    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";
    private static final long VIBRATE_DURATION = 100L;

    private Camera mCamera;
    private ImageCapture mImageCapture;
    private ImageAnalysis mImageAnalysis;
    private VideoCapture mVideoCapture;
    private ProcessCameraProvider mCameraProvider;
    private Preview mPreview;
    private MultiFormatReader multiFormatReader = new MultiFormatReader();

    private ImageView deviceLightSwitch;
    private TextView scanner_toolbar_album;
    private PreviewView previewView;
    public ViewfinderView viewfinderView;
    private boolean openOrCloseFlashLight = false;
    private boolean isAnalyzing;
    private Disposable disposable;//正在识别
    private boolean isBackCamera = true;//默认打开后置摄像头

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_scancode_by_camerax;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        redirectActivityForResult(new BaseActivityResultContact(), new ActivityResultCallback() {
            @Override
            public void onActivityResult(Object result) {
                if (null != result) {
                    onActivityResultForLocalPhotos((Intent) result);
                }
            }
        });

        initView();
    }

    private void initView() {
        //本地相册
        scanner_toolbar_album = (TextView) findViewById(R.id.scanner_toolbar_album);
        scanner_toolbar_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //本地图片，打开本地相册，建议扫码是让用户裁剪二维码
                CheckPermissionDialog(new CheckPermissionDialogCallBak() {
                    @Override
                    public void hasPermission(boolean success) {
                        if (success) {
                            Intent intent = null;
                            if (VersionUtils.isTargetQ(view.getContext())) {
                                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            } else {
                                intent = new Intent(Intent.ACTION_GET_CONTENT);
                            }
                            intent.setType("image/*");
                            resultLauncher.launch(intent);
                        }
                    }
                }, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });
        //打开或关闭闪光灯
        deviceLightSwitch = findViewById(R.id.scan_code_device_light);
        deviceLightSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean open = OpenFlashLight();
                if (open) {
                    deviceLightSwitch.setImageResource(R.mipmap.icon_device_light_on);
                } else {
                    deviceLightSwitch.setImageResource(R.mipmap.icon_device_light_off);
                }
            }
        });

        findViewById(R.id.scanner_toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackByQuick();
            }
        });
        viewfinderView = findViewById(R.id.viewfinder_content_camerax);
        viewfinderView.drawViewfinder();
        previewView = findViewById(R.id.scanner_view);
        initBeepSound();
        setupCamera(previewView);
    }

    //设置摄像头
    private void setupCamera(final PreviewView previewView) {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    mCameraProvider = cameraProviderFuture.get();
                    bindPreview(mCameraProvider, previewView);
                    onAnalyzeGo();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onDestroy() {
        closeFlashLight();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public boolean OpenFlashLight() {
        openOrCloseFlashLight = !openOrCloseFlashLight;
        mCamera.getCameraControl().enableTorch(openOrCloseFlashLight);
        return openOrCloseFlashLight;
    }

    public void closeFlashLight() {
        mCamera.getCameraControl().enableTorch(false);
    }

    /**
     * 传入本地二维码图片路径
     *
     * @param path
     */
    public void scanAlbunUrl(final String path) {
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "图片路径错误", Toast.LENGTH_SHORT).show();
            return;
        }
        (new Thread(new Runnable() {
            public void run() {
                Result result = MyCaptureByCameraXActivity.this.scanningImage(path);
                if (result != null) {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(INTENT_EXTRA_KEY_QR_SCAN, result.getText());
                    resultIntent.putExtras(bundle);
                    MyCaptureByCameraXActivity.this.setResult(RESULT_CODE_QR_SCAN, resultIntent);
                    finish();
                } else {
                    ToastUtil.showToastLong("识别失败，请试试单独编辑图片，剪切出要扫描的二维码图");
                }
            }
        })).start();
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
                LogUtil.e("图片路径", filePath);
                if (!TextUtils.isEmpty(filePath) && new File(filePath).exists()) {
                    scanAlbunUrl(filePath);
                } else {
                    Toast.makeText(this, "扫描失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path))
            return null;
        Hashtable<DecodeHintType, Object> HINTS = new Hashtable<>();
        List<BarcodeFormat> allFormats = new ArrayList<>();
        allFormats.add(BarcodeFormat.AZTEC);
        allFormats.add(BarcodeFormat.CODABAR);
        allFormats.add(BarcodeFormat.CODE_39);
        allFormats.add(BarcodeFormat.CODE_93);
        allFormats.add(BarcodeFormat.CODE_128);
        allFormats.add(BarcodeFormat.DATA_MATRIX);
        allFormats.add(BarcodeFormat.EAN_8);
        allFormats.add(BarcodeFormat.EAN_13);
        allFormats.add(BarcodeFormat.ITF);
        allFormats.add(BarcodeFormat.MAXICODE);
        allFormats.add(BarcodeFormat.PDF_417);
        allFormats.add(BarcodeFormat.QR_CODE);
        allFormats.add(BarcodeFormat.RSS_14);
        allFormats.add(BarcodeFormat.RSS_EXPANDED);
        allFormats.add(BarcodeFormat.UPC_A);
        allFormats.add(BarcodeFormat.UPC_E);
        allFormats.add(BarcodeFormat.UPC_EAN_EXTENSION);
        HINTS.put(DecodeHintType.TRY_HARDER, BarcodeFormat.QR_CODE);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, allFormats);
        HINTS.put(DecodeHintType.CHARACTER_SET, "utf-8");
//        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        this.scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        int sampleSize = (int) (options.outHeight / 200.0F);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        this.scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(this.scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap((Binarizer) new HybridBinarizer((LuminanceSource) source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, HINTS);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initBeepSound() {
        if (this.playBeep && this.mediaPlayer == null) {
            setVolumeControlStream(3);
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setAudioStreamType(3);
            this.mediaPlayer.setOnCompletionListener(this.beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                this.mediaPlayer.setDataSource(file.getFileDescriptor(), file
                        .getStartOffset(), file.getLength());
                file.close();
                this.mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                this.mediaPlayer.prepare();
            } catch (IOException e) {
                this.mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (this.playBeep && this.mediaPlayer != null)
            this.mediaPlayer.start();
        if (this.vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    //手动切换摄像头
    public void onChangeGo() {
        if (mCameraProvider != null) {
            isBackCamera = !isBackCamera;
            bindPreview(mCameraProvider, previewView);
            if (mImageAnalysis != null) {
                mImageAnalysis.clearAnalyzer();
            }
        }
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider,
                             PreviewView previewView) {
        bindPreview(cameraProvider, previewView, false);
    }

    //切换前后摄像头
    @SuppressLint("RestrictedApi")
    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider,
                             PreviewView previewView, boolean isVideo) {
        Preview.Builder previewBuilder = new Preview.Builder();
        ImageCapture.Builder captureBuilder = new ImageCapture.Builder()
                .setTargetRotation(previewView.getDisplay().getRotation());
        CameraSelector cameraSelector = isBackCamera ? CameraSelector.DEFAULT_BACK_CAMERA
                : CameraSelector.DEFAULT_FRONT_CAMERA;

        mImageAnalysis = new ImageAnalysis.Builder()
                .setTargetRotation(previewView.getDisplay().getRotation())
                .setTargetResolution(new Size(720, 1080))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        mVideoCapture = new VideoCapture.Builder()
                .setTargetRotation(previewView.getDisplay().getRotation())
                .setVideoFrameRate(25)
                .setBitRate(3 * 1024 * 1024)
                .build();

        mPreview = previewBuilder.build();

        mImageCapture = captureBuilder.build();

        cameraProvider.unbindAll();
        if (isVideo) {
            mCamera = cameraProvider.bindToLifecycle(this, cameraSelector,
                    mPreview, mVideoCapture);
        } else {
            mCamera = cameraProvider.bindToLifecycle(this, cameraSelector,
                    mPreview, mImageCapture, mImageAnalysis);
        }
        mPreview.setSurfaceProvider(previewView.getSurfaceProvider());
    }

    //开启扫描
    @SuppressLint("RestrictedApi")
    public void onAnalyzeGo() {
        if (mImageAnalysis == null) {
            return;
        }
        if (!isAnalyzing) {
            LogUtil.e("扫码", "开启监听setAnalyzer()");
            mImageAnalysis.setAnalyzer(CameraXExecutors.mainThreadExecutor(), new ImageAnalysis.Analyzer() {
                @Override
                public void analyze(@NonNull ImageProxy image) {
                    LogUtil.e("扫码", "开始解析");
                    //这里开启线程，否则扫描线动画卡顿
                    if (disposable == null) {
                        Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Throwable {
                                String result = analyzeQRCode(image);
                                if (!TextUtils.isEmpty(result)) {
                                    LogUtil.e("ppppppppp", "scan result subscribe= " + result);
                                    emitter.onNext(result);
                                } else if (disposable != null) {
                                    disposable.dispose();
                                    disposable = null;
                                }
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<String>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        disposable = d;
                                    }

                                    @Override
                                    public void onNext(String resultString) {
                                        playBeepSoundAndVibrate();
                                        Intent resultIntent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putString(INTENT_EXTRA_KEY_QR_SCAN, resultString);
                                        LogUtil.e("ppppppppp", "scan result onNext= " + resultString);
                                        resultIntent.putExtras(bundle);
                                        setResult(RESULT_CODE_QR_SCAN, resultIntent);
                                        goBackByQuick();
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }
            });

        } else {
            LogUtil.e("扫码", "clearAnalyzer()");
            mImageAnalysis.clearAnalyzer();
        }
        isAnalyzing = !isAnalyzing;
    }

    //解析二维码
    private String analyzeQRCode(@NonNull ImageProxy imageProxy) {
        ByteBuffer byteBuffer = imageProxy.getPlanes()[0].getBuffer();
        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data);

        int width = imageProxy.getWidth(), height = imageProxy.getHeight();
        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                data, width, height, 0, 0, width, height);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = null;
        try {
            result = multiFormatReader.decode(bitmap);
            LogUtil.e("扫码成功", "result:" + result);
            imageProxy.close();
            return result.getText();
        } catch (Exception e) {
            LogUtil.e("扫码", "Error decoding barcode==" + e.getMessage());
            imageProxy.close();
        } finally {
            multiFormatReader.reset();
        }
        return "";
    }
}
