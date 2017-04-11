package com.shawnway.nav.app.yylg.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.AssertResponseWraper;

import java.text.DecimalFormat;

/**
 * Created by Eiffel on 2015/11/26.
 */
public class Bank extends LinearLayout implements View.OnClickListener {
    private static final int SIMPLE_MODE = 0;
    private static final int RICH_MOE = 1;
    private static final String TAG = "Bank";
    private final Context context;
    private String payType;
    private PayRadioGroup commonBank;
    private PayRadioGroup otherBank;
    private int bankMode;

    public Bank(Context context) {
        this(context, null);

    }

    public Bank(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Bank(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Bank);

        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.Bank_bankStyle:
                    int mode = a.getInt(attr, SIMPLE_MODE);
                    bankMode = mode;
                    Log.d(TAG, "bank mode is:" + bankMode);
                    break;
            }

        }

        a.recycle();


        this.context = context;


        initLayout();
        setOnSelected();
        initOtherWay();
        disableNouseWay();
//        setValue();
        switchMode();

    }

    private void disableNouseWay() {

    }


    private void initLayout() {
        LayoutInflater.from(context).inflate(R.layout.layout_bank, this, true);
        commonBank = ((PayRadioGroup) findViewById(R.id.genderGroup));
        otherBank = ((PayRadioGroup) findViewById(R.id.genderGroup2));
    }

    public void setAssert(AssertResponseWraper.BankBody b, String total) {
        DecimalFormat df1 = new DecimalFormat("0.00");
        Float totalValue=Float.valueOf(total);
        Float pointValue = Float.valueOf(b.point) / 1000;
        Float balanceValue=Float.valueOf(b.balance);


        PayRadioPurified point = (PayRadioPurified) findViewById(R.id.pay_point);
        point.setTextDesc(getContext().getString(R.string.pay_point_desc, b.point, df1.format(pointValue)));
        point.setCheckAble(pointValue > totalValue);
        PayRadioPurified balance = (PayRadioPurified) findViewById(R.id.pay_balance);
        balance.setTextDesc(getContext().getString(R.string.pay_balance_desc, b.balance));
        balance.setCheckAble(balanceValue > totalValue);
        if(pointValue >= totalValue){
            point.setChecked(true);
        }else{
            balance.setChecked(true);
        }
//        balance.setChecked(true);//设置选中余额，就是不知道点击福分的时候会不会清除选中的状态，试试
        TextView tot = (TextView) findViewById(R.id.amount);
        tot.setText(getContext().getString(R.string.pay_tot_amount, total));
        //TODO:隐藏了通联支付，以后可能要做
        PayRadioPurified bankCard = (PayRadioPurified) findViewById(R.id.p3);
        bankCard.setCheckAble(true);
        PayRadioPurified webChat = (PayRadioPurified)findViewById(R.id.pay_wechat);
        webChat.setCheckAble(true);
    }


    private void switchMode() {
        if (bankMode == SIMPLE_MODE) {
            setCommonBankVisibility(View.GONE);
            setChargeBankVisibility(View.VISIBLE);
        }
        else if(bankMode == RICH_MOE){
            setCommonBankVisibility(View.VISIBLE);
            setChargeBankVisibility(View.GONE);
        }
    }

    private void setCommonBankVisibility(int visibility) {
        findViewById(R.id.genderGroup).setVisibility(visibility);
    }

    private void setChargeBankVisibility(int visibility) {
        findViewById(R.id.genderGroup2).setVisibility(visibility);
        findViewById(R.id.pay_otherway).setVisibility(View.GONE);
    }

    private void initOtherWay() {
        findViewById(R.id.pay_otherway).setOnClickListener(this);
    }


    private void setOnSelected() {
        commonBank.setOnCheckedChangeListener(new PayRadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(PayRadioGroup group, int checkedId) {
                Log.d(TAG, "checked change commonBank:" + checkedId);
                if (checkedId != PayRadioGroup.NONCHECKED) {
                    otherBank.clearCheck();
                    Log.d(TAG, "clear otherBank");
                }
            }
        });
        otherBank.setOnCheckedChangeListener(new PayRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(PayRadioGroup group, int checkedId) {
                Log.d(TAG, "checked change otherBank:" + checkedId);
                if (checkedId != PayRadioGroup.NONCHECKED) {
                    commonBank.clearCheck();
                    Log.d(TAG, "clear commonBank");
                }
            }
        });
    }

    public void setPayType(String payType){
        this.payType = payType;
    }


    public String getPayType() {
        int radioButtonId = commonBank.getCheckedRadioButtonId();
        try {
            if (radioButtonId != PayRadioGroup.NONCHECKED) {
                PayRadioPurified rl = (PayRadioPurified) findViewById(radioButtonId);
                Log.d(TAG, "bank:" + rl.getTag());
                return (String) rl.getTag();
            } else {
                int radioButtonId2 = otherBank.getCheckedRadioButtonId();
                if (radioButtonId2 != PayRadioGroup.NONCHECKED) {
                    PayRadioPurified rl = (PayRadioPurified) findViewById(radioButtonId2);
                    return (String) rl.getTag();
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_otherway:
                int bankVis = otherBank.getVisibility();
                ImageView arrow = (ImageView) v.findViewById(R.id.orderArr);
                arrow.setImageDrawable(getResources().getDrawable(bankVis != View.VISIBLE ? R.drawable.ic_arr_down_gray : R.drawable.ic_arr_up_gray));
                otherBank.setVisibility(bankVis != View.VISIBLE ? View.VISIBLE : View.GONE);
        }
    }

}
