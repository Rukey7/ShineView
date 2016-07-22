package com.dl7.shineview.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by long on 2016/7/21.
 */
public class ShineButton extends ImageView {

    private ShineView mShineView;
    private Activity mActivity;
    private int mBottomHeight;


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
        if (mActivity != null) {
            DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics();
            int[] location = new int[2];
            getLocationInWindow(location);
            mBottomHeight = displayMetrics.heightPixels - location[1];
        }
    }

    public int getBottomHeight() {
        return mBottomHeight;
    }

    private void _getParents() {
    }


    /************************************************************/

    public void startAnim() {
        if (mActivity != null) {
            final ViewGroup rootView = (ViewGroup) mActivity.findViewById(Window.ID_ANDROID_CONTENT);
            mShineView = new ShineView(mActivity);
            mShineView.attachTargetView(this);
            rootView.addView(mShineView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public void removeView(ShineView shineView) {
        if (mActivity != null) {
            ViewGroup rootView = (ViewGroup) mActivity.findViewById(Window.ID_ANDROID_CONTENT);
            rootView.removeView(shineView);
        }
    }
}
