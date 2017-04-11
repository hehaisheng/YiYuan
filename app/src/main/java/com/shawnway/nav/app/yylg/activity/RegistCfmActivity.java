package com.shawnway.nav.app.yylg.activity;

import java.util.Calendar;
import java.util.HashMap;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.HeaderGson;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.GsonRequest;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;

public class RegistCfmActivity extends FragmentActivity implements OnClickListener {
    private static final String TAG = "DetailActicity";
    private long sendTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(RegistCfmActivity.this);
        setContentView(R.layout.activity_register_summit);
        initValue();
        initToolbar();

    }

    private void initValue() {
        Bundle bundle = getIntent().getExtras();
        TextView phoneTxt = (TextView) findViewById(R.id.regist_msg_sended_text);
        phoneTxt.setText(bundle.getString("phone"));
        final Button sendBtn = (Button) findViewById(R.id.register_resend);
        setCountDown(sendTime = bundle.getLong("sendtime"), sendBtn);
    }

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(
                R.string.regist_page_title));
        centerText.setVisibility(View.VISIBLE);

        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

    }

    private void setCountDown(long sendtime, final Button sendBtn) {
        sendBtn.setEnabled(false);
        long curTime = Calendar.getInstance().getTimeInMillis();
        long remainTime = (getResources().getInteger(
                R.integer.regist_countdown_time)
                * 1000 - curTime + sendTime);
        Log.d(TAG, "curTime:" + curTime + "  sendTime:" + sendtime);
        timer = new MyCountDownTimer(remainTime, 1000, sendBtn);
        timer.start();
    }

    private MyCountDownTimer timer;

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_resend:
                resend();
                sendTime = Calendar.getInstance().getTimeInMillis();
                break;
            case R.id.register_cfmregist_btn:
                if (Constants.dummy) {
                    successConfirm();
                } else {
                    confirm();
                }
                break;
            case R.id.top_back:
                cancel();
                break;
            default:
                break;
        }
    }

    private void confirm() {
        String code = ((EditText) findViewById(R.id.code_inputer)).getText()
                .toString().trim();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("customerCode", code);

        GsonRequest<ResponseGson> request = new GsonRequest<ResponseGson>(
                Method.POST, Constants.REGIST_URL, ResponseGson.class, this,
                new Listener<ResponseGson>() {

                    @Override
                    public void onResponse(ResponseGson response) {
                        HeaderGson header = response.getHeader();

                        if (!Utils.handleResponseError(RegistCfmActivity.this, response)) {
                            if (header.code.equals(VolleyTool.SUCCESS)) {
                                successConfirm();
                            }
                        }

                    }

                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShort(RegistCfmActivity.this, VolleyErrorHelper.getMessage(error, RegistCfmActivity.this));

            }
        });
        request.setParams(params);
        request.setSendCookie();
        VolleyTool.getInstance(this).getRequestQueue().add(request);
    }

    private void resend() {

        Log.d(TAG, "resending");
        GsonRequest<ResponseGson> request = new GsonRequest<>(Method.POST,
                Constants.RESEND_URL, ResponseGson.class, this,
                new Listener<ResponseGson>() {

                    @Override
                    public void onResponse(ResponseGson response) {
                        HeaderGson header = response.getHeader();
                        String code = header.code;
                        if (code.equals(VolleyTool.SUCCESS)) {
                            sendTime = Calendar.getInstance().getTimeInMillis();
                            final Button sendBtn = (Button) findViewById(R.id.register_resend);
                            setCountDown(sendTime, sendBtn);
                            Log.d(TAG, "resended");

                        } else if (code
                                .equals(VolleyTool.ATHENTICATION_REQUIRED)) {

                            String tips = getResources().getString(
                                    R.string.wrong_authentication_timeout);
                            ToastUtil.show(RegistCfmActivity.this, tips, 1000);

                            Intent i = getIntent();
                            i.putExtra("sendtime", sendTime);
                            setResult(RESULT_CANCELED);
                            finish();
                        }else{
                            Utils.handleResponseError(RegistCfmActivity.this,response);
                        }
                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                ToastUtil.showShort(RegistCfmActivity.this, VolleyErrorHelper.getMessage(error, RegistCfmActivity.this));

            }
        });
        request.setSendCookie();
        VolleyTool.getInstance(this).getRequestQueue().add(request);
    }

    private void successConfirm() {
        Intent intent = getIntent();
        intent.putExtra("sendtime", sendTime);
        setResult(RESULT_OK, intent);
        ToastUtil.show(this, "注册成功", 1000);
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
