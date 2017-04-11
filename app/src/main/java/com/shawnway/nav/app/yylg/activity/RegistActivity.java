package com.shawnway.nav.app.yylg.activity;

import java.util.Calendar;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
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
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

public class RegistActivity extends FragmentActivity implements OnClickListener {
    private static final String TAG = "DetailActicity";
    private static final int REQUEST_ENTER_CUSTOMER_CODE = 0;
    private static final int RESULT_RESEND = 0;
    private final int PHONE_LENTH=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(RegistActivity.this);
        setContentView(R.layout.activity_register_infor);
        initToolbar();
    }

    private void sendValCode() {
        Log.d(TAG, "sending");
        final String phone = ((EditText) findViewById(R.id.regist_input_phone)).getText().toString().trim();
        String pwd = ((EditText) findViewById(R.id.regist_input_password)).getText().toString().trim();
        String invitation = ((EditText) findViewById(R.id.regist_input_invitation)).getText().toString().trim();
        if (isLegalNumber(phone, pwd)) {

            GsonRequest<ResponseGson> request = new GsonRequest<ResponseGson>(
                    Method.POST, Constants.VALIDATE_URL, ResponseGson.class,
                    this, new Listener<ResponseGson>() {

                @Override
                public void onResponse(ResponseGson response) {
                    HeaderGson header = response.getHeader();
                    String code = header.code;
                    if (code.equals(VolleyTool.SUCCESS)) {
                        success(phone);
                    } else if (code.equals(VolleyTool.VALIDATE_ERROR)) {
                        ToastUtil
                                .show(RegistActivity.this, header.error, 1000);
                    }
                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    String erromsg = VolleyErrorHelper.getMessage(error, RegistActivity.this);
                    if (erromsg != null)
                        ToastUtil.showShort( RegistActivity.this, erromsg);
                }
            });
            HashMap<String, String> map = new HashMap<>();
            map.put("cellphone", phone);
            map.put("password", pwd);
            map.put("invitationCode",invitation);
            request.setParams(map);
            request.setSendCookie();
            VolleyTool.getInstance(this).getRequestQueue().add(request);

        }

    }

    private boolean isLegalNumber(String phone, String pwd) {
        if (phone == null || phone.isEmpty()) {
            ToastUtil.showShort(this, getString(R.string.regist_alert_phone_not_empty),true);
            return false;
        }
        if (pwd == null || pwd.isEmpty()) {
            ToastUtil.showShort(this, getString(R.string.regist_alert_password_not_empty),true);
            return false;
        }
        if (phone.length()!=PHONE_LENTH){
            ToastUtil.showShort(this,getString(R.string.regist_alert_phone_not_legal),true);
            return  false;
        }
        if (!pwd.matches("[0-9A-Za-z]*")){
            ToastUtil.showShort(this,getString(R.string.regist_alert_pwd_not_legal,true));
            return  false;
        }
        if (pwd.length()<6){
            ToastUtil.showShort(this,getString(R.string.regist_alert_pwd_too_short),true);
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
            Log.d(TAG, "sendtime" + sendtime);
        }
        Intent intent = new Intent(RegistActivity.this,
                RegistCfmActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("sendtime", sendtime);
        startActivityForResult(intent,
                REQUEST_ENTER_CUSTOMER_CODE);
        ToastUtil
                .show(RegistActivity.this, getResources().getString(R.string.tips_validatecode_sended), 1000);
    }

    String lastphone;
    long sendtime;

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(
                R.string.title_activity_register));
        centerText.setVisibility(View.VISIBLE);

        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);

        TextView actionButton = (TextView) findViewById(R.id.top_back_text);
        actionButton.setText(getResources().getString(
                R.string.title_activity_login));
        actionButton.setVisibility(View.VISIBLE);

    }

    private void changeSendBtnStatus(CheckBox checkBox) {

        View send = findViewById(R.id.regist_send);

        if (checkBox.isChecked()) {
            Log.d(TAG, "is checked");
            send.setEnabled(true);
        } else {
            Log.d(TAG, "is unchecked");
            send.setEnabled(false);
        }

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
            case R.id.regist_agreement:
                Log.d(TAG, "agree is click");
                changeSendBtnStatus((CheckBox) v);
                break;
            case R.id.regist_send:
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
            case R.id.tv_protocol:
                ServiceProtocolActivity.getInstance(this,ServiceProtocolActivity.class);
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
