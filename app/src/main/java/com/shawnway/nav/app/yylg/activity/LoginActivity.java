package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.fragment.LoginWtihPwdFrag;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;

public class LoginActivity extends FragmentActivity implements OnClickListener {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(LoginActivity.this);
        Constants.loginActivity = this;
        //设置了改参数后，改活动仅用于驱动前一活动的onActivityResult的SUCCESS方法
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.getBoolean("canView", true)) {
            setResult(RESULT_OK);
            finish();
        }
        setContentView(R.layout.activity_login);
        initToolbar();
        initFragment();

    }

    private void initFragment() {
        Fragment fragment = LoginWtihPwdFrag.getInstance(getIntent().getBooleanExtra("clear", false));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment).commit();

    }

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(
                R.string.title_activity_login));
        centerText.setVisibility(View.VISIBLE);

        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

        TextView actionButton = (TextView) findViewById(R.id.action_gray_common);
        actionButton.setText(getResources().getString(R.string.action_regist));
        actionButton.setTextSize(18);
        actionButton.setTextColor(Color.GRAY);
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setOnClickListener(this);

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.action_gray_common:
                Intent i = new Intent(this, RegistActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    public static void getInstance(Context context, int requestCode, boolean canView) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("canView", canView);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public static void getInstance(Activity context, int requestCode, boolean canView, boolean clearPassword) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("canView", canView);
        intent.putExtra("clear", clearPassword);
        context.startActivityForResult(intent, requestCode);
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
