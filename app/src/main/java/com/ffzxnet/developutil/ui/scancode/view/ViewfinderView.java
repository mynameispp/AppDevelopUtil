package com.ffzxnet.developutil.ui.scancode.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.ui.scancode.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

public final class ViewfinderView extends View {
    private static final int[] SCANNER_ALPHA = new int[]{0, 64, 128, 192, 255, 192, 128, 64};

    private static final long ANIMATION_DELAY = 10L;

    private static final int OPAQUE = 255;

    private static final int CORNER_RECT_WIDTH = 8;

    private static final int CORNER_RECT_HEIGHT = 40;

    private static final int SCANNER_LINE_MOVE_DISTANCE = 5;

    private static final int SCANNER_LINE_HEIGHT = 10;

    private final Paint paint;

    private Bitmap resultBitmap;

    private final int maskColor;

    private final int resultColor;

    private final int frameColor;

    private final int laserColor;

    private final int cornerColor;

    private final int resultPointColor;

    private int scannerAlpha;

    private final String labelText;

    private final int labelTextColor;

    private final float labelTextSize;

    public static int scannerStart = 0;

    public static int scannerEnd = 0;

    private Collection<ResultPoint> possibleResultPoints;

    private Collection<ResultPoint> lastPossibleResultPoints;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewfinderView);
        this.laserColor = array.getColor(R.styleable.ViewfinderView_laser_color, 65280);
        this.cornerColor = array.getColor(R.styleable.ViewfinderView_corner_color, 65280);
        this.frameColor = array.getColor(R.styleable.ViewfinderView_frame_color, 16777215);
        this.resultPointColor = array.getColor(R.styleable.ViewfinderView_result_point_color, -1056964864);
        this.maskColor = array.getColor(R.styleable.ViewfinderView_mask_color, 1610612736);
        this.resultColor = array.getColor(R.styleable.ViewfinderView_result_color, -1342177280);
        this.labelTextColor = array.getColor(R.styleable.ViewfinderView_label_text_color, -1862270977);
        this.labelText = array.getString(R.styleable.ViewfinderView_label_text);
        this.labelTextSize = array.getFloat(R.styleable.ViewfinderView_label_text_size, 36.0F);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.scannerAlpha = 0;
        this.possibleResultPoints = new HashSet<>(5);
    }

    public void onDraw(Canvas canvas) {
        //控制扫码框位置
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null)
            return;
        if (scannerStart == 0 || scannerEnd == 0) {
            scannerStart = frame.top;
            scannerEnd = frame.bottom;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        drawExterior(canvas, frame, width, height);
        if (this.resultBitmap != null) {
            this.paint.setAlpha(255);
            canvas.drawBitmap(this.resultBitmap, frame.left, frame.top, this.paint);
        } else {
            drawFrame(canvas, frame);
            drawCorner(canvas, frame);
            drawTextInfo(canvas, frame);
            drawLaserScanner(canvas, frame);
            Collection<ResultPoint> currentPossible = this.possibleResultPoints;
            Collection<ResultPoint> currentLast = this.lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                this.lastPossibleResultPoints = null;
            } else {
                this.possibleResultPoints = new HashSet<>(5);
                this.lastPossibleResultPoints = currentPossible;
                this.paint.setAlpha(255);
                this.paint.setColor(this.resultPointColor);
                for (ResultPoint point : currentPossible)
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0F, this.paint);
            }
            if (currentLast != null) {
                this.paint.setAlpha(127);
                this.paint.setColor(this.resultPointColor);
                for (ResultPoint point : currentLast)
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0F, this.paint);
            }
            postInvalidateDelayed(10L, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    /**
     * 文字提示
     *
     * @param canvas
     * @param frame
     */
    private void drawTextInfo(Canvas canvas, Rect frame) {
        this.paint.setColor(this.labelTextColor);
        this.paint.setTextSize(this.labelTextSize);
        this.paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(this.labelText, (frame.left + frame.width() / 2), (frame.top - 40), this.paint);
    }

    /**
     * 扫码框边角
     *
     * @param canvas
     * @param frame
     */
    private void drawCorner(Canvas canvas, Rect frame) {
        this.paint.setColor(this.cornerColor);
        int top = frame.top;
        int bottom = frame.bottom;
        //左上角
        canvas.drawRect(frame.left, top, (frame.left + 8), (top + 40), this.paint);
        canvas.drawRect(frame.left, top, (frame.left + 40), (top + 8), this.paint);
        //右上角
        canvas.drawRect((frame.right - 8), top, frame.right + 1, (top + 40), this.paint);
        canvas.drawRect((frame.right - 40), top, frame.right, (top + 8), this.paint);
        //左下角
        canvas.drawRect(frame.left, (bottom - 8), (frame.left + 40), bottom + 1, this.paint);
        canvas.drawRect(frame.left, (bottom - 40), (frame.left + 8), bottom, this.paint);
        //右下角
        canvas.drawRect((frame.right - 8), (bottom - 40), frame.right + 1, bottom, this.paint);
        canvas.drawRect((frame.right - 40), (bottom - 8), frame.right + 1, bottom + 1, this.paint);
    }

    /**
     * 扫码条
     *
     * @param canvas
     * @param frame
     */
    private void drawLaserScanner(Canvas canvas, Rect frame) {
        this.paint.setColor(this.laserColor);
//        LinearGradient linearGradient = new LinearGradient(frame.left, scannerStart, frame.left, (scannerStart + 10), shadeColor(this.laserColor), this.laserColor, Shader.TileMode.MIRROR);
        RadialGradient radialGradient = new RadialGradient((frame.left + frame.width() / 2), (scannerStart + 5), 360.0F, this.laserColor, shadeColor(this.laserColor), Shader.TileMode.MIRROR);
//        SweepGradient sweepGradient = new SweepGradient((frame.left + frame.width() / 2), (scannerStart + 10), shadeColor(this.laserColor), this.laserColor);
//        ComposeShader composeShader = new ComposeShader((Shader)radialGradient, (Shader)linearGradient, PorterDuff.Mode.ADD);
        this.paint.setShader((Shader) radialGradient);
        if (scannerStart <= scannerEnd) {
            RectF rectF = new RectF((frame.left + 20), scannerStart, (frame.right - 20), (scannerStart + 10));
            canvas.drawOval(rectF, this.paint);
            scannerStart += 5;
        } else {
            scannerStart = frame.top;
        }
        this.paint.setShader(null);
    }

    public int shadeColor(int color) {
        String hax = Integer.toHexString(color);
        String result = "20" + hax.substring(2);
        return Integer.valueOf(result, 16).intValue();
    }

    /**
     * 扫码框大小和四边线条
     *
     * @param canvas
     * @param frame
     */
    private void drawFrame(Canvas canvas, Rect frame) {
        this.paint.setColor(this.frameColor);
        int top = frame.top;
        int bottom = frame.bottom;
        canvas.drawRect(frame.left, top, (frame.right + 1), (top + 2), this.paint);
        canvas.drawRect(frame.left, (top + 2), (frame.left + 2), (bottom - 1), this.paint);
        canvas.drawRect((frame.right - 1), top, (frame.right + 1), (bottom - 1), this.paint);
        canvas.drawRect(frame.left, (bottom - 1), (frame.right + 1), (bottom + 1), this.paint);
    }

    private void drawExterior(Canvas canvas, Rect frame, int width, int height) {
        int top = frame.top;
        int bottom = frame.bottom;
        this.paint.setColor((this.resultBitmap != null) ? this.resultColor : this.maskColor);
        canvas.drawRect(0.0F, 0.0F, width, top, this.paint);
        canvas.drawRect(0.0F, top, frame.left, (bottom + 1), this.paint);
        canvas.drawRect((frame.right + 1), top, width, (bottom + 1), this.paint);
        canvas.drawRect(0.0F, (bottom + 1), width, height, this.paint);
    }

    public void drawViewfinder() {
        this.resultBitmap = null;
        invalidate();
    }

    public void drawResultBitmap(Bitmap barcode) {
        this.resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        this.possibleResultPoints.add(point);
    }
}

