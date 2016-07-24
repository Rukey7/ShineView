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

import com.dl7.shineview.drawable.BubblesDrawable;

/**
 * Created by long on 2016/7/21.
 */
public class ShineView extends View implements Animatable {

    private static final int DOT_COUNT = 16;
    private static final float ORIGIN_ANGLE = -85.0f;
    private static final float OFFSET_ANGLE = 5f;
    private static final float BIG_SMALL_ANGLE_INTERVAL = 10.0f;
    private static final int SHINE_DURATION = 3000;

    // 大小圆点画笔
    private Paint mBigPaint;
    private Paint mSmallPaint;
    // 大小圆矩形框
    private RectF mBigRect;
    private RectF mSmallRect;
    // 颜色
    private int mBigColor = Color.RED;
    private int mSmallColor = Color.GREEN;
    // 原始矩形框
    private RectF mOriginRect;
    // 圆点大小
    private float mBigSize;
    private float mSmallSize;
    // 属性动画
    private ValueAnimator mValueAnimator;
    // 动画时间
    private int mDuration = SHINE_DURATION;
    // 动画启动延迟时间
    private int mStartDelay;
    // 目标视图
    private ShineButton mTargetView;
    // 中心坐标
    private int centerX;
    private int centerY;
    // 动画百分比
    private float mPercent;
    // 圆点的角度间隔
    private float mDotAngleInterval;
    // 大小圆点的空间间隔
    private float mDotInterval;

    private BubblesDrawable mDrawable;


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
        mBigPaint.setColor(mBigColor);
        mBigPaint.setStyle(Paint.Style.STROKE);
        mBigPaint.setStrokeCap(Paint.Cap.ROUND);

        mSmallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallPaint.setColor(mSmallColor);
        mSmallPaint.setStyle(Paint.Style.STROKE);
        mSmallPaint.setStrokeCap(Paint.Cap.ROUND);

        mBigRect = new RectF();
        mSmallRect = new RectF();
        mOriginRect = new RectF();
        mDotAngleInterval = 360.0f / DOT_COUNT;

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.w("ShineView", "" + mPercent);
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        _initAnimation();
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
        // 计算中心点坐标
        int width = mTargetView.getWidth();
        int height = mTargetView.getHeight();
        int bottomHeight = mTargetView.getBottomHeight();
        int[] location = new int[2];
        mTargetView.getLocationInWindow(location);
        centerX = location[0] + width / 2;
        centerY = getMeasuredHeight() - bottomHeight + height / 2;

        Log.w("ShineView", ""+centerX);
        Log.w("ShineView", ""+centerY);
        Log.e("ShineView", ""+width);
        Log.e("ShineView", ""+height);
        Log.i("ShineView", ""+location[0]);
        Log.i("ShineView", ""+getMeasuredHeight());
        // 设置大小圆点的参数
        final int startSpace = Math.max(width, height);
        // 圆点移动距离
        final int moveSpace = Math.min(width, height) / 3 * 2;
        mDotInterval = startSpace / 6;
        mBigSize = startSpace / 5;
        mSmallSize = startSpace / 8;
        mBigPaint.setStrokeWidth(mBigSize);
        mSmallPaint.setStrokeWidth(mSmallSize);

        // 初始化矩形框
        mOriginRect.set(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
        mSmallRect.set(mOriginRect);
        mBigRect.set(mSmallRect.left - mDotInterval, mSmallRect.top - mDotInterval,
                mSmallRect.right + mDotInterval, mSmallRect.bottom + mDotInterval);
        // 初始化属性动画
        mValueAnimator = ValueAnimator.ofFloat(0, moveSpace);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPercent = value / (moveSpace);
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
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setAlpha(1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mTargetView.removeView(ShineView.this);
            }
        });
        Log.e("ShineView", ""+mStartDelay);
        Log.e("ShineView", ""+mDuration);
        mValueAnimator.setStartDelay(mStartDelay);
        mValueAnimator.setDuration(mDuration);
        setAlpha(0);
        mValueAnimator.start();

        mDrawable = new BubblesDrawable();
        mDrawable.setBounds(0, 0, width, height);
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
            Log.e("ShineView", "start");
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

    public void setAnimatorDelay(int startDelay) {
        mStartDelay = startDelay;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setBigColor(int bigColor) {
        mBigColor = bigColor;
        mBigPaint.setColor(mBigColor);
    }

    public void setSmallColor(int smallColor) {
        mSmallColor = smallColor;
        mSmallPaint.setColor(smallColor);
    }
}
