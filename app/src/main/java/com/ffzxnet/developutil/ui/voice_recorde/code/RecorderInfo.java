package com.ffzxnet.developutil.ui.voice_recorde.code;

public class RecorderInfo {
    //时长
    private int voiceTimeLength;
    //远程录音地址
    private String orginUrl;
    //本地下载好的录音地址
    private String LocalUrl;

    public int getVoiceTimeLength() {
        return voiceTimeLength;
    }

    public void setVoiceTimeLength(int voiceTimeLength) {
        this.voiceTimeLength = voiceTimeLength;
    }

    public String getOrginUrl() {
        return orginUrl;
    }

    public void setOrginUrl(String orginUrl) {
        this.orginUrl = orginUrl;
    }

    public String getLocalUrl() {
        return LocalUrl;
    }

    public void setLocalUrl(String localUrl) {
        LocalUrl = localUrl;
    }
}
