package com.dl7.shineview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by long on 2016/7/21.
 */
public class ShineView extends View {

    private Paint mPaint;
    private RectF mRect;


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
        mRect.set(50, 50, w - 50, h - 50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawArc(mRect, 11.0f, 0.1f, false, mPaint);
        canvas.drawPoint(mRect.centerX(), mRect.centerY(), mPaint);
    }
}
