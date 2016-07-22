package com.dl7.shineview.drawable;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by long on 2016/7/2.
 * 吹泡泡Drawable
 */
public class BubblesDrawable extends Drawable implements Animatable {

    private static final int BUBBLES_DURATION = 800;
    private static final float BROKEN_PERCENT = 0.8f;
    // 混合模式
    private static final PorterDuffXfermode PORTER_DUFF_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    private Paint mPaint;
    // 用来绘制泡泡吹破效果图
    private Bitmap mBitmap;
    private Canvas mCanvas;
    // 动画控制
    private ValueAnimator mValueAnimator;
    // 扩散半径
    private int mRadius;
    // 绘制的矩形框
    private RectF mRect = new RectF();
    // 动画启动延迟时间
    private int mStartDelay;
    // 最大半径
    private int mMaxRadius;
    // 动画持续时间
    private int mDuration = BUBBLES_DURATION;


    // 自定义一个扩散半径属性
    Property<BubblesDrawable, Integer> mRadiusProperty = new Property<BubblesDrawable, Integer>(Integer.class, "radius") {
        @Override
        public void set(BubblesDrawable object, Integer value) {
            object.setRadius(value);
        }

        @Override
        public Integer get(BubblesDrawable object) {
            return object.getRadius();
        }
    };
    public int getRadius() {
        return mRadius;
    }
    public void setRadius(int radius) {
        mRadius = radius;
    }


    public BubblesDrawable() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    public void draw(Canvas canvas) {
        // 绘制泡泡
        if (mRadius > mMaxRadius) {
            mCanvas.drawCircle(mRect.centerX(), mRect.centerY(), mMaxRadius, mPaint);
            mPaint.setXfermode(PORTER_DUFF_XFERMODE);
            mCanvas.drawCircle(mRect.centerX(), mRect.centerY(), mRadius % mMaxRadius, mPaint);
            mPaint.setXfermode(null);
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        } else {
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), mRadius, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.RGBA_8888;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect.set(_clipSquare(bounds));
        if (isRunning()) {
            stop();
        }
        mBitmap = Bitmap.createBitmap((int)mRect.width(), (int)mRect.height(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        // 计算最大半径
        mMaxRadius = (int) (mRect.width() / 2);

        // 控制扩散半径的属性变化
        Keyframe radiusFrame0 = Keyframe.ofInt(0, 0);
        Keyframe radiusFrame1 = Keyframe.ofInt(BROKEN_PERCENT, mMaxRadius);
        Keyframe radiusFrame2 = Keyframe.ofInt(1.0f, 2 * mMaxRadius);
        PropertyValuesHolder radiusHolder = PropertyValuesHolder.ofKeyframe(mRadiusProperty,
                radiusFrame0, radiusFrame1, radiusFrame2);
        // 控制透明度的属性变化
        Keyframe alphaFrame0 = Keyframe.ofInt(0, 255);
        Keyframe alphaFrame1 = Keyframe.ofInt(BROKEN_PERCENT, 50);
        Keyframe alphaFrame2 = Keyframe.ofInt(1.0f, 0);
        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofKeyframe("alpha",
                alphaFrame0, alphaFrame1, alphaFrame2);

        mValueAnimator = ObjectAnimator.ofPropertyValuesHolder(this, radiusHolder, alphaHolder);
        mValueAnimator.setStartDelay(mStartDelay);
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        int duration = (int) ((float)mDuration / BROKEN_PERCENT);
        mValueAnimator.setDuration(duration);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 监听属性动画并进行重绘
                invalidateSelf();
            }
        });
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        start();
    }

    /**
     * 裁剪Rect为正方形
     * @param rect
     * @return
     */
    private Rect _clipSquare(Rect rect) {
        int w = rect.width();
        int h = rect.height();
        int min = Math.min(w, h);
        int cx = rect.centerX();
        int cy = rect.centerY();
        int r = min / 2;
        return new Rect(
                cx - r,
                cy - r,
                cx + r,
                cy + r
        );
    }

    /************************************************************/

    @Override
    public void start() {
        mValueAnimator.start();
    }

    @Override
    public void stop() {
        mValueAnimator.end();
    }

    @Override
    public boolean isRunning() {
        return mValueAnimator != null && mValueAnimator.isRunning();
    }

    public void setAnimatorDelay(int startDelay) {
        mStartDelay = startDelay;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }
}
