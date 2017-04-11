package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.adapter.FindTabAdapter;
import com.shawnway.nav.app.yylg.bean.CustomerInfoWrapper;
import com.shawnway.nav.app.yylg.fragment.BuyRecordFragment;
import com.shawnway.nav.app.yylg.fragment.CusShareListFragment;
import com.shawnway.nav.app.yylg.fragment.EarnRecordFragment;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.GlideUtils;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.view.PagerSlidingTabStrip;
import com.thuongnh.zprogresshud.ZProgressHUD;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Eiffel on 2016/3/20.
 */
public class CustomInfoActivity extends AppCompatActivity {

    private static final String EXTRA_IMAGE = "http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E5%B0%8F%E7%B1%B3%E6%89%8B%E6%9C%BA%28MIUI%294%2F2016022715413700344_600x600.PNG";
    private static final String EXTRA_TITLE = "com.antonioleiva.materializeyourapp.extraTitle";
    private static final String TAG = "CustomInfoActivity";
    private PagerSlidingTabStrip tabLayout;
    private FindTabAdapter adapter;
    public ViewPager viewPager;
    private int customerId;
    private String customName;
    boolean locked = true;
    private String buyNum;
    private ZProgressHUD loading;

    private void lock() {
        locked = true;
    }

    private void unlock() {
        locked = false;
    }

    public static void getInstance(Context context, int customerID) {
        Intent intent = new Intent(context, CustomInfoActivity.class);
        intent.putExtra("id", customerID);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(CustomInfoActivity.this);
        setContentView(R.layout.activity_customer_info);
        loading = ZProgressHUD.getInstance(this).setMessage("加载中");
        loading.show();
        customerId = getIntent().getIntExtra("id", -1);//得到参与者的id，这个必须在切换页面之前赋值
        viewPager = (ViewPager) findViewById(R.id.pager);
        if (viewPager != null) {
            setupViewPager(viewPager);//切换页面
        }
        tabLayout = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabLayout.setShouldExpand(true);
        tabLayout.setViewPager(viewPager);

        refresh();
    }

    private void refresh() {

        lock();
        if (!Constants.DEBUG) {
            //放假数据,将正常环境的步骤复制过来，然后填充数据
            ((TextView) findViewById(R.id.user_id)).setText("13802520106");
            //更新tab栏的记录数量
            if (viewPager == null)
                setupViewPager(viewPager);
            Random ran = new Random();
            FindTabAdapter _adapter = (FindTabAdapter) viewPager.getAdapter();
            _adapter.updateTitle(0, getString(R.string.customer_info_bought, "" + (50 - ran.nextInt(30))));
            _adapter.updateTitle(1, getString(R.string.customer_info_earn, "" + (50 - ran.nextInt(45))));
            //触发PagerSlidingTabStrip的更新
            tabLayout.setViewPager(viewPager);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("customerID", String.valueOf(customerId));
            //加载头部数据
            VolleyTool.getInstance(this).sendGsonRequest(this, CustomerInfoWrapper.class,
                    Constants.CUSTOMER_INFO, params, new Response.Listener<CustomerInfoWrapper>() {
                        @Override
                        public void onResponse(CustomerInfoWrapper response) {
                            if (!Utils.handleResponseError(false, CustomInfoActivity.this, response)) {
                                customName = response.getBody().customName;
                                ((TextView) findViewById(R.id.user_id)).setText(customName);
                                setUserImage(response.getBody().userImg);
                                //更新tab栏的记录数量
                                if (viewPager == null)
                                    setupViewPager(viewPager);
                                FindTabAdapter _adapter = (FindTabAdapter) viewPager.getAdapter();
                                _adapter.updateTitle(0, getString(R.string.customer_info_bought, response.getBody().buyRecAmount));
                                _adapter.updateTitle(1, getString(R.string.customer_info_earn, response.getBody().earnAmount));
                                //触发PagerSlidingTabStrip的更新
                                tabLayout.setViewPager(viewPager);
                                buyNum = response.getBody().buyRecAmount;
                                unlock();
                                setupCollapToolBar(customName);
                                loading.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            loading.dismiss();
                        }
                    });
        }
    }

    /**
     * 设置用户头像
     */
    private void setUserImage(String image_url) {
        CircleImageView userImage = (CircleImageView) findViewById(R.id.iv_avatar);
        if (TextUtils.isEmpty(image_url)) {
            GlideUtils.loadImage(CustomInfoActivity.this, R.drawable.portrait, userImage);
        } else GlideUtils.loadImage(CustomInfoActivity.this, image_url, userImage);
    }

    private void setupCollapToolBar(String name) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_top_back_white);
        if (!locked) {

            String itemTitle = getIntent().getStringExtra(EXTRA_TITLE);
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitleEnabled(true);

            collapsingToolbarLayout.setTitle(name);
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        if (adapter == null)
            adapter = new FindTabAdapter(getSupportFragmentManager());
        adapter.addFragment(BuyRecordFragment.getInstance(customerId, buyNum), getString(R.string.customer_info_bought));
        adapter.addFragment(EarnRecordFragment.getInstance(customerId), getString(R.string.customer_info_earn));
        Map<String, String> params = new HashMap<>();
        params.put("customerId", String.valueOf(customerId));
        adapter.addFragment(CusShareListFragment.getInstance(customerId), "晒单");
        viewPager.setAdapter(adapter);
    }

//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config = new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config, res.getDisplayMetrics());
//        return res;
//    }

}
