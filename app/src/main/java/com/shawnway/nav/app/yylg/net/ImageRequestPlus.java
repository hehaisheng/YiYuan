package com.shawnway.nav.app.yylg.net;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.shawnway.nav.app.yylg.tool.CookieUtil;

public class ImageRequestPlus extends ImageRequest {
	private static final String TAG = "ImageRequestPlus";
	private Context mContext;
	private Map<String, String> sendHeader = new HashMap<>(1);

	
	/**
	 * this class will save cookie autolly
	 */
	public ImageRequestPlus(Context context, String url,
			Listener<Bitmap> listener, int maxWidth, int maxHeight,
			Config decodeConfig, ErrorListener errorListener) {
		super(url, listener, maxWidth, maxHeight, decodeConfig, errorListener);
		mContext = context;
	}

	@Override
	protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {

		CookieUtil.saveCookie(response, mContext);
		return super.parseNetworkResponse(response);

	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return sendHeader;
	}

	public void setSendCookie(String cookie) {
		sendHeader.put("Cookie", cookie);
		try {
			Log.d(TAG, getHeaders().toString());
		} catch (AuthFailureError e) {
			e.printStackTrace();
		}
	}

	public void setSendCookie() {
		String localCookieStr=CookieUtil.getLocalCookies(mContext);
		if (!localCookieStr.equals("")) {
			setSendCookie(localCookieStr);
		}
	}

}
