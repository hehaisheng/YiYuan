package com.shawnway.nav.app.yylg.tool;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
//正如前面代码看到的，在创建一个请求时，需要添加一个错误监听onErrorResponse。如果请求发生异常，会返回一个VolleyError实例。  
//以下是Volley的异常列表：  
//AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。  
//NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。  
//NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。  
//ParseError：在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。  
//SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。  
//TimeoutError：Socket超时，服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为2.5秒。如果得到这个错误可以使用RetryPolicy。  

public class VolleyErrorHelper {

    /**
     * Returns appropriate message which is to be displayed to the user against 
     * the specified error object. 
     *
     * @param error
     * @param context
     * @return
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(
                    R.string.error_system);
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);
        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.network_error);
        }
        return context.getResources().getString(R.string.error_unknown);
    }

    /**
     * Determines whether the error is related to network 
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError)
                || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server 
     *
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError)
                || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock 
     * message or to show a message retrieved from the server. 
     *
     * @param err
     * @param context
     * @return
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 404:
                    return context.getString(R.string.error_system);
                case 422:
                case 401:
                case 500:
                case 400:
                    try {
                        // server might return error like this { "error":
                        // "Some error occured" }
                        // Use "Gson" to parse the result
                        ResponseGson errorResp = new Gson().fromJson(
                                new String(response.data),
                                ResponseGson.class);
                        Log.e("VolleyTool","get error:"+new String(response.data));
                        if (errorResp != null && errorResp.getHeader()!=null) {
                          if (errorResp.getCode().equals(VolleyTool.VALIDATE_ERROR)) {
                              Log.e("VolleyTool", "验证错误缺少错误信息");
                             return errorResp.getError();

                            } else if (errorResp.getCode().equals(VolleyTool.GENERIC_ERROR)) {
                              Log.e("VolleyTool", "系统错误");
                              return errorResp.getError();

                          } else {
                              Log.e("VolleyTool", "未知错误码");
                              return errorResp.getError();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request
                    return error.getMessage();

                default:
                    return context.getResources().getString(
                            R.string.error_system);
            }
        }
        return context.getResources().getString(R.string.unkown_error);
    }
}  