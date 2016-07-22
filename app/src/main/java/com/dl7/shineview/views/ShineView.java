package com.dl7.shineview.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by long on 2016/7/21.
 */
public class ShineView extends View implements Animatable {

    private static final int DOT_COUNT = 16;
    private static final float ORIGIN_ANGLE = -85.0f;
    private static final float OFFSET_ANGLE = 5f;
    private static final float BIG_SMALL_ANGLE_INTERVAL = 10.0f;
    private static final int SHINE_DURATION = 3000;

    private Paint mBigPaint;
    private Paint mSmallPaint;
    private RectF mBigRect;
    private RectF mSmallRect;
    private RectF mOriginRect;
    private float mBigSize;
    private float mSmallSize;
    private ValueAnimator mValueAnimator;
    private ShineButton mTargetView;
    private int centerX;
    private int centerY;
    private int mStartRadius;
    private int mEndRadius;
    private float mPercent;
    private float mDotAngleInterval;
    private float mDotInterval;


    public ShineView(Context context) {
        super(context);
        _init();
    }

    public ShineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init();
    }


    private void _init() {
        mBigPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBigPaint.setColor(Color.GREEN);
        mBigPaint.setStyle(Paint.Style.STROKE);
        mBigPaint.setStrokeCap(Paint.Cap.ROUND);

        mSmallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallPaint.setColor(Color.RED);
        mSmallPaint.setStyle(Paint.Style.STROKE);
        mSmallPaint.setStrokeCap(Paint.Cap.ROUND);

        mBigRect = new RectF();
        mSmallRect = new RectF();
        mOriginRect = new RectF();
        mDotAngleInterval = 360.0f / DOT_COUNT;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("ShineView", "onSizeChanged ： " + h);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        _initAnimation();
        Log.w("ShineView", ""+mPercent);
//        if (mPercent > ANIM_CHANGE_POINT) {
//            float scale = (1.0f - mPercent) / (1.0f - ANIM_CHANGE_POINT);
//            mBigPaint.setStrokeWidth(mBigSize * scale);
//            mSmallPaint.setStrokeWidth(mSmallSize * scale);
//        }
        float scale = 1.0f - mPercent;
        mBigPaint.setStrokeWidth(mBigSize * scale);
        mSmallPaint.setStrokeWidth(mSmallSize * scale);

        for (int i = 0; i < DOT_COUNT; i++) {
            canvas.drawArc(mBigRect, ORIGIN_ANGLE + mDotAngleInterval * i + mPercent * OFFSET_ANGLE,
                    0.1f, false, mBigPaint);
        }

        for (int i = 0; i < DOT_COUNT; i++) {
            canvas.drawArc(mSmallRect,
                    ORIGIN_ANGLE + mDotAngleInterval * i + mPercent * OFFSET_ANGLE - BIG_SMALL_ANGLE_INTERVAL,
                    0.1f, false, mSmallPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("ShineView", "onMeasure");
    }

    /************************************************************/

    public void attachTargetView(ShineButton view) {
        mTargetView = view;
    }

    /************************************************************/

    private void _initAnimation() {
        if (isRunning()) {
            return;
        }
        if (mValueAnimator == null) {
            int width = mTargetView.getWidth();
            int height = mTargetView.getHeight();
            int bottomHeight = mTargetView.getBottomHeight();
            int[] location = new int[2];
            mTargetView.getLocationInWindow(location);
            centerX = location[0] + width / 2;
            centerY = getMeasuredHeight() - bottomHeight + height / 2;

            mStartRadius = Math.max(width, height);
            mEndRadius = mStartRadius + Math.min(width, height) / 3 * 2;
            mDotInterval = mStartRadius / 6;
            mBigSize = mStartRadius / 5;
            mSmallSize = mStartRadius / 8;
            mBigPaint.setStrokeWidth(mBigSize);
            mSmallPaint.setStrokeWidth(mSmallSize);
            
            mOriginRect.set(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
            mSmallRect.set(mOriginRect);
            mBigRect.set(mSmallRect.left - mDotInterval, mSmallRect.top - mDotInterval,
                    mSmallRect.right + mDotInterval, mSmallRect.bottom + mDotInterval);

        }
        mValueAnimator = ValueAnimator.ofFloat(0, mEndRadius - mStartRadius);
        mValueAnimator.setDuration(SHINE_DURATION);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPercent = value / (mEndRadius - mStartRadius);
                value /= 2;
                mSmallRect.set(mOriginRect.left - value, mOriginRect.top - value,
                        mOriginRect.right + value, mOriginRect.bottom + value);
                mBigRect.set(mSmallRect.left - mDotInterval, mSmallRect.top - mDotInterval,
                        mSmallRect.right + mDotInterval, mSmallRect.bottom + mDotInterval);
                invalidate();
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mTargetView.removeView(ShineView.this);
            }
        });
        mValueAnimator.start();
    }

    @Override
    public void start() {
        if (mTargetView == null) {
            throw new RuntimeException("You must attach the target view first!");
        }
        if (isRunning()) {
            mValueAnimator.end();
        }
        if (mValueAnimator != null) {
            mValueAnimator.start();
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            mValueAnimator.end();
        }
    }

    @Override
    public boolean isRunning() {
        return mValueAnimator != null && mValueAnimator.isRunning();
    }
}
