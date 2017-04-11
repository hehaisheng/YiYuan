package com.shawnway.nav.app.yylg.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.adapter.PagerAdapter;
import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.fragment.AllGoodFragment;
import com.shawnway.nav.app.yylg.fragment.CarFragment;
import com.shawnway.nav.app.yylg.fragment.HomeWrapperFragment;
import com.shawnway.nav.app.yylg.fragment.LastAnnounceFragment;
import com.shawnway.nav.app.yylg.fragment.UserFragment;
import com.shawnway.nav.app.yylg.style.BackPressStyle;
import com.shawnway.nav.app.yylg.testbaidu.MyLocation;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.view.MyViewPager;
import com.shawnway.nav.app.yylg.view.TabButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int CARFRAGMENT_POSITION = 3;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;
    private List<TabButton> mTabButtonList = new ArrayList<TabButton>();
    Class<?>[] classes = {HomeWrapperFragment.class, AllGoodFragment.class, LastAnnounceFragment.class,
            CarFragment.class, UserFragment.class};
    private BroadcastReceiver receiver;
    private int pageIndex = 0;
    private Handler mHandler = new Handler();
    public MyLocation myLocation;

    public static void getInstance(Context context, int index) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("index", index);
        context.startActivity(i);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(this);
        Constants.mainActivity = this;
//        AutoLoginUtils.startPollingService(this, AutoLoginService.class,Constants.ACTION_AUTOLOGIN);
        setContentView(R.layout.activity_main_cutomer_yy);
        initView();
        pagerAdapter();
        initEvent();
        pageIndex = getIntent().getIntExtra("index", 0);
        mViewPager.setCurrentItem(pageIndex);//这只选中第几个viewpager
        changeAlpha(pageIndex);//设置选中的btn图标

        initReceiver();
        updateCartMsgNum();

        getLocation();

    }


    /**
     * 设置了singleTask的启动回调方法
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(final Intent intent) {
        pageIndex = intent.getIntExtra("index", 0);
        changeAlpha(pageIndex);//设置选中的btn图标
        mViewPager.setCurrentItem(pageIndex,false);//这只选中第几个viewpager
    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {

                    if (intent.getAction().equals(Constants.ACTION_CART_NUM_CHANGE) || intent.getAction().equals(Constants.ACTION_CART_CHANGE)) {
                        updateCartMsgNum();
                    } else if (intent.getAction().equals(Constants.ACTION_CHANGE_HOME_INDEX)) {
                        int index2change = intent.getIntExtra("index", 0);
                        changeAlpha(index2change);
                        mViewPager.setCurrentItem(index2change);
                    }
                    else if(intent.getAction().equals("com.my.address"))
                    {
                        boolean hasAddr=intent.getBooleanExtra("hasAddr",false);
                        if(hasAddr)
                        {

                            Utils.setParam(MainActivity.this, "location", MyApplication.getInstance().getAddress());
                            //Toast.makeText(MainActivity.this,MyApplication.getInstance().getAddress(),Toast.LENGTH_SHORT).show();
                            myLocation.stop();
                        }
                        else
                        {

                            myLocation.start();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "false to handle broadcast:" + e.getLocalizedMessage());
                }

            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_CHANGE_HOME_INDEX);
        filter.addAction(Constants.ACTION_CART_NUM_CHANGE);
        filter.addAction(Constants.ACTION_CART_CHANGE);
        filter.addAction("com.my.address");
       // filter.addAction(Constants.ACTION_UPDATE_BALANCE);
        registerReceiver(receiver, filter);

    }

    private void updateCartMsgNum() {

        int cartSize = ((MyApplication) getApplication()).getData().size();
        mTabButtonList.get(CARFRAGMENT_POSITION).setMessageNumber(cartSize);
    }


    private void initView() {
        mViewPager = (MyViewPager) findViewById(R.id.viewpager);

        mTabButtonList.add((TabButton) findViewById(R.id.tab_first));
        mTabButtonList.add((TabButton) findViewById(R.id.tab_second));
        mTabButtonList.add((TabButton) findViewById(R.id.tab_third));
        mTabButtonList.add((TabButton) findViewById(R.id.tab_fourth));
        mTabButtonList.add((TabButton) findViewById(R.id.tab_five));
        mTabButtonList.get(0).setAlpha(1.0f);

    }

    private void pagerAdapter() {
        mFragmentList = new ArrayList<Fragment>();
        for (int i = 0; i < classes.length; i++) {
            try {
                mFragmentList.add(((Fragment) classes[i].newInstance()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(adapter);
    }

    private void initEvent() {
        for (int i = 0; i < mTabButtonList.size(); i++) {
            mTabButtonList.get(i).setOnClickListener(this);
            mTabButtonList.get(i).setTag(i);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffsetPixels != 0) {
                    mTabButtonList.get(position).setAlpha(1 - positionOffset);
                    mTabButtonList.get(position + 1).setAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == CARFRAGMENT_POSITION) {
                    PullRefreshLayout.OnRefreshListener pull = (PullRefreshLayout.OnRefreshListener) mFragmentList.get(CARFRAGMENT_POSITION);
                    if (pull == null) {
                        return;
                    }
                    pull.onRefresh();
                } else {
                    PullRefreshLayout.OnRefreshListener pull = (PullRefreshLayout.OnRefreshListener) mFragmentList.get(position);
                    if (pull == null) {
                        return;
                    }
                    pull.onRefresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void saveCart2Local() {

        Gson gson = new Gson();
        String cartData = gson.toJson(((MyApplication) getApplication()).getData());
        Log.d("MyApplication", "teminating----saved data:" + cartData);
        Utils.setParam(this, "cart", cartData);
        SharedPreferences sp = getSharedPreferences("share_date", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("cart",cartData);
        editor.apply();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
//        AutoLoginUtils.stopPollingService(this,AutoLoginService.class,Constants.ACTION_AUTOLOGIN);
        saveCart2Local();
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        //该界面不可见时，保存数据在本地
       saveCart2Local();
        super.onPause();
    }


    private BackPressStyle backPressStyle = new BackPressStyle();
    long timenum=System.currentTimeMillis();


    @Override
    public void onBackPressed() {
        backPressStyle.onBackPressed(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : mFragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

        int number = (Integer) v.getTag();
        changeAlpha(number);
        mViewPager.setCurrentItem(number, false);
        PullRefreshLayout.OnRefreshListener pull = (PullRefreshLayout.OnRefreshListener) mFragmentList.get(number);
        if (pull == null) {
            return;
        }
        pull.onRefresh();

    }


    public void changeAlpha(final int number) {
        for (int i = 0; i < mTabButtonList.size(); i++) {
            mTabButtonList.get(i).setAlpha(number == i ? 1.0f : 0.0f);
        }
    }


    //若发现
    @TargetApi(Build.VERSION_CODES.M)
    private void getLocation() {
        Boolean havePermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            havePermission = (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
               checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
        }
        if(havePermission)
        {
            myLocation=new MyLocation(MainActivity.this);
            myLocation.start();
        }
        else
        {
            getPersimmions();
        }


    }


    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//            }
//            /*
//			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
//			 */
//            // 读写权限
//            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
//            }
//            // 读取电话状态权限
//            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
//                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
//            }

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
           if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    private final int SDK_PERMISSION_REQUEST = 127;


    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SDK_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                getLocation();
        }
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

