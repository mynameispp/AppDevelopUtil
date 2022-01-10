package com.ffzxnet.developutil.utils.tools;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ffzxnet.developutil.application.MyApplication;


/**
 * @Package: com.feifanzhixing.o2ozhiye.utils
 * @ClassName: SetTextViewDrawable.java
 * @Description:TODO
 * @author: pifeifan
 * @date: 2015-8-19
 * <p>
 * Copyright @ 2015 51ZhiYe
 */
public class SetTextViewDrawable {
    /**
     * 设置Title TextView图片 drawableTop
     *
     * @param textView
     * @param drawableId
     */
    public static void setTopView(TextView textView, int drawableId) {
        Drawable drawable = MyApplication.getDrawableByResId(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    /**
     * 设置Title TextView图片 drawableBottom
     *
     * @param textView
     * @param drawableId
     */
    public static void setBottomView(TextView textView, int drawableId) {
        Drawable drawable = MyApplication.getDrawableByResId(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, null, drawable);
    }

    /**
     * 设置Title TextView图片 drawableLeft
     *
     * @param textView
     * @param drawableId
     */
    public static void setLeftView(TextView textView, int drawableId) {
        Drawable drawable = MyApplication.getDrawableByResId(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置Title TextView图片 drawableLeft
     *
     * @param textView
     * @param drawableId
     * @param TintColor
     */
    public static void setLeftView(final TextView textView, int drawableId,int TintColor) {
        Drawable drawable = MyApplication.getDrawableByResId(drawableId);
        drawable.setTint(TintColor);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置Title TextView图片 drawableRight
     *
     * @param textView
     * @param drawableId
     */
    public static void setRightView(TextView textView, int drawableId) {
        if (drawableId != 0) {
            Drawable drawable = MyApplication.getDrawableByResId(drawableId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, null, drawable, null);
        } else {
            //隐藏图片
            textView.setCompoundDrawables(null, null, null, null);
        }
    }

    /**
     * 取消Title 所有drawable
     */
    public static void claearView(final View view) {
        if (view instanceof TextView) {
            ((TextView) view).setCompoundDrawables(null, null, null, null);
        }
    }

}

