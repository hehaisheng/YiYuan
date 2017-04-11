package com.shawnway.nav.app.yylg.mvp.user.user_buy_record;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.base.BaseActivity;
import com.shawnway.nav.app.yylg.view.TabButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Kevin on 2017/1/6
 */

public class MyBuyRecordsActivity extends BaseActivity {
    @BindView(R.id.top_back)
    ImageButton topBack;
    @BindView(R.id.top_back_text)
    TextView topBackText;
    @BindView(R.id.top_text_center)
    TextView topTextCenter;
    @BindView(R.id.top_cart)
    TabButton topCart;
    @BindView(R.id.action_edit)
    TextView actionEdit;
    @BindView(R.id.action_gray_common)
    TextView actionGrayCommon;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.tab_all)
    TextView tabAll;
    @BindView(R.id.tab_annount)
    TextView tabAnnount;
    @BindView(R.id.tab_open)
    TextView tabOpen;
    @BindView(R.id.framelayout_buyrecords)
    FrameLayout framelayoutBuyrecords;


    private String DRAWSTATUS_ALL = "";
    private String DRAWSTATUS_OPEN = "OPEN";
    private String DRAWSTATUS_ANNOUNT = "ANNOUNCED";
    private MyBuyRecordsFragment allBuyRecordsFragment;
    private MyBuyRecordsFragment annountBuyRecordsFragment;
    private MyBuyRecordsFragment openBuyRecordsFragment;

    private final int TAB_ALL = 0;
    private final int TAB_ANNOUNT = 1;
    private final int TAB_OPEN = 2;


    @Override
    public int getLayout() {
        return R.layout.activity_buyrecords;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MyBuyRecordsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void initDataAndEvents() {
        initToolbar();
    }



    @Override
    protected void initDataAndEventsWithState(@Nullable Bundle savedInstanceState) {
        initBuyRecordsFrgment(savedInstanceState);
    }

    /**
     * 初始化Fragment
     */
    private void initBuyRecordsFrgment(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (savedInstanceState == null) {
            allBuyRecordsFragment = MyBuyRecordsFragment.newInstance(DRAWSTATUS_ALL);
            annountBuyRecordsFragment = MyBuyRecordsFragment.newInstance(DRAWSTATUS_ANNOUNT);
            openBuyRecordsFragment = MyBuyRecordsFragment.newInstance(DRAWSTATUS_OPEN);
            fragmentTransaction
                    .add(R.id.framelayout_buyrecords,
                            allBuyRecordsFragment,
                            "allBuyRecordsFragment")
                    .add(R.id.framelayout_buyrecords,
                            annountBuyRecordsFragment,
                            "annountBuyRecordsFragment")
                    .add(R.id.framelayout_buyrecords,
                            openBuyRecordsFragment,
                            "openBuyRecordsFragment")
                    .commit();
        } else {
            allBuyRecordsFragment = (MyBuyRecordsFragment) fragmentManager.findFragmentByTag("allBuyRecordsFragment");
            annountBuyRecordsFragment = (MyBuyRecordsFragment) fragmentManager.findFragmentByTag("annountBuyRecordsFragment");
            openBuyRecordsFragment = (MyBuyRecordsFragment) fragmentManager.findFragmentByTag("openBuyRecordsFragment");
        }
        setSelectTabFragment(TAB_ALL);
    }

    private void setSelectTabFragment(int TAB_TYPE) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        switch (TAB_TYPE) {
            case TAB_ALL:
                fragmentTransaction.show(allBuyRecordsFragment)
                        .hide(annountBuyRecordsFragment)
                        .hide(openBuyRecordsFragment);
                setSelectTabColor(tabAll);
                setUnSelectTabColor(tabAnnount, tabOpen);

                break;
            case TAB_ANNOUNT:
                fragmentTransaction.show(annountBuyRecordsFragment)
                        .hide(allBuyRecordsFragment)
                        .hide(openBuyRecordsFragment);
                setSelectTabColor(tabAnnount);
                setUnSelectTabColor(tabAll, tabOpen);

                break;
            case TAB_OPEN:
                fragmentTransaction.show(openBuyRecordsFragment)
                        .hide(allBuyRecordsFragment)
                        .hide(annountBuyRecordsFragment);
                setSelectTabColor(tabOpen);
                setUnSelectTabColor(tabAnnount, tabAll);

                break;
        }
        fragmentTransaction.commit();
    }

    /**
     * 设置选中Tab的颜色
     *
     * @param tab
     */
    private void setSelectTabColor(TextView tab) {
        tab.setTextColor(getResources().getColor(R.color.appcolor));
    }

    private void setUnSelectTabColor(TextView... tab) {
        for (TextView textView : tab) {
            textView.setTextColor(getResources().getColor(R.color.text_primary));
        }
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        topTextCenter.setText("购买记录");
        setVisiableView(toolbar, topBack, topTextCenter);
    }

    @OnClick({R.id.top_back, R.id.tab_all, R.id.tab_annount, R.id.tab_open})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.tab_all:
                setSelectTabFragment(TAB_ALL);
                break;
            case R.id.tab_annount:
                setSelectTabFragment(TAB_ANNOUNT);
                break;
            case R.id.tab_open:
                setSelectTabFragment(TAB_OPEN);
                break;
        }
    }
}
