package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.fragment.IntroCodeFragment;
import com.shawnway.nav.app.yylg.fragment.SelfPickFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

/**
 * Created by Eiffel on 2015/12/28.
 */
public class SelfPickActivity extends MyFragmentActivity{
    @Override
    protected Fragment setContentFragment() {
        return SelfPickFragment.getInstance();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        CloseActivityUtil.add(SelfPickActivity.this);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_activity_selfpick);
    }

    public static void getInstance(Context context) {
        Intent intent=new Intent(context,SelfPickActivity.class);
        context.startActivity(intent);
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
