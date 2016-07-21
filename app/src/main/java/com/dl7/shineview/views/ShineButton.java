package com.dl7.shineview.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by long on 2016/7/21.
 */
public class ShineButton extends ImageView {

    private ShineView mShineView;
    private Activity mActivity;


    public ShineButton(Context context) {
        this(context, null);
    }

    public ShineButton(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ShineButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context);
    }

    private void _init(Context context) {
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    public void init(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void _getParents() {
    }
}
