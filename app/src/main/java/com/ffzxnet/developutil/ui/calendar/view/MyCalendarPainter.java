package com.ffzxnet.developutil.ui.calendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.ffzxnet.developutil.utils.tools.DateUtils;
import com.ffzxnet.developutil.utils.tools.ScreenUtils;
import com.necer.entity.CalendarDate;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Attrs;
import com.necer.utils.CalendarUtil;
import com.necer.utils.DrawableUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class MyCalendarPainter implements CalendarPainter {
    private int noAlphaColor = 255;
    protected Paint mTextPaint;
    //    protected Paint mBgPaint;
    private float mCircleRadius;//选中背景圆角大小
    private Context mContext;
    //假日休息和补班属性
    private boolean showHolidayWorkday = true;//是否显示假日补班标识
    private int holidayWorkdayLocation = Attrs.TOP_RIGHT;//假日补班文字标识位置
    private int holidayWorkdayDistanceWidth = 35;//假日补班标识左右间距
    private int holidayWorkdayDistanceHeight = 60;//假日补班标识上下间距
    private float holidayWorkdayTextSize = 8;//假日补班标识字体大小
    private Drawable todayCheckedHoliday;//休息日图标
    private Drawable todayCheckedWorkday;//补班日图标
    private String workdayText = "班";//补班日文字
    private String holidayText = "休";//休息日文字
    private List<LocalDate> mHolidayList;//休息日期
    private List<LocalDate> mWorkdayList;//补班日期
    //公历和农历属性
    private float solarSize = 18;//公历字体大小
    private int solarBgGrayColor = Color.parseColor("#DDDEE1");
    private int solarBgBlueColor = Color.parseColor("#247EF9");
    private float lunarSize = 10;//农历字体大小
    private float lunarMarginTop = 17;//农历与公历的间距
    //日期标记信息
    private List<LocalDate> mPointList;//标记点
    private int pointCircleSize = 5;//标记点大小
    //颜色
    private int blueColor = Color.parseColor("#247EF9");
    private int blackColor = Color.parseColor("#000000");
    private int grayColor = Color.parseColor("#8B929C");

    public MyCalendarPainter(Context context) {
        //所有canvas都是从布局中心点开始计算画的
        mContext = context;

        mCircleRadius = (float) (solarSize * 2.2);

        mTextPaint = getPaint();
//        mBgPaint = getPaint();

//        mBgPaint.setColor(Color.parseColor("#DDDEE1"));

        //加载休息日期
        mHolidayList = new ArrayList<>();
        mWorkdayList = new ArrayList<>();
        List<String> holidayList = MyCalendarUtil.getHolidayList();
        for (int i = 0; i < holidayList.size(); i++) {
            mHolidayList.add(new LocalDate(holidayList.get(i)));
        }
        //补班日期
        List<String> workdayList = MyCalendarUtil.getWorkdayList();
        for (int i = 0; i < workdayList.size(); i++) {
            mWorkdayList.add(new LocalDate(workdayList.get(i)));
        }
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    @Override
    public void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList) {
        //当日
//        drawSelectBg(canvas, rectF, localDate, true, checkedDateList);
        drawSolar(canvas, rectF, localDate, checkedDateList.contains(localDate), true);
        drawLunar(canvas, rectF, localDate, checkedDateList.contains(localDate), blueColor, blueColor, true);
        drawPoint(canvas, rectF, localDate);
        drawHolidayWorkday(canvas, rectF, localDate, todayCheckedHoliday, todayCheckedWorkday
                , blackColor, blackColor, noAlphaColor, checkedDateList.contains(localDate), true);
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList) {
        //当前查看月
        drawSolar(canvas, rectF, localDate, checkedDateList.contains(localDate), false);
        drawLunar(canvas, rectF, localDate, checkedDateList.contains(localDate), grayColor, grayColor, false);
        drawPoint(canvas, rectF, localDate);
        drawHolidayWorkday(canvas, rectF, localDate, todayCheckedHoliday, todayCheckedWorkday
                , blackColor, blackColor, noAlphaColor, checkedDateList.contains(localDate), false);
    }

    @Override
    public void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList) {
        //上个月或者下个月
        drawSolar(canvas, rectF, localDate, checkedDateList.contains(localDate), false);
        drawLunar(canvas, rectF, localDate, checkedDateList.contains(localDate), grayColor, grayColor, false);
        drawPoint(canvas, rectF, localDate);
        drawHolidayWorkday(canvas, rectF, localDate, todayCheckedHoliday, todayCheckedWorkday
                , blackColor, blackColor, noAlphaColor, checkedDateList.contains(localDate), false);
    }

    //绘制选中背景
//    private void drawSelectBg(Canvas canvas, RectF rectF, LocalDate localDate, boolean isSelected,int selectColor,int unSelectColor,  List<LocalDate> selectedDateList) {
//
//        mBgPaint.setAlpha(isSelected ? 255 : 100);
//
//        LocalDate lastLocalDate = localDate.minusDays(1);
//        LocalDate nextLocalDate = localDate.plusDays(1);
//
//        if (selectedDateList.contains(localDate)) {
//                //圆形
//                mBgPaint.setAntiAlias(true);
//                mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//                canvas.drawCircle(rectF.centerX(), rectF.centerY(), mCircleRadius, mBgPaint);
//
//        }else {
//
//        }
//    }

    //绘制公历
    private void drawSolar(Canvas canvas, RectF rectF, LocalDate date, boolean isSelected, boolean isToday) {
        if (isSelected) {
            //选中背景
            if (!isToday) {
                mTextPaint.setColor(solarBgGrayColor);
            } else {
                mTextPaint.setColor(solarBgBlueColor);
            }
            canvas.drawCircle(rectF.centerX(), rectF.centerY() - mCircleRadius / 2, mCircleRadius, mTextPaint);
            //字体颜色
            if (!isToday) {
                int nowDate = Integer.parseInt(DateUtils.getTimeFormat(System.currentTimeMillis(), DateUtils.FORMAT_YYYYMMDD));
                int dateS = Integer.parseInt(date.toString(DateUtils.FORMAT_YYYYMMDD));
                if (dateS >= nowDate) {
                    //今日和之后的日期
                    mTextPaint.setColor(blackColor);
                } else {
                    //今日之前的日期
                    mTextPaint.setColor(grayColor);
                }
            } else {
                //当日日期
                mTextPaint.setColor(Color.WHITE);
            }
        } else {
            //未选中背景
            if (!isToday) {
                int nowDate = Integer.parseInt(DateUtils.getTimeFormat(System.currentTimeMillis(), DateUtils.FORMAT_YYYYMMDD));
                int dateS = Integer.parseInt(date.toString(DateUtils.FORMAT_YYYYMMDD));
                if (dateS >= nowDate) {
                    //今日和之后的日期
                    mTextPaint.setColor(blackColor);
                } else {
                    //今日之前的日期
                    mTextPaint.setColor(grayColor);
                }
            } else {
                //当日日期
                mTextPaint.setColor(blueColor);
            }
        }
        mTextPaint.setTextSize(ScreenUtils.dip2px(mContext, solarSize));
        mTextPaint.setFakeBoldText(true);
//        mTextPaint.setColor(isSelected ? selectColor : unSelectColor);
//        mTextPaint.setAlpha(isCurrectMonthOrWeek ? 255 : 100);
        canvas.drawText(date.getDayOfMonth() + "", rectF.centerX(), rectF.centerY(), mTextPaint);

    }

    //绘制农历
    private void drawLunar(Canvas canvas, RectF rectF, LocalDate date, boolean isSelected, int selectColor, int unSelectColor, boolean isCurrectMonthOrWeek) {
        mTextPaint.setTextSize(ScreenUtils.dip2px(mContext, lunarSize));
        CalendarDate calendarDate = MyCalendarUtil.getCalendarDate(date);
        mTextPaint.setColor(isSelected ? selectColor : unSelectColor);
//        mTextPaint.setAlpha(isCurrectMonthOrWeek ? 255 : 100);
        String lunarContent;
        if (!TextUtils.isEmpty(calendarDate.lunarHoliday)) {
            lunarContent = calendarDate.lunarHoliday;
        } else if (!TextUtils.isEmpty(calendarDate.solarTerm)) {
            lunarContent = calendarDate.solarTerm;
        } else if (!TextUtils.isEmpty(calendarDate.solarHoliday)) {
            lunarContent = calendarDate.solarHoliday;
        } else {
            lunarContent = calendarDate.lunar.lunarOnDrawStr;
        }
        canvas.drawText(lunarContent, rectF.centerX(), rectF.centerY() + ScreenUtils.dip2px(mContext, lunarMarginTop), mTextPaint);
    }

    //绘制标记
    private void drawPoint(Canvas canvas, RectF rectF, LocalDate date) {
        if (null != mPointList && mPointList.contains(date)) {
            int nowDate = Integer.parseInt(DateUtils.getTimeFormat(System.currentTimeMillis(), DateUtils.FORMAT_YYYYMMDD));
            int dateS = Integer.parseInt(date.toString(DateUtils.FORMAT_YYYYMMDD));
            if (dateS >= nowDate) {
                mTextPaint.setColor(blueColor);
            } else {
                mTextPaint.setColor(grayColor);
            }
            //pointCircleSize=大小，+ 75=顶部中心点边距
            canvas.drawCircle(rectF.centerX(), rectF.centerY() + 75, pointCircleSize, mTextPaint);
        }
    }

    //设置标记
    public void setPointList(List<String> list) {
        if (mPointList == null) {
            mPointList = new ArrayList<>();
        } else {
            mPointList.clear();
        }
        for (int i = 0; i < list.size(); i++) {
            LocalDate localDate = null;
            try {
                localDate = new LocalDate(list.get(i));
                mPointList.add(localDate);
            } catch (Exception e) {
                throw new RuntimeException("setPointList的参数需要 yyyy-MM-dd 格式的日期");
            }
        }
        //外部刷新
//        mCalendar.notifyCalendar();
    }

    //绘制节假日
    private void drawHolidayWorkday(Canvas canvas, RectF rectF, LocalDate localDate, Drawable holidayDrawable, Drawable workdayDrawable
            , int holidayTextColor, int workdayTextColor, int alphaColor, boolean isSelected, boolean isToday) {
        if (showHolidayWorkday && (mHolidayList.contains(localDate) || mWorkdayList.contains(localDate))) {
            int[] holidayLocation = getHolidayWorkdayLocation(rectF.centerX(), rectF.centerY());
            if (isSelected) {
                //选中背景（双色）
                //先画第一层底色
                mTextPaint.setColor(Color.WHITE);
                canvas.drawCircle(holidayLocation[0], holidayLocation[1], ScreenUtils.dip2px(mContext, holidayWorkdayTextSize) - 4, mTextPaint);
                //再画第二层底色
                if (!isToday) {
                    //非当日
                    mTextPaint.setColor(solarBgGrayColor);
                } else {
                    mTextPaint.setColor(solarBgBlueColor);
                }
                canvas.drawCircle(holidayLocation[0], holidayLocation[1], ScreenUtils.dip2px(mContext, holidayWorkdayTextSize) - 7, mTextPaint);
            }
            if (isToday) {
                //当日日期选中字体变色
                if (isSelected) {
                    holidayTextColor = Color.WHITE;
                    workdayTextColor = Color.WHITE;
                } else {
                    holidayTextColor = blueColor;
                    workdayTextColor = blueColor;
                }

            }
            if (mHolidayList.contains(localDate)) {
                //假日
                if (holidayDrawable == null) {
                    //文字显示
                    mTextPaint.setTextSize(ScreenUtils.dip2px(mContext, holidayWorkdayTextSize));
                    mTextPaint.setColor(holidayTextColor);
                    canvas.drawText(TextUtils.isEmpty(holidayText) ? "休" : holidayText, holidayLocation[0], getTextBaseLineY(holidayLocation[1]), mTextPaint);
                } else {
                    Rect drawableBounds = DrawableUtil.getDrawableBounds(holidayLocation[0], holidayLocation[1], holidayDrawable);
                    holidayDrawable.setBounds(drawableBounds);
                    holidayDrawable.setAlpha(alphaColor);
                    holidayDrawable.draw(canvas);
                }
            } else if (mWorkdayList.contains(localDate)) {
                //补班
                if (workdayDrawable == null) {
                    //文字显示
                    mTextPaint.setTextSize(ScreenUtils.dip2px(mContext, holidayWorkdayTextSize));
                    mTextPaint.setColor(workdayTextColor);
//                    mTextPaint.setFakeBoldText(mAttrs.holidayWorkdayTextBold);//加粗
                    canvas.drawText(TextUtils.isEmpty(workdayText) ? "班" : workdayText, holidayLocation[0], getTextBaseLineY(holidayLocation[1]), mTextPaint);
                } else {
                    Rect drawableBounds = DrawableUtil.getDrawableBounds(holidayLocation[0], holidayLocation[1], workdayDrawable);
                    workdayDrawable.setBounds(drawableBounds);
                    workdayDrawable.setAlpha(alphaColor);
                    workdayDrawable.draw(canvas);
                }
            }
        }
    }

    //HolidayWorkday的位置
    private int[] getHolidayWorkdayLocation(float centerX, float centerY) {
        int[] location = new int[2];
        switch (holidayWorkdayLocation) {
            case Attrs.TOP_LEFT:
                location[0] = (int) (centerX - holidayWorkdayDistanceWidth);
                location[1] = (int) (centerY - holidayWorkdayDistanceHeight);
                break;
            case Attrs.BOTTOM_RIGHT:
                location[0] = (int) (centerX + holidayWorkdayDistanceWidth);
                location[1] = (int) (centerY + holidayWorkdayDistanceHeight);
                break;
            case Attrs.BOTTOM_LEFT:
                location[0] = (int) (centerX - holidayWorkdayDistanceWidth);
                location[1] = (int) (centerY + holidayWorkdayDistanceHeight);
                break;
            case Attrs.TOP_RIGHT:
            default:
                location[0] = (int) (centerX + holidayWorkdayDistanceWidth);
                location[1] = (int) (centerY - holidayWorkdayDistanceHeight);
                break;
        }
        return location;

    }

    //canvas.drawText的基准线
    private float getTextBaseLineY(float centerY) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return centerY - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
    }
}
