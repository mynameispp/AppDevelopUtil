package com.ffzxnet.developutil.ui.voice_recorde.code;

import android.content.Context;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;

import com.ffzxnet.developutil.utils.tools.FileUtil;
import com.ffzxnet.developutil.utils.tools.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class EaseVoiceRecorder {
    MediaRecorder recorder;

    static final String PREFIX = "voice";
    static final String EXTENSION = ".wav";

    private boolean isRecording = false;
    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File file;
    private Handler handler;

    public EaseVoiceRecorder(Handler handler) {
        this.handler = handler;
    }

    /**
     * start recording to the file
     */
    public String startRecording(Context appContext) {
        file = null;
        try {
            // need to create recorder every time, otherwise, will got exception
            // from setOutputFile when try to reuse
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
//            recorder = new MediaRecorder();
//            CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
//            recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//            recorder.setOutputFormat(camcorderProfile.fileFormat);
//            recorder.setAudioSamplingRate(16000);//(科大讯飞语音评测只支持16000)
//            recorder.setAudioEncodingBitRate(16);
//            recorder.setAudioEncoder(camcorderProfile.audioSampleRate);
//            recorder.setAudioChannels(1);

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioChannels(1); // MONO
            recorder.setAudioSamplingRate(8000); // 8000Hz
            recorder.setAudioEncodingBitRate(64); // seems if change this to
            // 128, still got same file
            // size.
            // one easy way is to use temp file
            // file = File.createTempFile(PREFIX + userId, EXTENSION,
            // User.getVoicePath());
            voiceFileName = getVoiceFileName(SystemClock.currentThreadTimeMillis() + "");
            voiceFilePath = FileUtil.RecorderPath+ voiceFileName;
            file = new File(voiceFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            isRecording = true;
            recorder.start();
        } catch (IOException e) {
            LogUtil.e("voice", "prepare() failed");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRecording) {
                        android.os.Message msg = new android.os.Message();
                        msg.what = recorder.getMaxAmplitude() * 13 / 0x7FFF;
                        handler.sendMessage(msg);
                        SystemClock.sleep(100);
                    }
                } catch (Exception e) {
                    // from the crash report website, found one NPE crash from
                    // one android 4.0.4 htc phone
                    // maybe handler is null for some reason
                    LogUtil.e("voice", e.toString());
                }
            }
        }).start();
        startTime = new Date().getTime();
        LogUtil.e("voice", "start voice recording to file:" + file.getAbsolutePath());
        return file == null ? null : file.getAbsolutePath();
    }

    /**
     * stop the recoding
     *
     * @return seconds of the voice recorded
     */

    public void discardRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
                if (file != null && file.exists() && !file.isDirectory()) {
                    file.delete();
                }
            } catch (IllegalStateException e) {
            } catch (RuntimeException e) {
            }
            isRecording = false;
        }
    }

    public int stopRecoding() {
        if (recorder != null) {
            isRecording = false;
            try {
                recorder.stop();
            } catch (IllegalStateException e) {
                // TODO 如果当前java状态和jni里面的状态不一致，
                //e.printStackTrace();
                recorder = null;
                recorder = new MediaRecorder();
                recorder.stop();
            }
            recorder.release();
            recorder = null;

            if (file == null || !file.exists() || !file.isFile()) {
                return RecorderError.FILE_INVALID;
            }
            if (file.length() == 0) {
                file.delete();
                return RecorderError.FILE_INVALID;
            }
            int seconds = (int) (new Date().getTime() - startTime) / 1000;
            LogUtil.e("voice", "voice recording finished. seconds:" + seconds + " file length:" + file.length());
            return seconds;
        }
        return 0;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (recorder != null) {
            recorder.release();
        }
    }

    private String getVoiceFileName(String uid) {
        Time now = new Time();
        now.setToNow();
        return uid + now.toString().substring(0, 15) + EXTENSION;
    }

    public boolean isRecording() {
        return isRecording;
    }


    public String getVoiceFilePath() {
        return voiceFilePath;
    }

    public String getVoiceFileName() {
        return voiceFileName;
    }
}
