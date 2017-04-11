package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.fragment.GrantRecordFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

/**
 * Created by Eiffel on 2016/2/15.
 */
public class GrantRecordActivity extends MyFragmentActivity/*FragmentActivity implements View.OnClickListener*/ {
    @Override
    protected Fragment setContentFragment() {
        return GrantRecordFragment.getInstance();
    }

    protected String getToolbarTitle() {
        return getString(R.string.title_activity_grantrec);
    }

    public static void getInstance(Context context) {
        Intent intent = new Intent(context,GrantRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        CloseActivityUtil.add(GrantRecordActivity.this);
        return super.onCreateView(name, context, attrs);
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
