package com.shawnway.nav.app.yylg.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.shawnway.nav.app.yylg.tool.CookieUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {

    private static final String TAG = "GsonRequest";
    private static final int TYPE_NAME_VALUE_PAIR = 0;
    private static final int TYPE_JSONSTRING = 2;

    private final Listener<T> mListener;
//    private  Type mType;

    private Gson mGson;

    private Class<T> mClass;

    private Context mContext;

    private Map<String, String> sendHeader = new HashMap<String, String>(1);

    private Map<String, String> mMap;

    private int paramsType = TYPE_NAME_VALUE_PAIR;


    public GsonRequest(int method, String url, Class<T> clazz, Context context, Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        mClass = clazz;
        mListener = listener;
        mContext = context;
        mMap = new HashMap<>();
    }

    public GsonRequest(int method, String url, Map params, Class<T> clazz, Context context, Listener<T> listener,
                       ErrorListener errorListener) {
        this(method, url, clazz, context, listener, errorListener);
        setParams(params);
    }

    public GsonRequest(int method, String url, Object gsonObeject, Class<T> clazz, Context context, Listener<T> listener,
                       ErrorListener errorListener) {
        this(method, url, clazz, context, listener, errorListener);
        setJsonstr(gsonObeject);
    }


//    public GsonRequest(int method, String url, Type clazz,Context context, Listener<T> listener,
//                       ErrorListener errorListener) {
//        super(method, url, errorListener);
//        mGson = new Gson();
////        mClass = clazz;
//        mType=clazz;
//        mListener = listener;
//        mContext = context;
//        mMap=new HashMap<>();
//    }


    public GsonRequest(String url, Class<T> clazz, Context context, Listener<T> listener,
                       ErrorListener errorListener) {
        this(Method.GET, url, clazz, context, listener, errorListener);
    }


    //当http请求是post时，则需要该使用该函数设置往里面添加的键值对
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        CookieUtil.saveCookie(response, mContext);
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.d(TAG, "json from response:  " + jsonString);
//            if (mClass!=null)
            return Response.success(mGson.fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
//            else
//                return Response.success(a=mGson.fromJson(jsonString, mType),
//                        HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (mListener == null || response == null) {
            Log.d(TAG, "网络出错。。。");
            return;
        }
        mListener.onResponse(response);
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
        String localCookieStr = CookieUtil.getLocalCookies(mContext);
        Log.d(TAG, "Sended cookies->" + localCookieStr);
        if (!localCookieStr.equals("")) {
            setSendCookie(localCookieStr);
        } else {
            Log.d(TAG, "cookies at phone is null");
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        switch (paramsType) {
            case TYPE_JSONSTRING:
                if (jsonstr != null)
                    Log.d(TAG, "sended params:" + jsonstr);
                try {
                    return jsonstr.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", new Object[]{jsonstr, "utf-8"});
                }
                return null;
            default:
                return super.getBody();

        }
    }

    String jsonstr;

    public void setParams(Map map) {
        setParamsType(TYPE_NAME_VALUE_PAIR);
        mMap = map;
    }

    public void setJsonstr(String str) {
        sendHeader.put("Content-Type", "application/json");
        jsonstr = str;
        setParamsType(TYPE_JSONSTRING);
        Log.d(TAG, "setted params:" + jsonstr);
    }

    public void setJsonstr(Object gson) {
        setJsonstr(new Gson().toJson(gson));
    }

    private void setParamsType(int type) {
        paramsType = type;
    }
}