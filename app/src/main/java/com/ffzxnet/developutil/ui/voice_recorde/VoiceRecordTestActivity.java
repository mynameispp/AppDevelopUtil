package com.ffzxnet.developutil.ui.voice_recorde;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.base.ui.CheckPermissionDialogCallBak;
import com.ffzxnet.developutil.ui.voice_recorde.code.EaseChatVoicePresenter;
import com.ffzxnet.developutil.ui.voice_recorde.code.EaseVoiceRecorderView;
import com.ffzxnet.developutil.ui.voice_recorde.code.RecorderInfo;
import com.ffzxnet.developutil.utils.tools.FileUtil;
import com.ffzxnet.developutil.utils.tools.GsonUtil;
import com.ffzxnet.developutil.utils.tools.LogUtil;
import com.ffzxnet.developutil.utils.tools.MMKVUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class VoiceRecordTestActivity extends BaseActivity implements VoiceRecordTestAdapter.AdapterListen {
    @BindView(R.id.voice_record_rv)
    RecyclerView voiceRecordRv;
    @BindView(R.id.voice_record_view)
    EaseVoiceRecorderView voiceRecordView;
    @BindView(R.id.voice_record_star_btn)
    Button voiceRecordStarBtn;
    @BindView(R.id.toolbar_right_tv)
    TextView toolbarRightTv1;

    private VoiceRecordTestAdapter adapter;
    //音频播放控件
    private EaseChatVoicePresenter voicePresenter;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_voice_record_test;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("", "录音");
        toolbarRightTv1.setText("全部删除");
        toolbarRightTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全部删除
                if (adapter != null && adapter.getItemCount() > 0) {
                    if (FileUtil.deleteFileInFolder(FileUtil.RecorderPath)) {
                        adapter.clearData();
                        MMKVUtil.getInstance().putString(MMKVUtil.Local_Audio_Record_List,"");
                    }
                }
            }
        });

        voicePresenter = new EaseChatVoicePresenter(this);
        voiceRecordRv.setLayoutManager(new LinearLayoutManager(this));
        String json = MMKVUtil.getInstance().getString(MMKVUtil.Local_Audio_Record_List, "");
        List<RecorderInfo> recorderInfo = null;
        if (!TextUtils.isEmpty(json)) {
            recorderInfo = GsonUtil.toClass(json, new TypeToken<List<RecorderInfo>>() {
            }.getType());
        }
        adapter = new VoiceRecordTestAdapter(recorderInfo, this);
        voiceRecordRv.setAdapter(adapter);


        //按住录音
        voiceRecordStarBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CheckPermissionDialog(new CheckPermissionDialogCallBak() {
                    @Override
                    public void hasPermission(boolean success) {
                        if (success) {
                            voiceRecordView.onPressToSpeakBtnTouch(voiceRecordStarBtn, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {
                                @Override
                                public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                                    LogUtil.e("录音结束", "时长=" + voiceTimeLength + "==路径=" + voiceFilePath);
                                    RecorderInfo recorderInfo = new RecorderInfo();
                                    recorderInfo.setVoiceTimeLength(voiceTimeLength);
                                    recorderInfo.setLocalUrl(voiceFilePath);
                                    adapter.add(recorderInfo);
                                    MMKVUtil.getInstance().putString(MMKVUtil.Local_Audio_Record_List, GsonUtil.toJson(adapter.getDatas()));
                                    if (((LinearLayoutManager) voiceRecordRv.getLayoutManager()).findLastVisibleItemPosition()
                                            != (adapter.getDatas().size() - 1)) {
                                        voiceRecordRv.smoothScrollToPosition(adapter.getDatas().size() - 1);
                                    }
                                }
                            });
                        }
                    }
                }, Manifest.permission.RECORD_AUDIO);
                return true;
            }
        });
    }

    @Override
    protected void onClickTitleBack() {
        goBackByQuick();
    }

    @Override
    public void itemVoiceClick(ImageView playAnimaImage, RecorderInfo itemData) {
        //点击播放
        RecorderInfo recorderInfo = new RecorderInfo();
        recorderInfo.setLocalUrl(itemData.getLocalUrl());
        recorderInfo.setOrginUrl(itemData.getOrginUrl());
        recorderInfo.setVoiceTimeLength(itemData.getVoiceTimeLength());
        playTopicVoice(playAnimaImage, recorderInfo);
    }

    //播放录音
    private void playTopicVoice(ImageView view, RecorderInfo recorderInfo) {
        voicePresenter.play(recorderInfo, view, new EaseChatVoicePresenter.RecoderInfoListen() {
            @Override
            public void onVoiceCompletion() {
                LogUtil.e("音频播放完成", "音频播放完成");
            }

            @Override
            public void getVoiceLength(int length) {
                LogUtil.e("音频时长", length + "秒");
            }

            @Override
            public void playError() {
                LogUtil.e("音频播放失败", "音频播放失败");
            }
        });
    }

}
