package com.dl7.shineview.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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

    private final static int FUNNY_DELAY = 500;
    private final static int FUNNY_DURATION = 800;
    private final static float DOT_POPUP_PERCENT = 0.2f;


    private ShineView mShineView;
    private Activity mActivity;
    private int mBottomHeight;
    private int mFunnyDelay = FUNNY_DELAY;
    private int mFunnyDuration = FUNNY_DURATION;
    private int mDotPopupDelay;
    private ObjectAnimator mAnimator;


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

        Keyframe scaleFrame1 = Keyframe.ofFloat(0, 0);
        Keyframe scaleFrame2 = Keyframe.ofFloat(DOT_POPUP_PERCENT + 0.1f, 1);
        Keyframe scaleFrame3 = Keyframe.ofFloat(0.6f, 1.2f);
        Keyframe scaleFrame4 = Keyframe.ofFloat(0.8f, 0.9f);
        Keyframe scaleFrame5 = Keyframe.ofFloat(1f, 1f);
        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofKeyframe("scaleX",
                scaleFrame1, scaleFrame2, scaleFrame3, scaleFrame4, scaleFrame5);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofKeyframe("scaleY",
                scaleFrame1, scaleFrame2, scaleFrame3, scaleFrame4, scaleFrame5);
        mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, scaleXHolder, scaleYHolder);
        mAnimator.setDuration(FUNNY_DURATION);
        mAnimator.setStartDelay(FUNNY_DELAY);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setAlpha(1);
            }
        });

        mDotPopupDelay = (int) (FUNNY_DURATION * DOT_POPUP_PERCENT);
//        mDrawable.setAnimatorDelay(FUNNY_DELAY + mDotPopupDelay);
//        mDrawable.setDuration(FUNNY_DURATION - mDotPopupDelay);
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
            mShineView.setAnimatorDelay(mDotPopupDelay + FUNNY_DELAY);
            mShineView.setDuration(FUNNY_DURATION - mDotPopupDelay);
            mShineView.attachTargetView(this);
            rootView.addView(mShineView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
//        setBackgroundDrawable(mDrawable);
        setAlpha(0);
        mAnimator.start();
    }

    public void removeView(ShineView shineView) {
        if (mActivity != null) {
            ViewGroup rootView = (ViewGroup) mActivity.findViewById(Window.ID_ANDROID_CONTENT);
            rootView.removeView(shineView);
        }
    }
}
