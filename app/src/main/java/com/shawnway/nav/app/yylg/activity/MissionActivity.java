package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.abview.MissionItemView;
import com.shawnway.nav.app.yylg.bean.SignResponseWrapper;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.thuongnh.zprogresshud.ZProgressHUD;

/**
 * Created by Eiffel on 2015/12/2.
 */
public class MissionActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "PresentActivity";
    private static final String STA_SIGNED = "ALREADY_SIGNED";
    private static final String STA_UNSIGN="NOT_SIGNED";

private final int[] missions = {R.id.mission0};
    private Context mContext;

    public static void getInstance(Context context) {
        Intent i = new Intent(context, MissionActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        CloseActivityUtil.add(MissionActivity.this);
        setContentView(R.layout.activity_present);
        initToolbar();
        setListener();
        initView();
    }

    private void initView() {
        updateMissionSta();
    }

    private Handler handler = new Handler();
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            VolleyTool.getInstance(mContext).sendGsonRequest(this, SignResponseWrapper.class, Constants.CHECK_SIGN_URL, new Response.Listener<SignResponseWrapper>() {
                @Override
                public void onResponse(SignResponseWrapper signResponseWrapper) {
                    if (!Utils.handleResponseError(MissionActivity.this, signResponseWrapper)) {
                        int dayCount = signResponseWrapper.getBody().continueSignDay;
                        TextView signCount = (TextView) findViewById(R.id.sign_count);
                        signCount.setText(dayCount + "");
                        if (dayCount == 30) {
                            findViewById(R.id.sign_30_wrapper).setVisibility(View.VISIBLE);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            });
        }
    };

    private void updateMissionSta() {
        final ZProgressHUD progressHUD=ZProgressHUD.getInstance(this);
        progressHUD.setMessage("努力刷新中");
        progressHUD.show();
        VolleyTool.getInstance(this).sendGsonRequest(this, SignResponseWrapper.class, Constants.CHECK_SIGN_URL, new Response.Listener<SignResponseWrapper>() {
            @Override
            public void onResponse(SignResponseWrapper signResponseWrapper) {
                if (!Utils.handleResponseError(MissionActivity.this, signResponseWrapper)) {
                    int dayCount = signResponseWrapper.getBody().continueSignDay;
                    TextView signCount = (TextView) findViewById(R.id.sign_count);
                    signCount.setText(dayCount + "");
                    if (dayCount == 30) {
                        findViewById(R.id.sign_30_wrapper).setVisibility(View.VISIBLE);
                    }
                    if (signResponseWrapper.getBody().signStatus.equals(STA_SIGNED)) {
                        ((MissionItemView) findViewById(R.id.mission0)).updateButton(MissionItemView.SIGNED);
                    } else if (signResponseWrapper.getBody().signStatus.equals(STA_UNSIGN)) {
                        ((MissionItemView) findViewById(R.id.mission0)).updateButton(MissionItemView.UNSIGN);
                    }
                    refreshSignDay();
                }
                progressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressHUD.dismiss();
            }
        });

    }

    private void refreshSignDay() {
        handler.postDelayed(run,1*1000);
    }


    private void setListener() {
        for (int id : missions) {
            setMissionListener((MissionItemView) findViewById(id));
        }
    }

    private void setMissionListener(MissionItemView mission) {
        mission.setOnClickListener(this);
        mission.setButtonListener(this, mission.getTag());//设置misson的领取奖励按钮的响应事件，按钮id为mission_button
    }


    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_present));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== Constants.ACTION_REQUEST_LOGIN&&resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onLoginResult");
            updateMissionSta();
        } else if (requestCode== Constants.ACTION_REQUEST_LOGIN){
          finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.mission0:
                break;
            case R.id.mission_button:
                String tag = null;
                try {
                    tag = (String) v.getTag();
                } catch (ClassCastException e) {
                    Log.e(TAG, "should give a tag to missionbutton");
                }
                if (tag != null)
                    switch (tag) {
                       //这里可以根据button被添加的tag进行对应的响应事件，tag在setMissionListener时设置
                    }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(run);
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
