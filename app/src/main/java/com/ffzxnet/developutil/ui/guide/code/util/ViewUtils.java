package com.ffzxnet.developutil.ui.guide.code.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.viewpager.widget.ViewPager;

public class ViewUtils {
    private static final String FRAGMENT_CON = "NoSaveStateFrameLayout";

    public static Rect getLocationInView(View parent, View child) {
        if (child == null || parent == null) {
            throw new IllegalArgumentException("parent and child can not be null .");
        }

        View decorView = null;
        Context context = child.getContext();
        if (context instanceof Activity) {
            decorView = ((Activity) context).getWindow().getDecorView();
        }

        Rect result = new Rect();
        Rect tmpRect = new Rect();

        View tmp = child;

        if (child == parent) {
            child.getHitRect(result);
            return result;
        }
        while (tmp != decorView && tmp != parent) {
            tmp.getHitRect(tmpRect);
            if (!tmp.getClass().equals(FRAGMENT_CON)) {
                result.left += tmpRect.left;
                result.top += tmpRect.top;
            }
            tmp = (View) tmp.getParent();
            if (tmp == null) {
                throw new IllegalArgumentException("the view is not showing in the window!");
            }
            //fix ScrollView中无法获取正确的位置
            if (tmp.getParent() instanceof ScrollView) {
                ScrollView scrollView = (ScrollView) tmp.getParent();
                int scrollY = scrollView.getScrollY();
                result.top -= scrollY;
            }
            if (tmp.getParent() instanceof HorizontalScrollView) {
                HorizontalScrollView horizontalScrollView = (HorizontalScrollView) tmp.getParent();
                int scrollX = horizontalScrollView.getScrollX();
                result.left -= scrollX;
            }

            //added by isanwenyu@163.com fix bug #21 the wrong rect user will received in ViewPager
            if (tmp.getParent() != null && (tmp.getParent() instanceof ViewPager)) {
                tmp = (View) tmp.getParent();
            }
        }
        result.right = result.left + child.getMeasuredWidth();
        result.bottom = result.top + child.getMeasuredHeight();
        return result;
    }
}
