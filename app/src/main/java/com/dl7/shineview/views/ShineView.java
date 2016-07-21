package com.dl7.shineview.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by long on 2016/7/21.
 */
public class ShineView extends View implements Animatable {

    private Paint mPaint;
    private RectF mRect;
    private ValueAnimator mValueAnimator;
    private ShineButton mDestView;
    private int centerX;
    private int centerY;
    private int mStartRadius;
    private int mEndRadius;

    public ShineView(Context context) {
        super(context);
        _init();
    }

    public ShineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init();
    }


    private void _init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(50);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawArc(mRect, 11.0f, 0.1f, false, mPaint);
        canvas.drawPoint(mRect.centerX(), mRect.centerY(), mPaint);
    }

    /************************************************************/

    public void attachDestView(ShineButton view) {
        mDestView = view;
    }

    /************************************************************/

    @Override
    public void start() {
        if (mValueAnimator == null && mDestView != null) {
            int width = mDestView.getWidth();
            int height = mDestView.getHeight();
            int bottomHeight = mDestView.getBottomHeight();
            int[] location = new int[2];
            getLocationInWindow(location);
            centerX = location[0] + width / 2;
            centerY = getMeasuredHeight() - bottomHeight + height / 2;
            mRect.set(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
            mStartRadius = Math.max(width, height);
            mEndRadius = mStartRadius + Math.min(width, height) / 2;


        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
