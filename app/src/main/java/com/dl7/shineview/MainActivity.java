package com.dl7.shineview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dl7.shineview.drawable.BubblesDrawable;
import com.dl7.shineview.views.ShineButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.shine_btn)
    ShineButton mShineBtn;
    @BindView(R.id.iv_btn)
    ImageView mIvBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mIvBtn.setImageDrawable(new BubblesDrawable());
    }


    @OnClick({R.id.iv_btn, R.id.shine_btn})
    public void onClick(View view) {
        Log.e("MainActivity", "onClick");
        switch (view.getId()) {
            case R.id.iv_btn:
                break;
            case R.id.shine_btn:
                mShineBtn.startAnim();
//                mShineBtn.setImageDrawable(new BubblesDrawable());
                break;
        }
    }
}
