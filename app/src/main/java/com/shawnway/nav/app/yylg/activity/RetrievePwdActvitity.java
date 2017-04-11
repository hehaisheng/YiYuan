package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.HeaderGson;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Eiffel on 2015/12/2.
 */
public class RetrievePwdActvitity extends MyActivity {
    private static final int REQUEST_ENTER_CUSTOMER_CODE = 0;
    private static final int RESULT_RESEND = 0;
    private final int PHONE_LENTH = 11;


    public static void getInstance(Context context) {
        Intent i = new Intent(context, RetrievePwdActvitity.class);
        context.startActivity(i);
    }

    private static final String TAG = "RetrievePwdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(RetrievePwdActvitity.this);
        setContentView(R.layout.activity_retrievepwd);
        initToolbar();
    }

    private void sendValCode() {
        Log.d(TAG, "sending");
        final String phone = ((EditText) findViewById(R.id.input_phone)).getText().toString().trim();
        if (isLegalNumber(phone)) {

            HashMap<String, String> map = new HashMap<>();
            map.put("cellphone", phone);

            VolleyTool.getInstance(this).sendGsonPostRequest(this,ResponseGson.class, Constants.RETRIEVEPWDVALIDATE_URL, map, new Response.Listener<ResponseGson>() {

                @Override
                public void onResponse(ResponseGson response) {
                    HeaderGson header = response.getHeader();
                    String code = header.code;
                    if (code.equals(VolleyTool.SUCCESS)) {
                        success(phone);
                    } else if (code.equals(VolleyTool.VALIDATE_ERROR)) {
                        ToastUtil
                                .show(RetrievePwdActvitity.this, header.error, 1000);
                    }
                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    int statueCode = 0;
                    try {
                        statueCode = error.networkResponse.statusCode;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    if (statueCode !=0) {
                        ToastUtil.showShort(RetrievePwdActvitity.this, getString(R.string.request_error_param_wrong)+": "+statueCode, true);
                    } else {
                        ToastUtil.showNetError(RetrievePwdActvitity.this);
                    }
                }
            });

        }

    }


    private boolean isLegalNumber(String phone, String captcha) {
        if (phone == null || phone.isEmpty()) {
            ToastUtil.showShort(this, getString(R.string.regist_alert_phone_not_empty), true);
            return false;
        }

        if (phone.length() != PHONE_LENTH) {
            ToastUtil.showShort(this, getString(R.string.regist_alert_phone_not_legal), true);
            return false;
        }

        return true;

    }

    private boolean isLegalNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            ToastUtil.showShort(this, getString(R.string.regist_alert_phone_not_empty), true);
            return false;
        }

        if (phone.length() != PHONE_LENTH) {
            ToastUtil.showShort(this, getString(R.string.regist_alert_phone_not_legal), true);
            return false;
        }

        return true;

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void success(final String phone) {
        if (!phone.equals(lastphone)) {
            lastphone = phone;
            sendtime = Calendar.getInstance()
                    .getTimeInMillis();
        }
        RetrievePwdCfmActivity.getInstance(this, phone, sendtime, REQUEST_ENTER_CUSTOMER_CODE);

        ToastUtil
                .show(RetrievePwdActvitity.this, getResources().getString(R.string.tips_validatecode_sended), 1000);
    }

    String lastphone;
    long sendtime;

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(
                R.string.title_activity_retrieve));
        centerText.setVisibility(View.VISIBLE);

        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_RESEND:
                // RisgistCfmActivity时重新发送了短信
                sendtime = data.getExtras().getLong("sendtime");
                break;
            case RESULT_OK:
                // 注册成功
                finish();
            default:
                break;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrieve_send:
                sendValCode();
                break;
            case R.id.top_back:
                finish();
                break;
            case R.id.top_back_text:
                finish();
                break;
            case R.id.captcha_img:
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
