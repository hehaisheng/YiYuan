package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.fragment.CalDetCountDownFragment;
import com.shawnway.nav.app.yylg.fragment.CalDetFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/25 0025.
 */
public class CalDetailCountDownActivity extends MyFragmentActivity {

    @Override
    protected Fragment setContentFragment() {
        return new CalDetCountDownFragment();
    }

    public static void getInstance(Context context, Class<?> clazz, Serializable bean) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("goodbean", bean);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        CloseActivityUtil.add(CalDetailCountDownActivity.this);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_activity_caldetail);
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
