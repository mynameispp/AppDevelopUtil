package com.ffzxnet.developutil.utils.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;

import com.ffzxnet.countrymeet.application.MyApplication;


public class TxtSpannableUtil {
    /**
     * 带有\n换行符的字符串都可以用此方法显示2种颜色  如果没有换行符就使用第一种颜色显示
     *
     * @param text     文字
     * @param color1   换行前的文字颜色    0为默认颜色  getResources().getColor(colorId)
     * @param color2   换行后的文字颜色    0为默认颜色  getResources().getColor(colorId)
     * @param fontSize 文字大小
     * @param qianhou  0前面的字体 1后面的字体
     * @return
     */
    public static SpannableStringBuilder TxtSpannable(String text, int color1, int color2, int fontSize, int qianhou) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);//用于可变字符串
        //字体样式  可组合多个样式
        CharacterStyle span_0 = null, span_1 = null, span_2 = null;
        int end = text.indexOf("\n");
        if (end == -1) {//如果没有换行符就使用第一种颜色显示
            if (color1 != 0) {
                span_0 = new ForegroundColorSpan(color1);
            }
            spannable.setSpan(span_0, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            //字体颜色
            if (color1 != 0) {
                span_0 = new ForegroundColorSpan(color1);
                spannable.setSpan(span_0, 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (color2 != 0) {
                span_1 = new ForegroundColorSpan(color2);
                spannable.setSpan(span_1, end + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //字体大小
            span_2 = new AbsoluteSizeSpan(fontSize, true);
            if (qianhou == 0) {
                spannable.setSpan(span_2, 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spannable.setSpan(span_2, end + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannable;
    }

    /**
     * 带有\n换行符的字符串都可以用此方法显示2种颜色  如果没有换行符就使用第一种颜色显示
     *
     * @param text           文字
     * @param color1         换行前的文字颜色     0为默认颜色 getResources().getColor(colorId)
     * @param color2         换行后的文字颜色    0为默认颜色 getResources().getColor(colorId)
     * @param firstFontSize  分割前文字大小
     * @param secondFontSize 分割后文字大小
     * @return
     */
    public static SpannableStringBuilder TxtSpannable(int color1, int color2, int firstFontSize, int secondFontSize, String text) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);//用于可变字符串
        //字体样式  可组合多个样式
        CharacterStyle span_0 = null, span_1 = null, span_2 = null;
        int end = text.indexOf("\n");
        if (end == -1) {//如果没有换行符就使用第一种颜色显示
            if (color1 != 0) {
                span_0 = new ForegroundColorSpan(color1);
                spannable.setSpan(span_0, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            //字体颜色
            if (color1 != 0) {
                span_0 = new ForegroundColorSpan(color1);
                spannable.setSpan(span_0, 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (color2 != 0) {
                span_1 = new ForegroundColorSpan(color2);
                spannable.setSpan(span_1, end + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //字体大小
            if (firstFontSize != 0) {
                span_2 = new AbsoluteSizeSpan(firstFontSize, true);
                spannable.setSpan(span_2, 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (secondFontSize != 0) {
                span_2 = new AbsoluteSizeSpan(secondFontSize, true);
                spannable.setSpan(span_2, end + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannable;
    }

    /**
     * 0为颜色不变
     * 带有\n换行符的字符串都可以用此方法显示2种颜色
     *
     * @param text   文字
     * @param color1 换行前的文字颜色 getResources().getColor(colorId)
     * @param color2 换行后的文字颜色 getResources().getColor(colorId)
     * @return
     */
    public static SpannableStringBuilder TxtSpannable(String text, int color1, int color2) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);//用于可变字符串
        //字体样式  可组合多个样式
        CharacterStyle span_0 = null, span_1 = null, span_2 = null;
        int end = text.indexOf("\n");
        if (end == -1) {//如果没有换行符就使用第一种颜色显示
            if (color1 != 0) {
                span_0 = new ForegroundColorSpan(color1);
                spannable.setSpan(span_0, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            //字体颜色
            if (color1 != 0) {
                span_0 = new ForegroundColorSpan(color1);
                spannable.setSpan(span_0, 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            span_1 = new ForegroundColorSpan(color2);
            spannable.setSpan(span_1, end + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    /**
     * 指定文字颜色
     *
     * @param txt   字符串
     * @param color 指定颜色 0不改变现有的颜色
     * @param start 从那开始
     * @param end   结束
     * @param size  指定文字大小 为0的时候为默认大小
     * @return
     */
    public static Spannable highlight(String txt, int color, int start, int end, int size) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(txt);//用于可变字符串
        if (color != 0) {
            ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(MyApplication.getContext(),color));  //颜色样式
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (size > 0) {
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size, true);
            spannable.setSpan(absoluteSizeSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    /**
     * 指定文字颜色
     *
     * @param txt   字符串
     * @param color 指定颜色 0不改变现有的颜色  getResources().getColor(colorId)
     * @param start 从那开始
     * @param end   结束
     * @return
     */
    public static Spannable highlight(String txt, int color, int start, int end, int color1, int start1, int end1) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(txt);//用于可变字符串
        if (color != 0) {
            ForegroundColorSpan span = new ForegroundColorSpan(color);  //颜色样式
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (color1 != 0) {
            ForegroundColorSpan span = new ForegroundColorSpan(color1);
            spannable.setSpan(span, start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    /**
     * 指定文字颜色
     *
     * @param txt   字符串
     * @param color 指定颜色 0不改变现有的颜色  getResources().getColor(colorId)
     * @param size  指定文字大小 为0的时候为默认大小
     * @return
     */
    public static Spannable highlight(String txt, int color, int size) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(txt);//用于可变字符串
        int end = txt.length();
        if (color != 0) {
            ForegroundColorSpan span = new ForegroundColorSpan(color);  //颜色样式
//			spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(span, 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (size > 0) {
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size, true);
            spannable.setSpan(absoluteSizeSpan, 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    /**
     * 加下划线
     *
     * @param txt
     * @param start
     * @param end
     */
    public static Spannable underline(String txt, int start, int end) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(txt);
        CharacterStyle span = new UnderlineSpan();  //下滑线样式
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 加中划线
     *
     * @param txt
     */
    public static Spannable strikethrough(String txt) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(txt);
        CharacterStyle span = new StrikethroughSpan();
        spannable.setSpan(span, 0, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 加中划线
     *
     * @param txt
     * @param star 从那开始
     */
    public static Spannable strikethrough(String txt, int star) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(txt);
        CharacterStyle span = new StrikethroughSpan();
        spannable.setSpan(span, star, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 在文字中间插入图片
     *
     * @param context
     * @param txt
     * @param bitmap
     * @return
     */
    public static Spannable setImgInTxtCenter(Context context, String txt, Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //需要处理的文本，插入一个字符代表图片要插入的位置 
        StringBuilder builder = new StringBuilder(txt);
        builder.insert(txt.length() / 2, "\n \n");
        SpannableStringBuilder spannable = new SpannableStringBuilder(builder.toString());
        //要让图片替代指定的文字就要用ImageSpan  
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //开始替换，
        spannable.setSpan(span, builder.toString().indexOf(" "), builder.toString().indexOf(" ") + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable;
    }

//    /**
//     * 获取人民币文本
//     *
//     * @param amount 金额
//     * @param rate   比例
//     * @return
//     */
//    public static SpannableStringBuilder getRmbText(String amount, float rate) {
//        SpannableStringBuilder ssb = new SpannableStringBuilder(MyApplication.getResours().getText(R.string.rmb_sign));
//        ssb.append(amount);
//        //如果1，不需要进行后面判断
//        if (rate == 1) {
//            return ssb;
//        }
//        RelativeSizeSpan span = new RelativeSizeSpan(rate);
//        ssb.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //小数点后面-又诚说要变小，后面小数点
//        if (amount.contains(".")) {
//            RelativeSizeSpan spanPositon = new RelativeSizeSpan(rate);
//            ssb.setSpan(spanPositon, ssb.length() - 2, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        return ssb;
//    }
}
