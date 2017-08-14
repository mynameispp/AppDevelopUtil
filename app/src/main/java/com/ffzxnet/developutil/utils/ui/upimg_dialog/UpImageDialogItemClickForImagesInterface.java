package com.ffzxnet.developutil.utils.ui.upimg_dialog;

import java.util.List;

/**
 * 创建者： feifan.pi 在 2017/7/10.
 */

public interface UpImageDialogItemClickForImagesInterface {
    void getImgPathFromDialog(List<String> path);
    //还可以最多选几张
    int getImgMax();
}
