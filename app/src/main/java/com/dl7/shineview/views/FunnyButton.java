package com.dl7.shineview.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dl7.shineview.R;
import com.dl7.shineview.drawable.BubblesDrawable;

/**
 * Created by long on 2016/7/25.
 */
public class FunnyButton extends FrameLayout {

    private final static int FUNNY_DELAY = 500;
    private final static int FUNNY_DURATION = 800;
    private final static float DOT_POPUP_PERCENT = 0.3f;


    private ImageView mIvIcon;
    private ImageView mIvPopup;
    private ShineView mShineView;
    private Activity mActivity;
    private int mBottomHeight;
    private int mFunnyDelay = FUNNY_DELAY;
    private int mFunnyDuration = FUNNY_DURATION;
    private int mDotPopupDelay;
    private ValueAnimator mAnimator;
    private BubblesDrawable mDrawable;
    private float mScale;


    // 自定义一个扩散半径属性
    Property<FunnyButton, Float> mScaleProperty = new Property<FunnyButton, Float>(Float.class, "scale") {
        @Override
        public void set(FunnyButton object, Float value) {
            object.setScale(value);
        }

        @Override
        public Float get(FunnyButton object) {
            return object.getScale();
        }
    };
    public float getScale() {
        return mScale;
    }
    public void setScale(float scale) {
        mScale = scale;
    }

    public FunnyButton(Context context) {
        this(context, null);
    }

    public FunnyButton(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FunnyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context);
    }

    private void _init(Context context) {
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        LayoutInflater.from(context).inflate(R.layout.layout_funny, this, true);
        mIvPopup = (ImageView) findViewById(R.id.iv_popup);
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);

        Keyframe scaleFrame1 = Keyframe.ofFloat(0, 0);
        Keyframe scaleFrame2 = Keyframe.ofFloat(DOT_POPUP_PERCENT + 0.1f, 1);
        Keyframe scaleFrame3 = Keyframe.ofFloat(0.6f, 1.2f);
        Keyframe scaleFrame4 = Keyframe.ofFloat(0.8f, 0.9f);
        Keyframe scaleFrame5 = Keyframe.ofFloat(1f, 1f);
//        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofKeyframe("scaleX",
//                scaleFrame1, scaleFrame2, scaleFrame3, scaleFrame4, scaleFrame5);
//        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofKeyframe("scaleY",
//                scaleFrame1, scaleFrame2, scaleFrame3, scaleFrame4, scaleFrame5);
//        mAnimator = ObjectAnimator.ofPropertyValuesHolder(mIvIcon, scaleXHolder, scaleYHolder);
        PropertyValuesHolder scaleHolder = PropertyValuesHolder.ofKeyframe(mScaleProperty,
                scaleFrame1, scaleFrame2, scaleFrame3, scaleFrame4, scaleFrame5);
        mAnimator = ValueAnimator.ofPropertyValuesHolder(scaleHolder);
        mAnimator.setDuration(FUNNY_DURATION);
        mAnimator.setStartDelay(FUNNY_DELAY);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value > 1.0f) {
                    setScaleX(value);
                    setScaleY(value);
                } else {
                    mIvIcon.setScaleX(value);
                    mIvIcon.setScaleY(value);
                }
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                _reset();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                _reset();
            }
        });

        mDotPopupDelay = (int) (FUNNY_DURATION * DOT_POPUP_PERCENT);
        mDrawable = new BubblesDrawable();
        mDrawable.setDuration(FUNNY_DELAY);
        mIvPopup.setImageDrawable(mDrawable);
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

    private void _reset() {
        ViewCompat.setScaleX(this, 1);
        ViewCompat.setScaleY(this, 1);
        ViewCompat.setScaleX(mIvIcon, 1);
        ViewCompat.setScaleY(mIvIcon, 1);
    }


    /************************************************************/

    /**
     * 设置Icon
     * @param resId
     */
    public void setIcon(int resId) {
        mIvIcon.setImageResource(resId);
    }
    public void setIcon(Drawable drawable) {
        mIvIcon.setImageDrawable(drawable);
    }

    public void startAnim() {
        if (mActivity != null) {
            final ViewGroup rootView = (ViewGroup) mActivity.findViewById(Window.ID_ANDROID_CONTENT);
            mShineView = new ShineView(mActivity);
//            mShineView.setAnimatorDelay(mDotPopupDelay + FUNNY_DELAY);
//            mShineView.setDuration(FUNNY_DURATION - mDotPopupDelay);
            mShineView.setAnimatorDelay(FUNNY_DELAY - mDotPopupDelay);
            mShineView.setDuration(FUNNY_DURATION);
            mShineView.attachTargetView(this);
            rootView.addView(mShineView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        mIvIcon.setScaleX(0);
        mIvIcon.setScaleY(0);
        mAnimator.start();
        mDrawable.start();
    }

    public void removeView(ShineView shineView) {
        if (mActivity != null) {
            ViewGroup rootView = (ViewGroup) mActivity.findViewById(Window.ID_ANDROID_CONTENT);
            rootView.removeView(shineView);
        }
    }
}
