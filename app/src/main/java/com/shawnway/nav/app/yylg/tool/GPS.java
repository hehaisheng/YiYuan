package com.shawnway.nav.app.yylg.tool;

import android.app.Activity;
import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.service.LocationService;

public class GPS {
	private final static String TAG = "GPS";
	private final LocationService mLocationService;

	public BDLocationListener mListener ;
	private BDLocation mBDLocation;

	static final String PROVINCE="province";//for sharePeferences
	static final String CITY = "location";	 	//for sharePeferences

	/**
	 * @param listener
	 */
	public GPS(Activity activity,BDLocationListener listener) {
		mLocationService=((MyApplication)activity.getApplication()).locationService;

		mLocationService.setLocationOption(mLocationService.getDefaultLocationClientOption());

		// to invoke
		mListener=listener;
		mLocationService.registerListener(mListener);
	}

	public void start() {
		mLocationService.start();
	}

	public void stop(){
		mLocationService.stop();
	}

	public void unregistListener(){
		mLocationService.unregisterListener(mListener);
	}



	final int SUCCESS = 61;
	final int SUCCESS_BYNET = 161;



	public boolean isSuccess() {
		return mBDLocation.getLocType() == SUCCESS
				| mBDLocation.getLocType() == SUCCESS_BYNET;
	}


	public static String getLocalLocation(Context context) {
		return String.valueOf(Utils.getParam(context, "location", ""));
	}
}
