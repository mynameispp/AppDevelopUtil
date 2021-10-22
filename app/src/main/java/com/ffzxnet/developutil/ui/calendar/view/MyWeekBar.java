package com.ffzxnet.developutil.ui.calendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.ffzxnet.developutil.R;
import com.necer.utils.Attrs;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class MyWeekBar extends AppCompatTextView {


    public String[] days = {"日", "一", "二", "三", "四", "五", "六"};

    private int type;//一周的第一天是周几
    private TextPaint textPaint;
    private String todayOfWeek;//今天星期几

    public MyWeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NCalendar);
        type = ta.getInt(R.styleable.NCalendar_firstDayOfWeek, Attrs.SUNDAY);
        ta.recycle();

        textPaint = getPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    public void setCurrentDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 2:
                todayOfWeek = "一";
                break;
            case 3:
                todayOfWeek = "二";
                break;
            case 4:
                todayOfWeek = "三";
                break;
            case 5:
                todayOfWeek = "四";
                break;
            case 6:
                todayOfWeek = "五";
                break;
            case 7:
                todayOfWeek = "六";
                break;
            default:
                todayOfWeek = "日";
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getMeasuredWidth() - paddingRight - paddingLeft;
        int height = getMeasuredHeight() - paddingTop - paddingBottom;
        for (int i = 0; i < days.length; i++) {
            Rect rect = new Rect(paddingLeft + (i * width / days.length), paddingTop, paddingLeft + ((i + 1) * width / days.length), paddingTop + height);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float top = fontMetrics.top;
            float bottom = fontMetrics.bottom;
            int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
            String day;
            if (type == Attrs.MONDAY) {
                int j = i + 1;
                day = days[j > days.length - 1 ? 0 : j];
            } else {
                day = days[i];
            }
            //设置字体颜色
            if (day.equals(todayOfWeek)) {
                //今日星期
                textPaint.setColor(Color.parseColor("#247EF9"));
            } else {
                textPaint.setColor(Color.parseColor("#000000"));
            }
            canvas.drawText(day, rect.centerX(), baseLineY, textPaint);
//            canvas.drawColor(Color.parseColor("#FFFFFF"));
        }
    }

}
