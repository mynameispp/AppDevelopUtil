package com.ffzxnet.developutil.ui.video_download.utils;

import android.text.TextUtils;

import com.ffzxnet.developutil.ui.video_download.bean.DownloadVideoInfoBean;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.ffzxnet.developutil.utils.video_download.VideoDownloadManager;
import com.ffzxnet.developutil.utils.video_download.model.VideoTaskState;
import com.ffzxnet.developutil.utils.video_download.utils.KeyUtils;
import com.ffzxnet.developutil.utils.video_download.utils.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.jeffmony.m3u8library.VideoProcessManager;
import com.jeffmony.m3u8library.listener.IVideoTransformListener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DownLoadUtil {
    public static void saveDownLoading(List<DownloadVideoInfoBean> saveData) {
        if (saveData != null) {
            SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.SP_DownLoading_List
                    , GsonUtil.toJson(saveData));
        }
    }

    public static List<DownloadVideoInfoBean> getDownLoading() {
        List<DownloadVideoInfoBean> downLoadingData = null;
        String downLoadingJson = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.SP_DownLoading_List, "");
        if (!TextUtils.isEmpty(downLoadingJson)) {
            downLoadingData = GsonUtil.toClass(downLoadingJson, new TypeToken<List<DownloadVideoInfoBean>>() {
            }.getType());
        }
        if (downLoadingData == null) {
            downLoadingData = new ArrayList<>();
        }
        return downLoadingData;
    }

    public static synchronized void saveDownLoadOver(List<DownloadVideoInfoBean> saveData) {
        if (saveData != null) {
            SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.SP_DownLoadOver_List
                    , GsonUtil.toJson(saveData));
        }
    }

    public static List<DownloadVideoInfoBean> getDownLoadOver() {
        List<DownloadVideoInfoBean> downLoadOverVideos = null;
        String downLoadOverJson = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.SP_DownLoadOver_List, "");
        if (!TextUtils.isEmpty(downLoadOverJson)) {
            downLoadOverVideos = GsonUtil.toClass(downLoadOverJson, new TypeToken<List<DownloadVideoInfoBean>>() {
            }.getType());
        }
        if (downLoadOverVideos == null) {
            downLoadOverVideos = new ArrayList<>();
        }
        return downLoadOverVideos;
    }

    public static void stopAllDownLoading() {
        String downLoadingJson = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.SP_DownLoading_List, "");
        if (!TextUtils.isEmpty(downLoadingJson)) {
            List<DownloadVideoInfoBean> downLoadingVideos = GsonUtil.toClass(downLoadingJson, new TypeToken<List<DownloadVideoInfoBean>>() {
            }.getType());
            if (downLoadingVideos != null && downLoadingVideos.size() > 0) {
                VideoDownloadManager.getInstance().pauseAllDownloadTasks();
                for (DownloadVideoInfoBean downLoadingVideo : downLoadingVideos) {
                    if (downLoadingVideo.getDownLoadUrl().getTaskState() != VideoTaskState.DEFAULT) {
                        downLoadingVideo.getDownLoadUrl().setTaskState(VideoTaskState.PAUSE);
                    }
                }
                saveDownLoading(downLoadingVideos);
            }
        }
    }

    public static void starPlayM3u8ToMp4(DownloadVideoInfoBean data, TransformM3U8ToMp4Listen listen) {
        String locaPath = data.getDownLoadUrl().getFilePath();
        File file = new File(locaPath);
        if (locaPath.toLowerCase().contains(".mp4") && file.exists()) {
            listen.onTransformProgressFinsh(data);
        } else {
            listen.onTransformProgressing("正在转换视频中");
            KeyUtils.checkKey(data.getDownLoadUrl(), new KeyUtils.CheckKeyListen() {
                @Override
                public void checkStatus(boolean success) {
                    if (success) {
                        String locaPath = data.getDownLoadUrl().getFilePath();
                        if (TextUtils.isEmpty(locaPath)) {
                            locaPath = data.getDownLoadUrl().getSaveDir() + "/" + data.getDownLoadUrl().getFileHash() + "_local." + data.getDownLoadUrl().getMimeType();
                        }
                        File file = new File(locaPath);
                        LogUtil.e("是否存在===", file.exists() + "==");
                        if (locaPath.contains(".m3u8")) {
                            if (!file.exists()) {
                                ToastUtil.showToastShort("文件损坏请删除重新下载");
                            } else {
                                tranM3u8(data, locaPath, listen);
                            }
                        } else {
                            if (!file.exists()) {
                                locaPath = data.getDownLoadUrl().getSaveDir() + "/" + data.getDownLoadUrl().getFileHash() + "_local." + data.getDownLoadUrl().getMimeType();
                                tranM3u8(data, locaPath, listen);
                            } else {
                                listen.onTransformProgressFinsh(data);
                            }
                        }
                    } else {
                        ToastUtil.showToastShort("视频验证失败请重新下载");
                    }
                }
            });
        }
    }

    public interface TransformM3U8ToMp4Listen {
        void onTransformProgressing(String progress);

        void onTransformProgressFinsh(DownloadVideoInfoBean video);

        void onTransformProgressError(String msg);
    }

    public static void tranM3u8(DownloadVideoInfoBean data, String locaPath, TransformM3U8ToMp4Listen listen) {
        String mp4 = data.getDownLoadUrl().getSaveDir() + "/" + data.getDownLoadUrl().getFileHash() + "_local.mp4";
        File mp4file = new File(mp4);
        if (!mp4file.exists()) {
            try {
                mp4file.createNewFile();
            } catch (Exception e) {
                LogUtils.e("dddddddddddddd", "Create file failed, exception = " + e);
                return;
            }
        }
        VideoProcessManager.getInstance().transformM3U8ToMp4(locaPath, mp4file.getAbsolutePath(), new IVideoTransformListener() {

            @Override
            public void onTransformProgress(float progress) {
                DecimalFormat format = new DecimalFormat(".00");
                listen.onTransformProgressing("转换的进度: " + format.format(progress) + "%");
            }

            @Override
            public void onTransformFinished() {
                data.getDownLoadUrl().setFilePath(mp4);
                listen.onTransformProgressFinsh(data);
                //转换成功后播放
//                DownLoadUtil.saveDownLoadOver(adapter.getDatas());
//                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
//                intent.putExtra("VIDEO_ID", data.getVideoId());
//                intent.putExtra("VIDEO_IMG", data.getVideoImage()); //封面图
//                intent.putExtra("VIDEO_NAME", data.getVideoName());
//
//                intent.putExtra("Local_VIDEO_Url", "file://" + mp4);
//                intent.putExtra("Local_VIDEO_Name", data.getDownLoadUrl().getTitle());
//                startActivity(intent);
            }

            @Override
            public void onTransformFailed(Exception e) {
                listen.onTransformProgressError("转换失败: " + e.getMessage());
            }
        });
    }
}
