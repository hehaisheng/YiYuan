package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;

import java.io.Serializable;

/**
 * Created by Eiffel on 2015/12/8.
 * <p/>
 * 包含有自定义的toolbar及可拓展的bottombar，核心内容为传入的fragment，可通过getInstance获取示例并传递参数给fragment
 * <p/>
 * 设计时主要是为了结合{@link com.shawnway.nav.app.yylg.fragment.ListFragment}
 * 进行使用，方便传入参数和放入碎片，同时也方便调用fragment的onActivityResult，因为遇到actvity中
 * 的fragment在activity不主动回调的情况下不调用onActivityResult的问题。
 * <p/>
 * bottombar可通过getBottomBar来填充，示例{@link CommentsActivity#onCreate(Bundle)}
 */
@Deprecated
/*
*  改用toolbar更方便维护
 */
public abstract class MyFragmentActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "MyFragmentActivity";


//    abstract protected Fragment setContentFragment();
/*abstract*/ protected Fragment setContentFragment(){return null;};
    abstract protected String getToolbarTitle();

    public static void getInstance(Context context, Class<?> clazz) {
        try {
            Intent intent = new Intent(context, clazz);

            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "argument 'clazz' should be an Activity class");
        }
    }

    public static void getInstance(Context context, Class<?> clazz, Bundle bundle) {
        try {
            Intent intent = new Intent(context, clazz);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "argument 'clazz' should be an Activity class");
        }
    }

    @Deprecated
    public static void getInstance(Context context, Class<?> clazz, Serializable bean) {
        try {
            Intent intent = new Intent(context, clazz);
            intent.putExtra("bean", bean);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "argument 'clazz' should be an Activity class");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment);
        initToolbar();
        initFragment();
    }

    private void initFragment() {

        Fragment fragment = setContentFragment();
        fragment.setArguments(getIntent().getExtras()); // FragmentActivity将点击的菜单列表标题传递给Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).commit();

    }

    protected ViewGroup getBottomView() {
        return (ViewGroup) findViewById(R.id.layout_bottomBar);
    }


    protected Fragment getContentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getToolbarTitle());
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

    @Override
    protected void onStop() {
        super.onStop();
        VolleyTool.getInstance(this).getRequestQueue().cancelAll(this);

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
