package com.shawnway.nav.app.yylg.activity;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.JoinBean;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.view.MyRecyclerView;

public class UserActivity extends FragmentActivity implements OnClickListener {
    private static final String TAG = "DetailActicity";
    private MyRecyclerView recyclerView;
    private ArrayList<JoinBean> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(UserActivity.this);
        setContentView(R.layout.activity_user_center);
    }

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_user_center));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

    }


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
