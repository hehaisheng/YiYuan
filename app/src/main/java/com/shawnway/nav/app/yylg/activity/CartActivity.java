package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.shawnway.nav.app.yylg.R;

/**
 * Created by Eiffel on 11/6/2015.
 */
public class CartActivity extends FragmentActivity implements View.OnClickListener {

    public static  void getInstantce(Context context){
        Intent carIntent = new Intent(context, CartActivity.class);
        context.startActivity(carIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment);
        View view=fragment.getView().findViewById(R.id.top_back);
        view.setOnClickListener(this) ;
        view.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_back:
                finish();
        }
    }

    public static void getInstance(Context context) {
        Intent cartIntent = new Intent(context, CartActivity.class);
        context.startActivity(cartIntent);
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
