package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.HelpCenterResponse;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Utils;

/**
 * Created by Eiffel on 2015/12/2.
 */
public class HelpCenterActivity extends MyActivity implements View.OnClickListener {

    private TextView shawnway;
    private HelpCenterResponse mData;

    public static void getInstance(Context context) {
        Intent i = new Intent(context, HelpCenterActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(HelpCenterActivity.this);
        setContentView(R.layout.activity_helpcenter);
        shawnway = (TextView) findViewById(R.id.shawnway);
        shawnway.setOnClickListener(this);
        initToolbar();
        initData();
    }

    private void initData() {
        VolleyTool.getInstance(this).sendGsonRequest(this,HelpCenterResponse.class, Constants.HELP_CENTER, new Response.Listener<HelpCenterResponse>() {
            @Override
            public void onResponse(HelpCenterResponse helpCenterResponse) {
                if (!Utils.handleResponseError(HelpCenterActivity.this, helpCenterResponse)) {
                    HelpCenterResponse.HelpCenterBody body = helpCenterResponse.getBody();
                    ((TextView) findViewById(R.id.tv_helpphone)).setText(getString(R.string.helpcenter_phone, body.hotline));
                    ((TextView) findViewById(R.id.tv_helpcr)).setText(body.copyright);
                    mData=helpCenterResponse;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_helpcenter));
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
                break;
            case R.id.tv_helpguide_link:
                if (mData!=null) {
                    HelpGuideActivity.getInstance(this,HelpGuideActivity.class,mData.getBody().guideInfo);
                }
                break;
            case R.id.shawnway:
                WebViewActivity.getInstance(this, R.string.shawnway,
                        "http://www.shawnway.cn/", null, false, true, false);
                break;
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
