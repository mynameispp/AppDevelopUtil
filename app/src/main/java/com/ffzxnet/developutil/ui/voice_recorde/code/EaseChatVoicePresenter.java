package com.ffzxnet.developutil.ui.voice_recorde.code;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.net.ApiImp;
import com.ffzxnet.developutil.net.ErrorResponse;
import com.ffzxnet.developutil.net.IApiSubscriberCallBack;
import com.ffzxnet.developutil.utils.tools.DownLoadFileUtil;
import com.ffzxnet.developutil.utils.tools.FileUtil;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.ffzxnet.developutil.utils.ui.LoadingUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import java.io.File;

import okhttp3.ResponseBody;

public class EaseChatVoicePresenter {
    private static final String TAG = "EaseChatVoicePresenter";

    private EaseChatRowVoicePlayer voicePlayer;
    private AnimationDrawable voiceAnimation;
    private boolean isPaus;


    public EaseChatVoicePresenter(Context cxt) {
        voicePlayer = EaseChatRowVoicePlayer.getInstance(cxt);
    }

    public interface RecoderInfoListen {
        void onVoiceCompletion();

        /**
         * 获取时长
         *
         * @return
         */
        void getVoiceLength(int length);

        /**
         * 播放音频失败
         */
        void playError();
    }


    public void onDetachedFromWindow() {
        if (voicePlayer.isPlaying() || isPaus) {
            LogUtil.e("播放", "暂停2");
            voicePlayer.stop();
        }
    }


    /**
     * 暂停和继续播放
     */
    public void pausOrResumePlayVoice() {
        if (voicePlayer.isPlaying()) {
            voicePlayer.paus();
            isPaus = true;
        } else {
            voicePlayer.resumPlay();
            isPaus = false;
        }
    }


    /**
     * 下载录音
     *
     * @param recorderInfo
     * @param itemHuanxinImVoice
     */
    private void asyncDownloadVoice(final RecorderInfo recorderInfo, final ImageView itemHuanxinImVoice
            , final RecoderInfoListen recoderInfoListen) {
        LoadingUtil.showLoadingDialog(true, "加载音频中...");
        ApiImp.getInstance().downloadFile(recorderInfo.getOrginUrl(), new IApiSubscriberCallBack<ResponseBody>() {
            @Override
            public void onCompleted() {
                LoadingUtil.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort("题干音频下载失败");
                Throwable throwable = new Exception("题干音频下载失败 = " + recorderInfo.getOrginUrl());
//                CrashReport.postCatchedException(throwable);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String filePath = FileUtil.DownLoadFilePath;
                String orginName = recorderInfo.getOrginUrl();
                String fileName = orginName.substring(orginName.lastIndexOf("/") + 1);
                if (DownLoadFileUtil.writeResponseBodyToDisk(responseBody, filePath, fileName)) {
                    //设置本地路径
                    recorderInfo.setLocalUrl(filePath + fileName);
                    play(recorderInfo, itemHuanxinImVoice, recoderInfoListen);
                }
            }
        });
    }

    public void play(RecorderInfo recorderInfo, ImageView itemHuanxinImVoice, RecoderInfoListen recoderInfoListen) {
        if (TextUtils.isEmpty(recorderInfo.getOrginUrl()) && TextUtils.isEmpty(recorderInfo.getLocalUrl())) {
            ToastUtil.showToastShort("音频地址为空哦");
            return;
        }
        String localPath;
        if (TextUtils.isEmpty(recorderInfo.getLocalUrl())) {
            //远程音频下载到本地
            String filePath = FileUtil.RecorderPath;
            String orginName = recorderInfo.getOrginUrl();
            String fileName = orginName.substring(orginName.lastIndexOf("/") + 1);
            localPath = filePath + fileName;
        } else {
            localPath = recorderInfo.getLocalUrl();
        }
        recorderInfo.setLocalUrl(localPath);
        File file = new File(localPath);
        if (file.exists() && file.isFile()) {
            // Start the voice play animation.
            playVoice(recorderInfo, itemHuanxinImVoice, recoderInfoListen);
            startVoicePlayAnimation(recorderInfo, itemHuanxinImVoice);
        } else {
            LogUtil.e(TAG, "file not exist");
            //下载
            asyncDownloadVoice(recorderInfo, itemHuanxinImVoice, recoderInfoListen);
        }
    }

    /**
     * 播放raw资源文件语音
     */
    public void playRaw(int resId) {
        voicePlayer.playRaw(resId);
    }

    /**
     * 开始播放
     *
     * @param recorderInfo
     * @param itemHuanxinImVoice
     */
    private void playVoice(final RecorderInfo recorderInfo, final ImageView itemHuanxinImVoice, RecoderInfoListen recoderInfoListen) {
        voicePlayer.play(recorderInfo, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Stop the voice play animation.
                stopVoicePlayAnimation(itemHuanxinImVoice);
            }
        }, recoderInfoListen);
    }

    /**
     * 开始播放动画
     *
     * @param recorderInfo
     * @param itemHuanxinImVoice
     */
    public void startVoicePlayAnimation(RecorderInfo recorderInfo, ImageView itemHuanxinImVoice) {
        if (null == itemHuanxinImVoice) {
            return;
        }
        int animaResId = (int) itemHuanxinImVoice.getTag(R.id.tagId_2);
        if (animaResId > 0) {
            itemHuanxinImVoice.setImageResource(animaResId);
            try {
                voiceAnimation = (AnimationDrawable) itemHuanxinImVoice.getDrawable();
                if (null != voiceAnimation) {
                    voiceAnimation.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
                //普通图
                itemHuanxinImVoice.setImageResource(animaResId);
            }
        }
    }

    /**
     * 停止播放
     *
     * @param itemHuanxinImVoice
     */
    public void stopVoicePlayAnimation(ImageView itemHuanxinImVoice) {
        if (null == itemHuanxinImVoice) {
            return;
        }
        if (voiceAnimation != null) {
            voiceAnimation.stop();
        }
        itemHuanxinImVoice.setImageResource((Integer) itemHuanxinImVoice.getTag(R.id.tagId_1));
    }
}
