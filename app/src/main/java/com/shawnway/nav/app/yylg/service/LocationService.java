package com.shawnway.nav.app.yylg.service;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 *
 * @author baidu
 *
 */
public class LocationService {
	private LocationClient client = null;
	private LocationClientOption mOption,DIYoption;
	private Object  objLock = new Object();

	/***
	 *
	 * @param locationContext
	 */
	public LocationService(Context locationContext){
		synchronized (objLock) {
			if(client == null){
				client = new LocationClient(locationContext);
				client.setLocOption(getDefaultLocationClientOption());
			}
		}
	}

	/***
	 *
	 * @param listener
	 * @return
	 */

	public boolean registerListener(BDLocationListener listener){
		boolean isSuccess = false;
		if(listener != null){
			client.registerLocationListener(listener);
			isSuccess = true;
		}
		return  isSuccess;
	}

	public void unregisterListener(BDLocationListener listener){
		if(listener != null){
			client.unRegisterLocationListener(listener);
		}
	}

	/***
	 *
	 * @param option
	 * @return isSuccessSetOption
	 */
	public boolean setLocationOption(LocationClientOption option){
		boolean isSuccess = false;
		if(option != null){
			if(client.isStarted())
				client.stop();
			DIYoption = option;
			client.setLocOption(option);
			isSuccess = true;
		}
		return isSuccess;
	}

	public LocationClientOption getOption(){
		return DIYoption;
	}
	/***
	 *
	 * @return DefaultLocationClientOption
	 */
	public LocationClientOption getDefaultLocationClientOption(){
		if(mOption == null){
			mOption = new LocationClientOption();
			mOption.setLocationMode(LocationMode.Hight_Accuracy);
			mOption.setCoorType("bd09ll");
			int span = 1000;
			mOption.setScanSpan(10 * 60 * 1000);
			mOption.setIsNeedAddress(true);
			mOption.setOpenGps(true);
			mOption.setLocationNotify(false);
			mOption.setIsNeedLocationDescribe(false);
			mOption.setIsNeedLocationPoiList(true);
			mOption.setIgnoreKillProcess(false);
			mOption.SetIgnoreCacheException(false);
			//这个属性一定要设置 不然找不到服务就无法定位
			//com.baidu.location.service_v2.9
			//com.baidu.location.service_v2.2
			mOption.setServiceName("com.baidu.location.service_v2.9");
			mOption.setEnableSimulateGps(false);}
		return mOption;
	}

	public void start(){
		synchronized (objLock) {
			if(client != null && !client.isStarted()){
				client.start();
			}
		}
	}
	public void stop(){
		synchronized (objLock) {
			if(client != null && client.isStarted()){
				client.stop();
			}
		}
	}

}
