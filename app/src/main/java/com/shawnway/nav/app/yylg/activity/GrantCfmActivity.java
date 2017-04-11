package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.fragment.GrantCfmFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

/**
 * Created by Eiffel on 2016/2/5.
 *
 * 确认赠与页面
 */
public class GrantCfmActivity extends MyFragmentActivity{
    public static void getRtnInstance(Activity context,String payee,int reqCode){
        Intent intent=new Intent(context,GrantCfmActivity.class);
        intent.putExtra("payee",payee);
        context.startActivityForResult(intent,reqCode);
    }

    @Override
    protected Fragment setContentFragment() {
        return GrantCfmFragment.getInstance(getIntent().getExtras().getString("payee"));
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        CloseActivityUtil.add(GrantCfmActivity.this);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_activity_grant);
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
