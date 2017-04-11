package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.SupportedBank;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;
import com.shawnway.nav.app.yylg.view.ClearEditView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2016/3/28.
 */
public class CashOutFormActivity extends MyActivity implements View.OnClickListener {

    private static final String TAG = "CashOutFormActivity";
    private String mCashOutWay;
    private String mCashAmount;
    private long mPosition;

    public static void getInstance(Context context, String cashoutWay,double amount) {
        Intent intent = new Intent(context, CashOutFormActivity.class);
        intent.putExtra("way", cashoutWay);
        intent.putExtra("amount", String.valueOf(amount));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(CashOutFormActivity.this);
        setContentView(R.layout.activity_cashoutform);
        mCashOutWay = getIntent().getStringExtra("way");
        Log.d(TAG,"从CashOut中得到的提现方式：" + mCashOutWay);
        mCashAmount=getIntent().getStringExtra("amount");
        initToolbar();
        setupView();

    }

    private void setupView() {
        ((TextView)findViewById(R.id.tv_amount)).setText(getString(R.string.cashout_amount, mCashAmount));
        switch (mCashOutWay) {
            case "WECHAT":
                updatePayWayOption(R.drawable.icon_wx_logo, R.string.wechat);
                updateAccountInputer(getString(R.string.co_account_wx),getString(R.string.hint_co_account_wx));
                break;
            case "ALIPAY":
                updatePayWayOption(R.drawable.ic_alipay_wap, R.string.alipay);
                updateAccountInputer(getString(R.string.co_account_alipay), getString(R.string.hint_co_account_alipay));

                break;
            case "ALLBANK":
                updateToSipnner();
                updateAccountInputer(getString(R.string.co_account_allbank), getString(R.string.hint_co_account_allbank));

                break;

        }
    }

    private void updateAccountInputer(String label, String hint) {
        ((TextView)findViewById(R.id.label_account)).setText(label);
        ((EditText)findViewById(R.id.inputer_account)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(21)});
    }

    private void updateToSipnner() {
        final Spinner gradeSpinner = (Spinner) findViewById(R.id.spinner);
        gradeSpinner.setVisibility(View.VISIBLE);
        ArrayList<SupportedBank> bankList = SupportedBank.getSupportedCashOutBank();
        MySpinnerAdapter adapter = new MySpinnerAdapter(this, bankList);
        gradeSpinner.setAdapter(adapter);
        findViewById(R.id.wraper_option).setVisibility(View.GONE);
        //显示开户支行和持卡人
        findViewById(R.id.option_account_owner).setVisibility(View.VISIBLE);
        findViewById(R.id.option_account_bunch).setVisibility(View.VISIBLE);
        //显示持卡人电话号码
        findViewById(R.id.option_account_phone).setVisibility(View.VISIBLE);
    }

    private void updatePayWayOption(int iconRsc, int titleId) {
        findViewById(R.id.spinner).setVisibility(View.GONE);
        findViewById(R.id.wraper_option).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.pay_way)).setText(getString(titleId));
        ((ImageView) findViewById(R.id.pay_icon)).setImageResource(iconRsc);

    }

    private void summit() {
        final Map<String,String>params=new HashMap<>();
        String input_bank = Utils.getEditTextStr(findViewById(R.id.inputer_account));//银行账号
        Long num = 0l;
        Long CardLength = 100000000000000l;
        if(!input_bank.equals("")){
            try{
                num = Long.parseLong(input_bank);
            }catch (Exception e){
                Log.d(TAG, "输入的银行账号格式上有点问题。。。");
                e.printStackTrace();
            }
        }
        Log.d(TAG, "num:"+num+",CarLength:"+CardLength);
        String owner = Utils.getEditTextStr(findViewById(R.id.inputer_owner));//持卡人姓名
        String bunch = Utils.getEditTextStr(findViewById(R.id.inputer_bunch));//开户网点
        String phone = Utils.getEditTextStr(findViewById(R.id.inputer_phone));//持卡人电话
        if(num<CardLength){
            ToastUtil.showShort(this,"请重新输入您的账号");
            return;
        }
        if(owner.length()<=1){
            ToastUtil.showShort(this,"请重输入持卡人姓名");
            return;
        }
        if(bunch.length()<=1){
            ToastUtil.showShort(this,"请重新输入开户网点");
            return;
        }
        if(phone.length()<=1){
            ToastUtil.showShort(this,"请重输入持卡人电话号码");
            return;
        }

        switch (mCashOutWay){
            case "WECHAT":
            case "ALIPAY":
                params.put("withdrawType",mCashOutWay);
                break;
            case "ALLBANK":
                //TODO:要将持卡人姓名和电话传递给后台
                params.put("ownerName",owner);
                params.put("cellphone",phone);
                params.put("blankBranch", bunch);
                if(mPosition == 0){
                    ToastUtil.showShort(this,"请选择您要提现的银行卡",true);
                    return;
                }else if(mPosition == 1){
                    params.put("blankName","工商银行");
                    params.put("withdrawType","ICBC");
                }else if(mPosition == 2){
                    params.put("blankName","建设银行");
                    params.put("withdrawType","CCB");
                }else if(mPosition == 3){
                    params.put("blankName","农业银行");
                    params.put("withdrawType","ABC");

                }else if(mPosition == 4){
                    params.put("blankName","中国光大银行");
                    params.put("withdrawType","CEB");

                }else if(mPosition == 5){
                    params.put("blankName","华夏银行");
                    params.put("withdrawType","HXB");

                }else if(mPosition == 6){
                    params.put("blankName","中国邮政储蓄银行");
                    params.put("withdrawType","PSBC");

                }else if(mPosition == 7){
                    params.put("blankName","中国民生银行");
                    params.put("withdrawType","CMBC");

                }else if(mPosition == 8){
                    params.put("blankName","浦发银行");
                    params.put("withdrawType","SPDB");

                }else if(mPosition == 9){
                    params.put("blankName","兴业银行");
                    params.put("withdrawType","CIB");

                }else if(mPosition == 10){
                    params.put("blankName","招商银行");
                    params.put("withdrawType","CMB");

                }else if(mPosition == 11){
                    params.put("blankName","中国银行");
                    params.put("withdrawType","BC");

                }else if(mPosition == 12){
                    params.put("blankName","平安银行");
                    params.put("withdrawType","PAB");
                }
                break;
        }
        params.put("withdrawFrom","COMMISSION");
        params.put("amount",mCashAmount);
        params.put("account",Utils.getEditTextStr(findViewById(R.id.inputer_account)));

        VolleyTool.getInstance(this).sendGsonPostRequest(this, ResponseGson.class, Constants.CASHOUT, params, new Response.Listener<ResponseGson>() {
            @Override
            public void onResponse(ResponseGson responseGson) {
                    if (!Utils.handleResponseError(CashOutFormActivity.this,responseGson)){
                        Log.d(TAG, "提现时候的参数："+params);
                        ToastUtil.show(getApplicationContext(), getString(R.string.cashout_success),3*1000);
                        MainActivity.getInstance(CashOutFormActivity.this,4);
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showShort(CashOutFormActivity.this, VolleyErrorHelper.getMessage(volleyError,CashOutFormActivity.this));
            }
        });
    }


    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        String title = null;
        switch (mCashOutWay) {
            case "WECHAT":
                title = getString(R.string.title_activity_cashoform_wechat);
                break;
            case "ALIPAY":
                title = getString(R.string.title_activity_cashoform_alipay);
                break;
            case "ALLBANK":
                title = getString(R.string.title_activity_cashoform_allbank);
                break;
        }
        if(title!=null){
            centerText.setText(title);
        }else{
            centerText.setText(getResources().getString(R.string.title_activity_cashout));
        }
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
            case R.id.btn_next:
                summit();
                break;
        }
    }

    public class MySpinnerAdapter extends BaseAdapter {
        private final ArrayList<SupportedBank> mList;
        private final Context mContext;

        public MySpinnerAdapter(Context context, ArrayList<SupportedBank> supportedCashOutBank) {
            mList=supportedCashOutBank;
            mContext=context;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public SupportedBank getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            mPosition = position;
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
            convertView=_LayoutInflater.inflate(R.layout.spinner_item_layout, null);
            if(convertView!=null) {
                ImageView label = (ImageView) convertView
                        .findViewById(R.id.spinner_item_label);
                ImageView check = (ImageView) convertView
                        .findViewById(R.id.spinner_item_checked_image);
                label.setImageResource(getItem(position).getSrc());
            }
            return convertView;
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
