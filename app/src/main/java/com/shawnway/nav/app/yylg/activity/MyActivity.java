package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import com.shawnway.nav.app.yylg.net.VolleyTool;


/**
 * Created by Eiffel on 2016/1/28.
 */
public class MyActivity extends Activity {
    @Override
    protected void onDestroy() {
        VolleyTool.getInstance(this).getRequestQueue().cancelAll(this);
        super.onDestroy();
    }

    protected  void logD(String message){
        Log.d(getClass().getSimpleName(),message);
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
