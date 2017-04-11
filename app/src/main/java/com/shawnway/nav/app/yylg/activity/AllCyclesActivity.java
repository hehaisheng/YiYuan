package com.shawnway.nav.app.yylg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.fragment.AllCyclesAnnounceFragment;
import com.shawnway.nav.app.yylg.fragment.AllCyclesCalculatingFragment;
import com.shawnway.nav.app.yylg.fragment.AllCyclesOpenFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.view.PagerSlidingTabStrip;
import com.shawnway.nav.app.yylg.view.TabButton;

import java.util.ArrayList;

@Deprecated
public class AllCyclesActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "AllCyclesActivity";
    String[] searchType = {Hook.ALL, Hook.HOT, Hook.NEW};
    Class<?>[] classes = {AllCyclesAnnounceFragment.class, AllCyclesOpenFragment.class, AllCyclesCalculatingFragment.class};
    int[] titles = {R.string.title_fragment_annoucced,
            R.string.title_fragment_open, R.string.title_fragment_calculating};
    private ArrayList<SubFragEntity> mFragmentList;
    private TabButton cartButton;
    private PagerSlidingTabStrip tabs;
    private BroadcastReceiver receiver;

    public static void getInstance(Context context, long proId) {
        Intent intent = new Intent(context, AllCyclesActivity.class);
        intent.putExtra("proid", proId);
        Log.d(TAG, "activity get id:" + proId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(AllCyclesActivity.this);
        setContentView(R.layout.fragment_navgation);
        initToolbar();
        initContent();
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
        IntentFilter filter = new IntentFilter(Constants.ACTION_CART_CHANGE);
        registerReceiver(receiver, filter);
    }

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_back_text);
        centerText.setText(getResources().getString(R.string.title_activity_good_list));
        centerText.setVisibility(View.VISIBLE);

        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

        cartButton = (TabButton) findViewById(R.id.top_cart);
        cartButton.setVisibility(View.VISIBLE);
        cartButton.setOnClickListener(this);

    }

    private void initContent() {
        addTab();
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setIndicatorStyle(PagerSlidingTabStrip.STYLE_RECTANGLE);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(pager);
        setTabsValue();
    }

    private void addTab() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
        }


        for (int i = 0; i < classes.length; i++) {
            try {
                mFragmentList.add(new SubFragEntity(searchType[i], getResources().getString(
                        titles[i]), (Fragment) classes[i].newInstance()));
            } catch (Exception e) {
                Log.e(TAG, "false to add fragment");
            }
        }

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
            case R.id.top_cart:
//                CartActivity.getInstance(this);
                MainActivity.getInstance(this,3);
            default:
                break;
        }
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // // 设置Tab是自动填充满屏幕的
        // tabs.setShouldExpand(true);
        // // 设置Tab的分割线是透明的
        // tabs.setDividerColor(Color.WHITE);
        // // tabs.setDividerColor(Color.BLACK);
        // // 设置Tab底部线的高度
        // tabs.setUnderlineHeight((int) TypedValue.applyDimension(
        // TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // // 设置Tab Indicator的高度
        // tabs.setIndicatorHeight((int) TypedValue.applyDimension(
        // TypedValue.COMPLEX_UNIT_DIP, 4, dm));// 4
        // // 设置Tab标题文字的大小
        // tabs.setTextSize((int) TypedValue.applyDimension(
        // TypedValue.COMPLEX_UNIT_SP, 16, dm)); // 16
        // // 设置Tab Indicator的颜色
        // tabs.setIndicatorColor(getResources().getColor(R.color.white));//
        // #45c01a
        // // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        // tabs.setSelectedTextColor(getResources().getColor(R.color.pink));//
        // #45c01a
        // // 取消点击Tab时的背景色
        // tabs.setTabBackground(R.color.pink);
        // tabs.setUnderlineColorResource(R.color.white);
        // 设置Tab是自动填充满屏幕的
        tabs.setBackgroundColor(getResources().getColor(R.color.appcolor));
        tabs.setShouldExpand(true);
        tabs.setFillTabBar(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // tabs.setDividerColor(Color.BLACK);
        // // 设置Tab底部线的高度
        // tabs.setUnderlineHeight((int) TypedValue.applyDimension(
        // TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // // 设置Tab Indicator的高度
        // tabs.setIndicatorHeight(1);// 4
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) (16)); // 16
        // 设置Tab标题文字的颜色
        tabs.setTextColor(getResources().getColor(R.color.white));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.white));// #45c01a
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(getResources().getColor(R.color.appcolor));// #45c01a
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    // FragmentPagerAdapter FragmentStatePagerAdapter //不能用FragmentPagerAdapter

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentList.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position).getFragment();
        }

    }

    class SubFragEntity {
        String searchby;
        String mTitle;
        Fragment mFragment;

        public SubFragEntity(String searchType, String title, Fragment fragment) {
            searchby = searchType;
            mTitle = title;
            mFragment = fragment;
        }

        public String getTitle() {
            return mTitle;
        }

        public Fragment getFragment() {
            mFragment.setArguments(getIntent().getExtras());
            return mFragment;
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
