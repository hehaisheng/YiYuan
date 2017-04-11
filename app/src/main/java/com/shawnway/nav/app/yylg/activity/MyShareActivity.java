package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.fragment.MyShareFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

/**
 * Created by Eiffel on 2015/12/7.
 */
public class MyShareActivity extends FragmentActivity implements View.OnClickListener {

    public static void getInstance(Context context) {
        Intent intent = new Intent(context, MyShareActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(MyShareActivity.this);
        setContentView(R.layout.activity_with_fragment);
        initToolbar();
        initFragment();
    }

    private void initFragment() {

        Fragment fragment = new MyShareFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).commit();

    }


    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_myshare));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getSupportFragmentManager().findFragmentById(R.id.content_frame).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
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
