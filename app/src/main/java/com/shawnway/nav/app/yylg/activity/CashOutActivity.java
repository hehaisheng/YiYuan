package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.StringUtils;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.view.PayRadioGroup;
import com.shawnway.nav.app.yylg.view.PayRadioPurified;

/**
 * Created by Eiffel on 2016/3/28.
 *
 * CashOut 提现
 */
public class CashOutActivity extends MyActivity implements View.OnClickListener {

    private double commission;

    public static void getInstance(Context context,double restCommission) {
        Intent intent = new Intent(context, CashOutActivity.class);
        intent.putExtra("commission", restCommission);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(CashOutActivity.this);
        setContentView(R.layout.activity_cashout);
        setupView();
        initToolbar();

    }

    private void setupView() {
        commission=getIntent().getDoubleExtra("commission", 0);
        ((TextView)findViewById(R.id.tv_commission)).setText(getString(R.string.my_commission, String.valueOf(commission)));
    }

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_cashout));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);

        backButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                PayRadioGroup chooser = (PayRadioGroup) findViewById(R.id.genderGroup);
                String tag = chooser.getCheckedRadioButtonStringTag();//获取第几个提现方式
                String amount = Utils.getEditTextStr(findViewById(R.id.inputer_amount));//获取input里面的输入金额
                if (tag != null && checkAmount(amount))//选择了提现方式和，填写了输入金额(输入金额数字合法)，就***跳转到下一个界面***
                    CashOutFormActivity.getInstance(this, tag, Double.parseDouble(amount));
                else if (tag == null)
                    ToastUtil.showShort(this, getString(R.string.error_cashout_ways_notchoose, true));
                else if (StringUtils.isBlank(amount))
                    ToastUtil.showShort(this, getString(R.string.error_cashout_amount_amount_empty));
                else if(Double.parseDouble(amount)>commission){
                    ToastUtil.showShort(this,getString(R.string.error_cashout_amount_amount_overflow));
                }
                else
                    ToastUtil.showShort(this, getString(R.string.error_cashout_amount_not_hund));
                break;
            case R.id.top_back:
                finish();
                break;

        }
    }


    //检测输入金额是否违法
    private boolean checkAmount(String amount) {
        //空白
        if (StringUtils.isBlank(amount))
            return false;
        try {
            double amountValue = Double.parseDouble(amount);
            //非整百，0，大于可用佣金
            if (amountValue % 100 == 0 && amountValue != 0&&commission>amountValue)
                return true;

        } catch (ClassCastException e) {
            //非double型数据
            e.printStackTrace();
        }
        return false;
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
