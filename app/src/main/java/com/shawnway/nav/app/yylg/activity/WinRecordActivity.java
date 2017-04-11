package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.phone_recharge.PhoneRechargeActivity;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.withdraw_deposit.WithDrawDepositActivity;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

/**
 * Created by Eiffel on 2015/11/19.
 */
public class WinRecordActivity extends FragmentActivity implements View.OnClickListener {

    public static void getInstance(Context context) {
        Intent goWinRecord = new Intent(context, WinRecordActivity.class);
        context.startActivity(goWinRecord);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(WinRecordActivity.this);
        setContentView(R.layout.activity_my_winrecord);
        initToolBar();
    }

    protected void initToolBar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_mwinrecord));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getSupportFragmentManager().findFragmentById(R.id.win_record_fragment).onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhoneRechargeActivity.REQUEST_RECHARGE_CODE && resultCode == RESULT_OK) {
            //充值完
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == WithDrawDepositActivity.REQUEST_WITHDRAW_CODE && resultCode == RESULT_OK) {
            //虚拟充值完
            setResult(RESULT_OK);
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