package com.shawnway.nav.app.yylg.app;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.MainActivity;
import com.shawnway.nav.app.yylg.bean.CarItemBean;
import com.shawnway.nav.app.yylg.service.AutoLoginService;
import com.shawnway.nav.app.yylg.service.LocationService;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;


public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private volatile  ArrayList<CarItemBean> mData=new ArrayList<>();
    public LocationService locationService;
    private Vibrator mVibrator;
    private static  MyApplication myApplication;
    private static final String BUGLY_APPID = "70219d068d";
    public int cycle=20;
    public String outtradeno;
    //经纬度
    public double latitude;
    public double longitude;
    public String address;
    public boolean hasAddr=false;
    public void  setHasAddr(boolean hasAddr)
    {
        this.hasAddr=hasAddr;
    }
    public boolean getHasAddr()
    {
        return this.hasAddr;
    }




    public void setAddress(String address)
    {
        this.address=address;
    }
    public String getAddress()
    {
        return this.address;
    }
    public void setLatitude(double latitude)
    {
        this.latitude=latitude;
    }
    public double getLatitude()
    {
        return this.latitude;
    }
    public void setLongitude(double longitude)
    {
        this.longitude=longitude;
    }
    public  double  getLongitude()
    {
        return this.longitude;
    }
    public void setTradeNum(String tradeNum)
    {
        this.outtradeno=tradeNum;
    }
    public String getTradeNum()
    {
        return this.outtradeno;
    }
    public void  setCycle(int cycle)
    {
        this.cycle=cycle;
    }
    public int getCycle()
    {
        return this.cycle;
    }


    public static MyApplication getInstance() {
        return myApplication;
    }

    public ArrayList<CarItemBean> getData() {
        return mData;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        initImageLoader(getApplicationContext());
        initBDLocation();
        initBuglyUpgrade();
        startService(new Intent(myApplication, AutoLoginService.class));
        //initGalleryFinal();
        try {
            mData.clear();
            mData = new Gson().fromJson((String) Utils.getParam(this, "cart", "[]"), new TypeToken<ArrayList<CarItemBean>>() {
            }.getType());
        } catch (Exception e) {
//            mData = new ArrayList<>();
        }
        Log.d("MyApplication", "取出之前退出APP的购物车的数据" + mData);
    }

    private void initBDLocation() {
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());

    }


    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程的优先级，优先于主线程
                .denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置缓存文件的名字
                .discCacheFileCount(600)// 缓存文件的最大个数
                .tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
                .build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 初始化更新
     */
    private void initBuglyUpgrade() {
        Beta.autoInit = true;
        Beta.autoCheckUpgrade = true;
        Beta.initDelay =  1000;
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Beta.largeIconId = R.drawable.show_img;
        Beta.smallIconId = R.drawable.show_img;
        Beta.showInterruptedStrategy = true;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
        Bugly.init(getApplicationContext(), BUGLY_APPID, false);
    }

    @Override
    public void onTerminate() {
        Gson gson = new Gson();
        String cartData = gson.toJson(mData);
        Log.d("MyApplication", "在退出APP的时候保存购物车的信息：" + cartData);
        Utils.setParam(this, "cart", cartData);
        int realLength = (int) Utils.getParam(myApplication, "realLength", 0);
        if(realLength > 0){
            for(int i = 0;i<realLength;i++){
                String realValue = (String) Utils.getParam(myApplication, "real" + i, "");
                Utils.setParam(myApplication,realValue,"");
            }
        }
        super.onTerminate();
    }

}
