package com.ffzxnet.developutil.ui.guide.code.model;

import android.graphics.RectF;
import android.view.View;

import androidx.annotation.Nullable;

public interface HighLight {

    Shape getShape();

    /**
     * @param view anchor view
     * @return highlight's rectF
     */
    RectF getRectF(View view);

    /**
     * 当shape为CIRCLE时调用此方法获取半径
     */
    float getRadius();

    /**
     * 获取圆角，仅当shape = Shape.ROUND_RECTANGLE才调用次方法
     */
    int getRound();

    /**
     * 额外参数
     */
    @Nullable
    HighlightOptions getOptions();

    public enum Shape {
        CIRCLE,//圆形
        RECTANGLE, //矩形
        OVAL,//椭圆
        ROUND_RECTANGLE;//圆角矩形
    }
}
