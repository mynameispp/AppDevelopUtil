package com.ffzxnet.developutil.utils.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;


public class TxtSpannableUtil {
    private SpannableStringBuilder spannable;

    public TxtSpannableUtil(String str) {
        this.spannable = new SpannableStringBuilder(str);
    }

    public SpannableStringBuilder build() {
        return spannable;
    }

    /**
     * 设置颜色
     *
     * @param color        颜色
     * @param starPosition 从哪里开始
     * @param endPosition  到哪里结束
     * @return
     */
    public TxtSpannableUtil setSpanColor(int color, int starPosition, int endPosition) {
        if (color != 0) {
            ForegroundColorSpan colorSpan =
                    new ForegroundColorSpan(color);  //颜色样式
            spannable.setSpan(colorSpan, starPosition, endPosition,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    /**
     * 设置大小
     *
     * @param size         大小
     * @param starPosition 从哪里开始
     * @param endPosition  到哪里结束
     * @return
     */
    public TxtSpannableUtil setSpanSize(int size, int starPosition, int endPosition) {
        if (size > 0) {
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size, true);
            spannable.setSpan(absoluteSizeSpan, starPosition, endPosition,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    /**
     * 设置字体加粗
     *
     * @param starPosition 从哪里开始
     * @param endPosition  到哪里结束
     * @return
     */
    public TxtSpannableUtil setSpanBold(int starPosition, int endPosition) {
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannable.setSpan(styleSpan, starPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体删除线
     *
     * @param starPosition 从哪里开始
     * @param endPosition  到哪里结束
     * @return
     */
    public TxtSpannableUtil setSpanStrikethrough(int starPosition, int endPosition) {
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannable.setSpan(strikethroughSpan, starPosition, endPosition,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体下划线
     *
     * @param starPosition 从哪里开始
     * @param endPosition  到哪里结束
     * @return
     */
    public TxtSpannableUtil setSpanUnderline(int starPosition, int endPosition) {
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannable.setSpan(underlineSpan, starPosition, endPosition,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体相对大小缩放
     *
     * @param size         缩放比例
     * @param starPosition 从哪里开始
     * @param endPosition  到哪里结束
     * @return
     */
    public TxtSpannableUtil setSpanRelativeSize(float size, int starPosition, int endPosition) {
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(size);
        spannable.setSpan(relativeSizeSpan, starPosition, endPosition,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 加下划线
     *
     * @param start
     * @param end
     */
    public TxtSpannableUtil underline(int start, int end) {
        CharacterStyle span = new UnderlineSpan();  //下滑线样式
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 加中划线
     *
     * @param start
     * @param end
     */
    public TxtSpannableUtil strikethrough(int start, int end) {
        CharacterStyle span = new StrikethroughSpan();
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 在文字中间插入图片
     *
     * @param context
     * @param bitmap
     * @return
     */
    public TxtSpannableUtil setImgInTxtCenter(Context context, int position, Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //需要处理的文本，插入一个字符代表图片要插入的位置 
        //要让图片替代指定的文字就要用ImageSpan
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //开始替换，
        spannable.setSpan(span, position, position + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 在文字中间插入图片
     *
     * @param drawable
     * @return
     */
    public TxtSpannableUtil setImgInTxtCenter(int position, Drawable drawable) {
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //需要处理的文本，插入一个字符代表图片要插入的位置
        //要让图片替代指定的文字就要用ImageSpan
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //开始替换，
        spannable.setSpan(span, position, position + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置指定文字点击事件
     *  自定义点击事件响应如下
     *  class CommonClickableSpan extends ClickableSpan implements View.OnClickListener {
     *         @Override
     *         public void onClick(View widget) {
     *             //点击响应
     *         }
     *
     *         @Override
     *         public void updateDrawState(TextPaint ds) {
     *             super.updateDrawState(ds);
     *             //点击样式
     *             ds.setUnderlineText(false);
     *             ds.clearShadowLayer();
     *             ds.setColor(MyApplication.getColorByResId(R.color.colorPrimaryDark));
     *         }
     *     }
     *     最后要设置下面属性 TextView 才有效果
     *     TextView.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
     *     TextView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
     * @param clickableSpan
     * @param star
     * @param end
     * @return
     */
    public TxtSpannableUtil setTxtClickListen(ClickableSpan clickableSpan, int star, int end) {
        spannable.setSpan(clickableSpan, star, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
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
