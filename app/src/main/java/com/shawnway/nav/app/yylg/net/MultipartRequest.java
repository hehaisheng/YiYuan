package com.shawnway.nav.app.yylg.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.shawnway.nav.app.yylg.tool.CookieUtil;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest<T> extends Request<T> {

    private static final String TAG = "MultipartRequest";

    private Response.Listener<T> mListener;
    private MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

    private Map<String, File> mFileParts;
    private Map<String, String> mParams;
    private Map<String, String> sendHeader = new HashMap<String, String>(1);

    private Gson mGson;
    private Context mContext;
    private Class<T> mClazz;

//    /**
//     * 单个文件
//     *
//     * @param url
//     * @param errorListener
//     * @param listener
//     * @param filePartName
//     * @param file
//     * @param params
//     */
//    public MultipartRequest(Class clazz, Context context,String url, String filePartName, File file,
//                            Map<String, String> params,
//                            Response.Listener<T> listener, Response.ErrorListener errorListener) {
//        super(Method.POST, url, clazz, context, listener, errorListener);
//
//        mFileParts = new HashMap<>();
//        if (file != null) {
//            mFileParts.put("file", file);
//        }
//        mFilePartName = filePartName;
//        mParams = params;
//        buildMultipartEntity();
//    }

    /**
     * 多个文件，对应一个key
     *
     * @param url
     * @param errorListener
     * @param listener      //     * @param filePartName
     * @param files
     * @param params
     */
    public MultipartRequest(Class<T> clazz, Context context, String url,
                            Map<String, File> files, Map<String, String> params,
                            Response.Listener<T> listener, Response.ErrorListener errorListener) {
//        super(Method.POST, url, clazz, context, listener, errorListener);
        super(Method.POST, url, errorListener);
//        mFilePartName = filePartName;
        mListener = listener;
        mFileParts = files;
        mParams = params;

        mGson = new Gson();
        mContext=context;
        mClazz=clazz;

        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        if (mFileParts != null && mFileParts.size() > 0) {
            for (Map.Entry<String, File> entry : mFileParts.entrySet()) {
                entity.addPart(entry.getKey(), new FileBody(entry.getValue()));
            }
//            new FileBody()
            long l = entity.getContentLength();
            Log.d(TAG, mFileParts.size() + "个，长度：" + l);
        }

        try {
            if (mParams != null && mParams.size() > 0) {
                for (Map.Entry<String, String> entry : mParams.entrySet()) {
                    entity.addPart(
                            entry.getKey(),
                            new StringBody(entry.getValue(), Charset
                                    .forName("UTF-8")));
                }
            }
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }

    }

    @Override
    public String getBodyContentType() {
//        Log.d(TAG, "requestBodyType:");
//        Log.d(TAG, "value:" + entity.getContentType().getValue() + "  bodylenth: " + entity.getContentLength());
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        Log.d(TAG, "body string is:" + bos.toString());
        return bos.toByteArray();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
//        CLog.log("parseNetworkResponse");
        if (VolleyLog.DEBUG) {
            if (response.headers != null) {
                for (Map.Entry<String, String> entry : response.headers
                        .entrySet()) {
                    VolleyLog.d(entry.getKey() + "=" + entry.getValue());
                }
            }
        }

        CookieUtil.saveCookie(response, mContext);
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.d(TAG, "sended url:" + getUrl());
            Log.d(TAG, "json from response:  " + jsonString);
//            if (mClass!=null)
            return Response.success(mGson.fromJson(jsonString, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
//            else
//                return Response.success(a=mGson.fromJson(jsonString, mType),
//                        HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T s) {
        mListener.onResponse(s);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.android.volley.Request#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        sendHeader.put("Content-Type", getBodyContentType());
        return sendHeader;
    }

    public void setSendCookie(String cookie) {
        try {
            sendHeader.put("Cookie", cookie);
            Log.d(TAG, "after set cookie:" + getHeaders().toString());
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }
    }

    public void setSendCookie() {
        String localCookieStr = CookieUtil.getLocalCookies(mContext);
        if (!localCookieStr.equals("")) {
            setSendCookie(localCookieStr);
        }
    }

}