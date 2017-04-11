package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;

public class WelcomPageActivity extends Activity {

    private ImageView mShowPicture;
    private TextView mShowText;

    private Animation mFadeIn;
    private Animation mFadeInScale;
    private Animation mFadeOut;

    private Drawable mPicture_1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mShowPicture = (ImageView) findViewById(R.id.guide_picture);
        mShowText = (TextView) findViewById(R.id.guide_content);
        init();
        setListener();
    }

    private void init() {
        initPicture();
        initAnim();
        mShowPicture.setImageDrawable(mPicture_1);
        mShowPicture.startAnimation(mFadeIn);
    }

    private void initAnim() {
        mFadeIn = AnimationUtils.loadAnimation(this,
                R.anim.guide_welcome_fade_in);
        mFadeOut = AnimationUtils.loadAnimation(this,
                R.anim.guide_welcome_fade_out);
    }

    private void initPicture() {
        mPicture_1 = getResources().getDrawable(R.drawable.yy_guide_pic1);
    }

    private void setListener() {
        mFadeIn.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       mShowPicture.startAnimation(mFadeOut);
                   }
               }, getResources().getInteger(R.integer.welcome_delay)) ;
            }
        });
        mFadeOut.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                mShowPicture.setImageDrawable(null);

                startActivity(new Intent(WelcomPageActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config=new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config,res.getDisplayMetrics() );
//        return res;
//    }
}