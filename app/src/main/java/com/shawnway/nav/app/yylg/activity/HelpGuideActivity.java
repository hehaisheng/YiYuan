package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.fragment.HelpGuideFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

/**
 * Created by Eiffel on 2015/12/30.
 */
public class HelpGuideActivity extends MyFragmentActivity{
    @Override
    protected Fragment setContentFragment() {
        return new HelpGuideFragment();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        CloseActivityUtil.add(HelpGuideActivity.this);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_activity_helpguide);
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
