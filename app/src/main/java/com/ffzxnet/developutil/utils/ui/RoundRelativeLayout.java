package com.ffzxnet.developutil.utils.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.ffzxnet.developutil.R;

import androidx.annotation.Nullable;

/**
 * 可调整圆角相对布局
 */
public class RoundRelativeLayout extends RelativeLayout {
    private final RectF roundRect = new RectF();
    private float roundCorner = 0;
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();
    private final Paint strokePaint = new Paint();
    public float mStrokeWidth = 0;
    public int mStrokeColor = 0xffffffff;
    public int backgroundColor = 0xffffffff;

    public RoundRelativeLayout(Context context) {
        this(context, null);
    }

    public RoundRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundLayout, defStyleAttr, 0);
            backgroundColor = a.getColor(R.styleable.RoundLayout_backgroundColor, backgroundColor);
            roundCorner = a.getDimensionPixelSize(R.styleable.RoundLayout_round_corner, 0);
            mStrokeWidth = a.getDimensionPixelSize(R.styleable.RoundLayout_stroke_width, 0);
            mStrokeColor = a.getColor(R.styleable.RoundLayout_stroke_color, mStrokeColor);
            a.recycle();
        }

        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        maskPaint.setStyle(Paint.Style.FILL);
        //
        zonePaint.setAntiAlias(true);
//        zonePaint.setColor(backgroundColor);
        //
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(mStrokeColor);
        strokePaint.setStrokeWidth(mStrokeWidth);
        strokePaint.setStyle(Paint.Style.STROKE);

        setBackgroundColor(backgroundColor);
    }


    public void setCorner(float adius) {
        if (adius > 0) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            roundCorner = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, adius, displayMetrics);
        } else {
            roundCorner = 0;
        }
        invalidate();
    }

    public void setStrokeWidth(float strokeWidth) {
        if (strokeWidth > 0) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            mStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, strokeWidth, displayMetrics);
        } else {
            mStrokeWidth = 0;
        }

        strokePaint.setStrokeWidth(mStrokeWidth);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
        roundRect.set(0, 0, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        if (roundCorner > 0) {
            canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
            canvas.drawRoundRect(roundRect, roundCorner, roundCorner, zonePaint);
            canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
            super.draw(canvas);
            canvas.restore();
            if (mStrokeWidth > 0) {
                RectF roundRect2 = new RectF();
                roundRect2.set(roundRect.left + mStrokeWidth / 2, roundRect.top + mStrokeWidth / 2, roundRect.right - mStrokeWidth / 2, roundRect.bottom - mStrokeWidth / 2);
                canvas.drawRoundRect(roundRect2, roundCorner - mStrokeWidth, roundCorner - mStrokeWidth, strokePaint);
            }
        } else {
            super.draw(canvas);
        }
    }
}
