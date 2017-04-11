package com.shawnway.nav.app.yylg.testbaidu;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.shawnway.nav.app.yylg.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-03-27.
 */

public class MyLocation {


    public LocationClient mLocationClient = null;
    public BDLocationListener mListener = new MyListener();
    LocationClientOption option = new LocationClientOption();

    public String add;

    public Context context;
    public MyLocation(Context c)
    {
        this.context=c;
        initLocation(context);
    }
    public void initLocation(Context context) {
        mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(mListener);
        option.setScanSpan(3000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);    //启动Gps，注：手机上的GPs开关必须由用户自己打开。
        option.disableCache(true);    //设置是否不缓存结果
        option.setCoorType("gcj02"); //设置坐标系类型。
        option.setServiceName("com.baidu.location.service_v2.9");
        // option.setServiceName("com.baidu.location.service_v2.ss"); //这个属性一定要设置
        mLocationClient.setLocOption(option);

    }

    public void start() {
        mLocationClient.start();
        mLocationClient.requestLocation();

    }
    public void stop()
    {
        if(mLocationClient.isStarted())
        {

            mLocationClient.stop();
        }
    }





    public class MyListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            if (null != arg0 && arg0.getLocType() != BDLocation.TypeServerError) {
                Log.i("纬度", arg0.getLatitude() + "");
                Log.i("经度", arg0.getLongitude() + "");
                Log.i("精度半径", arg0.getRadius() + "");
                List<Address> addresses=new ArrayList<>();
                double[] addlatlon = new double[2];
                addlatlon[0] = arg0.getLatitude();
                addlatlon[1] = arg0.getLongitude();

                if (arg0.getLocType() == BDLocation.TypeNetWorkLocation) { //网络定位的结果
                    Log.i("地址", arg0.getAddrStr() + "");
                    Log.i("fjdk", arg0.getProvince() + arg0.getCity());
                    String addr=arg0.getProvince()+arg0.getCity();
                    MyApplication.getInstance().setAddress(addr);
                    MyApplication.getInstance().setHasAddr(true);
                    Intent intent = new Intent();  //Itent就是我们要发送的内容
                    intent.putExtra("hasAddr",true);
                    intent.setAction("com.my.address");   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                    context.sendBroadcast(intent);   //发送广播
                    mLocationClient.stop();

                }
                else
                {
                    MyApplication.getInstance().setHasAddr(false);
                    Intent intent = new Intent();  //Itent就是我们要发送的内容
                    intent.putExtra("hasAddr",false);
                    intent.setAction("com.my.address");   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                    context.sendBroadcast(intent);
                    mLocationClient.stop();
                }


            }
            else {

                MyApplication.getInstance().setHasAddr(false);
                Intent intent = new Intent();  //Itent就是我们要发送的内容
                intent.putExtra("hasAddr",false);
                intent.setAction("com.my.address");   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                context.sendBroadcast(intent);
                mLocationClient.stop();
            }

        }
    }
}




