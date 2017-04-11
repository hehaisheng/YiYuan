package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.fragment.LstAnnounFragment;
import com.shawnway.nav.app.yylg.fragment.ShareContentFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class ShareActivity extends MyFragmentActivity {

    public static void getInstance(Context context){
        Intent intent = new Intent(context, ShareActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        CloseActivityUtil.add(ShareActivity.this);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected Fragment setContentFragment() {
        return new ShareContentFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.title_activity_share);
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
