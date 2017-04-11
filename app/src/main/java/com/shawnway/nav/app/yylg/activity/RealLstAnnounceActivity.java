package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.fragment.LstAnnounFragment;
import com.shawnway.nav.app.yylg.fragment.RealLstAnnounFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

/**
 * Created by Administrator on 2016/6/1 0001.
 */
public class RealLstAnnounceActivity extends MyFragmentActivity {

    private String physicalName;
    public static void getInstance(Context context) {
        Intent i = new Intent(context, RealLstAnnounceActivity.class);
        context.startActivity(i);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        CloseActivityUtil.add(RealLstAnnounceActivity.this);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected Fragment setContentFragment() {
        String value = getIntent().getStringExtra("value");//第几个实体区
        physicalName = getIntent().getStringExtra("physicalName");//实体区的名字
        Bundle bundle = new Bundle();
        bundle.putString("value",value);
        RealLstAnnounFragment fragment = new RealLstAnnounFragment();
        fragment.setArguments(bundle);
        return fragment;
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
