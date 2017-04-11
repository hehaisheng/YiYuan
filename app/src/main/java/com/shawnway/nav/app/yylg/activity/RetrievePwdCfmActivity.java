package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.shawnway.nav.app.yylg.tool.StringUtils;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;

import java.util.HashMap;

/**
 * Created by Eiffel on 2015/12/2.
 */
public class RetrievePwdCfmActivity extends MyActivity implements View.OnClickListener {
    private static final String TAG = "RetrievePwdCfmActivity";
    private long sendTime;


    public static void getInstance(Activity activity, String phone, long sendtime, int reqCode) {
        Intent intent = new Intent(activity,
                RetrievePwdCfmActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("sendtime", sendtime);
        activity.startActivityForResult(intent,
                reqCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(RetrievePwdCfmActivity.this);
        setContentView(R.layout.activity_retrievecfm);
        initToolbar();

    }

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(
                R.string.title_activity_retrieve));
        centerText.setVisibility(View.VISIBLE);

        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

    }

    private MyCountDownTimer timer;

    private void confirm() {
        String code = ((EditText) findViewById(R.id.code_inputer)).getText()
                .toString().trim();
        String pwd = Utils.getEditTextStr(findViewById(R.id.pwd_inputer));
        String pwdc = Utils.getEditTextStr(findViewById(R.id.pwdcfm_inputer));
        HashMap<String, String> params = new HashMap<>();
        params.put("customerCode", code);
        params.put("newPwd", pwd);
        params.put("confirmPwd", pwdc);
        Log.d(TAG, "submit");
        if (isLegalInput(code, pwd, pwdc)) {
            VolleyTool.getInstance(this).sendGsonPostRequest(this,ResponseGson.class, Constants.RETRIEVEPWD_UTL, params, new Response.Listener<ResponseGson>() {

                @Override
                public void onResponse(ResponseGson response) {
                    HeaderGson header = response.getHeader();
                    if (header.code.equals(VolleyTool.SUCCESS)) {
                        successConfirm();
                    } else if (header.code
                            .equals(VolleyTool.VALIDATE_ERROR)) {
                        error(header);
                    }

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    ToastUtil.showNetError(RetrievePwdCfmActivity.this);
                }
            });
        }
    }

    private boolean isLegalInput(String code, String pwd, String pwdc) {
        if (!pwd.equals( pwdc)) {
            ToastUtil.showShort(this, getString(R.string.retrieve_alert_pwd_not_same));
            return false;
        }
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(pwd) || StringUtils.isEmpty(pwdc)) {
            ToastUtil.showShort(this, getString(R.string.retrieve_alert_something_empty));
            return false;
        }
        if (!pwd.matches("[0-9A-Za-z]*")) {
            ToastUtil.showShort(this, getString(R.string.regist_alert_pwd_not_legal, true));
            return false;
        }
        if (pwd.length() < 6) {
            ToastUtil.showShort(this, getString(R.string.regist_alert_pwd_too_short), true);
            return false;
        }
        return true;
    }

    private void successConfirm() {
        Intent intent = getIntent();
        intent.putExtra("sendtime", sendTime);
        setResult(RESULT_OK, intent);
        ToastUtil.show(this, "密码找回成功", 1000);
        finish();
    }

    private void error(HeaderGson header) {
        ToastUtil.show(this,
                getResources().getString(R.string.mobile_verifycode_error),
                1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void cancel() {
        Intent intent = getIntent();
        intent.putExtra("sendtime", sendTime);
        setResult(RESULT_CANCELED, intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        cancel();
        super.onBackPressed();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                confirm();
                break;
            case R.id.top_back:
                cancel();
                break;
            default:
                break;
        }
    }


    class MyCountDownTimer extends CountDownTimer {
        private TextView sendBtn;

        public MyCountDownTimer(long millisInFuture, long countDownInterval,
                                Button button) {
            super(millisInFuture, countDownInterval);
            sendBtn = button;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendBtn.setText("重新发送" + "(" + millisUntilFinished / 1000 + ")");// 设置倒计时时间

        }

        @Override
        public void onFinish() {
            sendBtn.setText("重新获取验证码");
            sendBtn.setEnabled(true);// 重新获得点击
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
