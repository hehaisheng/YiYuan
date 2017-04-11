package com.shawnway.nav.app.yylg.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;

import com.shawnway.nav.app.yylg.net.VolleyTool;


/**
 * Created by Eiffel on 2016/1/28.
 */
public class MFragmentActivity extends FragmentActivity {

    @Override
    protected void onStop() {
        super.onStop();
        VolleyTool.getInstance(this).getRequestQueue().cancelAll(this);
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
