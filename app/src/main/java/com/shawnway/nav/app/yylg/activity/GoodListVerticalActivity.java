package com.shawnway.nav.app.yylg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.view.TabButton;

/**
 * Created by Eiffel on 2015/11/9.
 */
public class GoodListVerticalActivity extends FragmentActivity implements View.OnClickListener {


    private TabButton cartButton;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(GoodListVerticalActivity.this);
        setContentView(R.layout.activity_good_list_vertical);
        initToolbar();
        initReceiver();
    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Constants.ACTION_CART_CHANGE)) {
                    int cartSize = ((MyApplication) getApplication()).getData().size();
                    cartButton.setMessageNumber(cartSize);
                }


            }
        };
        IntentFilter filter=new IntentFilter(Constants.ACTION_CART_CHANGE);
        registerReceiver(receiver, filter);
    }

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_back_text);
        centerText.setText(getResources().getString(R.string.title_activity_renyimen));
        centerText.setVisibility(View.VISIBLE);

        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

        cartButton= (TabButton) findViewById(R.id.top_cart);
        cartButton.setVisibility(View.VISIBLE);
        cartButton.setOnClickListener(this);

    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

            switch (v.getId()) {
                case R.id.top_back:
                    finish();
                    break;
                case R.id.action_gray_common:
                    Intent i = new Intent(this, RegistActivity.class);
                    startActivity(i);
                    break;
                case R.id.top_cart:
                    MainActivity.getInstance(this,3);
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
