package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.fragment.SelectCycFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

/**
 * Created by Eiffel on 2016/3/22.
 * 选择期数页
 */
public class SelectCycActivity extends MyFragmentActivity{
    public static  void getInstance(GoodBean bean,Context context){
        getInstance(context,SelectCycActivity.class,bean);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        CloseActivityUtil.add(SelectCycActivity.this);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected Fragment setContentFragment() {
        return new SelectCycFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_activity_selectcyc);
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
