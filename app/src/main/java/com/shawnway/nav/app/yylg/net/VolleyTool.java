package com.shawnway.nav.app.yylg.net;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.shawnway.nav.app.yylg.cache.BitmapCache;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.NetUtil;
import com.shawnway.nav.app.yylg.tool.PayHook;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2015/11/3.
 * 该应用的网络层
 * ①分两大类型请求：
 * A、返回数据为JSONObject（{@link JsonObjectRequestPlus}) ，主要用在有无限key的response
 * B、返回数据已经过Gson转换为java对象({@link GsonRequest,MultipartRequest})
 * ②已实现三种ContentType的实体请求：
 * A、发送普通键值对（主要方式）：{@link #sendGsonRequest(Object, Class, int, String, Map, Listener, Response.ErrorListener, DefaultRetryPolicy)}
 * B、发送JSON数据：a、{@link #sendGsonRequest(Object, Class, int, String, String, Listener, Response.ErrorListener)}
 * b、{@link #sendJsonObjectPostRequest(Object, String, JSONObject, Listener, Response.ErrorListener)}
 * C、发送表单数据：{@link #sendMultipartRequest(Object, Class, String, Map, Map, Listener, Response.ErrorListener)}
 * ③图片请求见
 * {@link ImageRequestPlus}
 */
public class VolleyTool {
    public static final String SUCCESS = "000";// 成功处理
    public static final String VALIDATE_ERROR = "E001";// 验证错误
    public static final String GENERIC_ERROR = "E000";// 系统错误
    public static final String ATHENTICATION_REQUIRED = "A000";// 需要登陆
    private static final String TAG = "VolleyTool";

    private static VolleyTool mInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mContext;

    private VolleyTool(Context context) {
        mRequestQueue = Volley.newRequestQueue(mContext = context);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
    }

    public static VolleyTool getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyTool(context.getApplicationContext());
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }

    public <T extends Object> void sendGsonRequest(Object tag, Class clazz, String url, Listener<T> listener, Response.ErrorListener errorListener) {
        sendGsonRequest(tag, clazz, url, null, listener, errorListener);
    }

    public <T extends Object> void sendGsonRequest(Object tag, Class clazz, String url, Listener<T> listener, Response.ErrorListener errorListener, DefaultRetryPolicy timeout) {
        sendGsonRequest(tag, clazz, url, null, listener, errorListener, timeout);
    }

    public <T extends Object> void sendGsonRequest(Object tag, Class clazz, String url, Map<String, String> params, Listener<T> listener, Response.ErrorListener errorListener) {
        sendGsonRequest(tag, clazz, url, params, listener, errorListener, null);
    }

    public <T extends Object> void sendGsonRequest(Object tag, Class clazz, String url, Map<String, String> params, Listener<T> listener, Response.ErrorListener errorListener, DefaultRetryPolicy timeout) {
        if (params != null) url = NetUtil.putGetBody(url, params);
        sendGsonRequest(tag, clazz, Method.GET, url, params, listener, errorListener, timeout);
    }

    public <T extends Object> void sendGsonPostRequest(Object tag, Class clazz, String url, Map<String, String> params, Listener<T> listener, Response.ErrorListener errorListener) {
        sendGsonRequest(tag, clazz, Method.POST, url, params, listener, errorListener, null);
    }

    public <T extends Object> GsonRequest<T> sendGsonPostRequest(Object tag, Class clazz, String url, Listener<T> listener, Response.ErrorListener errorListener) {
        return sendGsonRequest(tag, clazz, Method.POST, url, listener, errorListener);
    }

    public <T extends Object> GsonRequest<T> sendGsonRequest(Object tag, @NonNull Class clazz, int method, @NonNull String url, Listener<T> listener, Response.ErrorListener errorListener) {
        return sendGsonRequest(tag, clazz, method, url, new HashMap<String, String>(), listener, errorListener, null);
    }


    /**
     * 每一个GsonRequest最后都会调用这个方法
     * 发送键值对数据，返回数据封装为java对象
     * <p/>
     * note:仅用于服务器返回JSON类型数据时
     *
     * @param tag           请求所绑定的页面
     * @param clazz         指定返回数据的载体类，
     * @param method        POST/GET
     * @param url           url
     * @param params        参数
     * @param listener      回调方法，T不能是clazz的子类，因为数据经gson转入载体类再强转返回给回调方法，否则会出转换错误
     * @param errorListener 失败回调方法
     * @param <T>           不能是clazz的子类
     * @return 特殊用法：{@link PayHook#prepayOrder(String, PayHook.PayParamsBean, Listener, Response.ErrorListener)}
     */
    public <T extends Object> GsonRequest<T> sendGsonRequest(Object tag, @NonNull Class clazz, int method, @NonNull String url, @Nullable Map<String, String> params, Listener<T> listener, Response.ErrorListener errorListener, DefaultRetryPolicy timeOut) {
        GsonRequest<T> request = new GsonRequest<T>(method, url, clazz, mContext, listener, errorListener);
        Log.d(TAG, "sended url:" + url);
        request.setTag(tag == null ? mContext : tag);
        if (params != null)
            request.setParams(params);
        request.setSendCookie();//设置cookies放于header里，应用通过cookie进行身份认证
        if (timeOut != null) {
            request.setRetryPolicy(timeOut);
        }
        else {
            DefaultRetryPolicy defaultRetryPolicy=new DefaultRetryPolicy(30 * 1000,
                  DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(defaultRetryPolicy);
        }
        mRequestQueue.add(request);

        return request;
    }


    /**
     * 这个方法将以json的格式传递参数给服务器,以gson返回数据
     *
     * @param tag
     * @param clazz
     * @param method
     * @param url
     * @param paramstr      需要一个JSON串
     * @param listener
     * @param errorListener
     * @param <T>
     * @return
     */
    public <T extends Object> GsonRequest<T> sendGsonRequest(Object tag, @NonNull Class clazz, int method, @NonNull String url, @NonNull String paramstr, Listener<T> listener, Response.ErrorListener errorListener) {
        return sendGsonRequest(tag, clazz, method, url, paramstr, listener, errorListener, null);
    }

    public <T extends Object> GsonRequest<T> sendGsonRequest(Object tag, @NonNull Class clazz, int method, @NonNull String url, @NonNull String paramstr, Listener<T> listener, Response.ErrorListener errorListener, DefaultRetryPolicy timeout1) {
        GsonRequest<T> request = new GsonRequest<T>(method, url, clazz, mContext, listener, errorListener);
        if (paramstr != null) request.setJsonstr(paramstr);
        request.setTag(tag);
        request.setSendCookie();
        if (timeout1 != null) {
            request.setRetryPolicy(timeout1);
        }
        else {
            DefaultRetryPolicy defaultRetryPolicy=new DefaultRetryPolicy(30 * 1000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(defaultRetryPolicy);
        }
        mRequestQueue.add(request);
        return request;
    }


    public <T extends Object> GsonRequest<T> sendGsonPostRequest(Object tag, Class clazz, String url, String paramstr, Listener<T> listener, Response.ErrorListener errorListener) {
        return sendGsonRequest(tag, clazz, Method.POST, url, paramstr, listener, errorListener, null);
    }

    public <T extends Object> GsonRequest<T> sendGsonPostRequest(Object tag, Class clazz, String url, String paramstr, Listener<T> listener, Response.ErrorListener errorListener, DefaultRetryPolicy timeout) {
        return sendGsonRequest(tag, clazz, Method.POST, url, paramstr, listener, errorListener, timeout);
    }

    public void sendJsonObjectPostRequest(Object tag, String url, JSONObject jsonstr, Listener<JSONObject> listener, Response.ErrorListener errorListener) throws JSONException {
        JsonObjectRequestPlus request = new JsonObjectRequestPlus(Request.Method.POST, url, jsonstr, mContext, listener, errorListener);
        request.setTag(tag);
        request.setSendCookie();
        VolleyTool.getInstance(mContext).getRequestQueue().add(request);
    }

    public <T extends Object> MultipartRequest sendMultipartRequest(Object tag, Class clazz, String url, Map<String, File> files, Map<String, String> params, Listener<T> listener, Response.ErrorListener errorListener) {
        MultipartRequest req = new MultipartRequest(clazz, mContext, url, files, params, listener, errorListener);
        req.setTag(tag);
        req.setSendCookie();
        req.setRetryPolicy(new DefaultRetryPolicy(Constants.DEF_TIMEOUT * 1000, 1, 1.0f));
        mRequestQueue.add(req);
        return req;
    }

    public void release() {
        this.mImageLoader = null;
        this.mRequestQueue = null;
        mInstance = null;
    }


}
