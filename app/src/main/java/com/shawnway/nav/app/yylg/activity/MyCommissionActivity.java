package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 佣金
 * Created by Eiffel on 2015/11/18.
 */
public class MyCommissionActivity extends Activity implements View.OnClickListener {

    private String mCommission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(MyCommissionActivity.this);
        setContentView(R.layout.activity_mycommission);
        initToolBar();
        initContent();
    }

    private void initContent() {
        VolleyTool.getInstance(this).sendGsonRequest(this, CommissionResponse.class, Constants.COMMISSION, new Response.Listener<CommissionResponse>() {
            @Override
            public void onResponse(CommissionResponse response) {
                if (!Utils.handleResponseError(MyCommissionActivity.this, response)) {
                    mCommission=response.getBody().commission;
                    ((TextView) findViewById(R.id.tv_commission)).setText(getString(R.string.coin, response.getBody().commission));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }


    protected void initToolBar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_mycommission));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case  R.id.top_back:
                finish();
                break;
            case R.id.bt_continue:
                JSONObject params=new JSONObject();
                try {
                    params.put("transferType","COMMISSION_TO_BALANCE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                VolleyTool.getInstance(this).sendGsonPostRequest(this,ResponseGson.class, Constants.COMMISION_TO_BALANCE, params.toString(), new Response.Listener<ResponseGson>() {
                    @Override
                    public void onResponse(ResponseGson responseGson) {
                        if (!Utils.handleResponseError(MyCommissionActivity.this,responseGson)){
                            ToastUtil.showShort(MyCommissionActivity.this,"佣金转换成功");
                            MainActivity.getInstance(MyCommissionActivity.this, 0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.showShort(MyCommissionActivity.this,"转入失败");
                        //byte[] htmlBodyBytes = error.networkResponse.data;
                        byte[] htmlBodyBytes = volleyError.networkResponse.data;
                        Log.e("MyCom", new String(htmlBodyBytes), volleyError);

                    }
                });

                break;
            case R.id.bt_get_cash://提现
                try {
                    if (mCommission != null) {
                        CashOutActivity.getInstance(this, Double.parseDouble(mCommission));
                    }
                }catch (ClassCastException e){
                    Log.e(getClass().getSimpleName(),"commission can't convert to long");
                    CashOutActivity.getInstance(this, 0);

                }
                break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== Constants.ACTION_REQUEST_LOGIN&&resultCode == Activity.RESULT_OK) {
            initContent();
        } else if (requestCode== Constants.ACTION_REQUEST_LOGIN){
           finish();
        }
    }

    private class CommissionResponse extends ResponseGson<CommissionBody>{
    }

    private class CommissionBody {
        String balance;
        String point;
        String commission;
        String purchaseAmount;
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
