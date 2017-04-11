package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.PrizeShowResponse;
import com.shawnway.nav.app.yylg.bean.ShareListWrapper;
import com.shawnway.nav.app.yylg.fragment.ShareDetialFragment;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2015/11/7.
 */
public class ShareDetialActivity extends FragmentActivity implements View.OnClickListener {

    public static void getInstance(final Context context, long prizeShowId) {

        Intent intent = new Intent(context, ShareDetialActivity.class);
        intent.putExtra("id",prizeShowId);
        context.startActivity(intent);

    }

    public static void getInstance(Context context, ShareListWrapper.ShareBean shareBean) {
        Intent intent = new Intent(context, ShareDetialActivity.class);
        intent.putExtra("bean",shareBean);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(ShareDetialActivity.this);
        setContentView(R.layout.activity_with_fragment);
        initToolbar();
        initFragment();
    }

    private void initFragment() {

        Fragment fragment = new ShareDetialFragment();
        fragment.setArguments(getIntent().getExtras()); // FragmentActivity将点击的菜单列表标题传递给Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).commit();

    }


    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_share_detail));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
            default:
                break;
        }
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
