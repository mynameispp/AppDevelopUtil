package com.ffzxnet.developutil.ui.voice_recorde.code;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;

import java.io.IOException;

/**
 * @description 语音播放
 */
public class EaseChatRowVoicePlayer {
    private static final String TAG = "ConcurrentMediaPlayer";

    private static EaseChatRowVoicePlayer instance = null;

    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private String playingId;
    private boolean speakerOn = true;//扬声器播放语音

    private MediaPlayer.OnCompletionListener onCompletionListener;
    private EaseChatVoicePresenter.RecoderInfoListen recoderInfoListen;

    public static EaseChatRowVoicePlayer getInstance(Context context) {
        if (instance == null) {
            synchronized (EaseChatRowVoicePlayer.class) {
                if (instance == null) {
                    instance = new EaseChatRowVoicePlayer(context);
                }
            }
        }
        return instance;
    }

    public MediaPlayer getPlayer() {
        return mediaPlayer;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * 播放raw资源文件语音
     */
    public void playRaw(int resId) {
        if (mediaPlayer.isPlaying()) {
            stop();
        }
        try {
            setSpeaker();
            AssetFileDescriptor file = MyApplication.getContext().getResources().openRawResourceFd(resId);
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
                    file.getLength());
            mediaPlayer.prepare();
            file.close();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();
                    recoderInfoListen = null;
                    playingId = null;
                    onCompletionListener = null;
                }
            });
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 播放语音，没下载会自动下载
     *
     * @param recorderInfo 录音信息
     * @param listener
     */
    public void play(final RecorderInfo recorderInfo, final MediaPlayer.OnCompletionListener listener
            , EaseChatVoicePresenter.RecoderInfoListen recoderListen) {

        if (mediaPlayer.isPlaying()) {
            LogUtil.e("播放", "暂停1");
            stop();
        }

        onCompletionListener = listener;
        recoderInfoListen = recoderListen;

        try {
            setSpeaker();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(recorderInfo.getLocalUrl());
            mediaPlayer.prepare();
            if (null != recoderInfoListen) {
                recoderInfoListen.getVoiceLength(mediaPlayer.getDuration());
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();

//                    recoderInfoListen = null;
                    playingId = null;
                    onCompletionListener = null;
                }
            });
            mediaPlayer.start();
            LogUtil.e("播放", "播放");
        } catch (IOException e) {
            e.printStackTrace();
            onCompletionListener.onCompletion(mediaPlayer);
            if (null != recoderInfoListen) {
                recoderInfoListen.playError();
            }
//            BuglyCrashReportUtil.CrashReport("播放音频失败" + e.toString());
            ToastUtil.showToastLong("呀，网络出了问题");
        }
    }

    /**
     * 暂停播放
     */
    public void paus() {
        mediaPlayer.pause();
    }

    /**
     * 继续播放
     */
    public void resumPlay() {
        mediaPlayer.start();
    }


    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.reset();

        /**
         * This listener is to stop the voice play animation currently, considered the following 3 conditions:
         *
         * 1.A new voice item is clicked to play, to stop the previous playing voice item animation.
         * 2.The voice is play complete, to stop it's voice play animation.
         * 3.Press the voice record button will stop the voice play and must stop voice play animation.
         *
         */
        if (onCompletionListener != null) {
            onCompletionListener.onCompletion(mediaPlayer);
        }
        if (null != recoderInfoListen) {
            recoderInfoListen.onVoiceCompletion();
        }
    }

    private EaseChatRowVoicePlayer(Context cxt) {
        Context baseContext = cxt.getApplicationContext();
        audioManager = (AudioManager) baseContext.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
    }

    /**
     * 是否扬声器播放语音 默认开启
     *
     * @param speakerOn
     */
    public void setSpeakerOn(boolean speakerOn) {
        this.speakerOn = speakerOn;
    }

    private void setSpeaker() {
        if (speakerOn) {
            //扬声器播放
            audioManager.setSpeakerphoneOn(true);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
    }
}
