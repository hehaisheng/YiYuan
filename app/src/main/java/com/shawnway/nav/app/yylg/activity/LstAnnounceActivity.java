package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.fragment.LstAnnounFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

import java.util.Date;

/**
 * Created by Eiffel on 2015/12/13.
 */
public class LstAnnounceActivity extends MyFragmentActivity {
    public static void getInstance(Context context) {
        Intent i = new Intent(context, LstAnnounceActivity.class);
        context.startActivity(i);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        CloseActivityUtil.add(LstAnnounceActivity.this);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected Fragment setContentFragment() {
        return new LstAnnounFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_activity_lstanounce);
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
