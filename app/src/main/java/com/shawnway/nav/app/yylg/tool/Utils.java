package com.shawnway.nav.app.yylg.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.activity.LoginActivity;
import com.shawnway.nav.app.yylg.activity.OrderActivity;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.fragment.UserFragment;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.net.VolleyTool;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float, Long类型的参数
 * 同样调用getParam就能获取到保存在手机里面的数据
 *
 * @author xiaanming
 */
public class Utils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";
    private static final String TAG = "Utils";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.apply();
//        editor.commit();
    }

    /**
     * 移除数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     */
    public static void removeParam(Context context, String key) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(key);
//        editor.commit();
        editor.apply();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        if (context == null) {
            return null;
        }
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }


    /**
     * 根据字串中的参数位给字串填充参数
     *
     * @param count 要填充的参数位置，例如要替换"%1$s"，则填1
     * @param src   要处理的字串
     * @param param 参数值
     * @return str
     */
    public static String insertParams(int count, String src, String param) {
        return src.replace(Utils.getTextTemplecate(count), param);
    }


    //获取参数标志，例如遇到字串中含有“%1$s”，则代表这是第一个参数位
    public static CharSequence getTextTemplecate(int i) {
        return "%" + i + "$s";
    }


    public static boolean handleResponseError(Context context, ResponseGson response) {
        return handleResponseError(true, context, response, "");
    }

    public static boolean handleResponseError(Context context, ResponseGson response, String str) {
        return handleResponseError(true, context, Constants.ACTION_REQUEST_LOGIN, str, response);
    }

    public static boolean handleResponseError(final boolean autoOpenLoginActivity, Context context, ResponseGson response) {
        return handleResponseError(autoOpenLoginActivity, context, response, "");
    }


    public static boolean handleResponseError(final boolean autoOpenLoginActivity, Context context, ResponseGson response, String str) {
        return handleResponseError(autoOpenLoginActivity, context, Constants.ACTION_REQUEST_LOGIN, str, response);
    }

    /**
     * 对返回结果进行一般性处理：
     * ①当出现身份校验错误时自动发送密码到服务器
     * ②其余错误状态进行toast处理
     * ③可以设置自动发送后依然错误是否弹出登陆页面
     *
     * @param context
     * @param response
     * @return 若response中不包含错误则返回false
     */
    public static boolean handleResponseError(final boolean autoOpenLoginActivity, final Context context, final int requestCode, final String str, final ResponseGson response) {
        if (response.getCode().equals(VolleyTool.SUCCESS)) {
            return false;
        } else if (response.getCode().equals(VolleyTool.ATHENTICATION_REQUIRED)) {
//            if (autoOpenLoginActivity)
//                LoginActivity.getInstance(context,requestCode,true,true);
            Log.d(TAG, "需要登录");
            String acc = (String) Utils.getParam(context, "acc", "");
            String dwp = (String) Utils.getParam(context, "dwp", "");
//            boolean rember = (boolean) Utils.getParam(context, "rem", false);

            if (StringUtils.isNotEmpty(dwp)) {
                Hook.getInstance(context).login(acc, dwp, new Response.Listener<ResponseGson>() {
                    @Override
                    public void onResponse(ResponseGson responseGson) {
                        if (responseGson.getCode().equals(VolleyTool.SUCCESS)) {
                            LoginActivity.getInstance(context, requestCode, false);
                        } else {
                            if (autoOpenLoginActivity) showLoginActivity(context, requestCode, str+"需重新登陆");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        if(Constants.loginActivity==null){
//                            Intent intent = new Intent(context, LoginActivity.class);
//                            intent.putExtra("rem",false);
//                            context.startActivityForResult(intent, requestCode);
//                        }
                        ToastUtil.showNetError(context);

                    }
                });
            } else if
                    (autoOpenLoginActivity) showLoginActivity(context, requestCode, str+"还没登录,请登录~");

        } else if (response.getCode().equals(VolleyTool.VALIDATE_ERROR)) {
            try {
                ToastUtil.showShort(context, response.getHeader().error);
            } catch (NullPointerException e) {
                ToastUtil.showShort(context, str + "验证错误");
            }
        } else if (response.getCode().equals(VolleyTool.GENERIC_ERROR)) {
            ToastUtil.showShort(context, str + "系统错误");
        } else {
            ToastUtil.showShort(context, str + "包含未处理的错误码");
        }
        return true;
    }

    /**
     * 自动登录，在登录的fragment页面调用，为的是解决后台三十分钟后登陆状态失效的问题
     *
     * @param context
     */
    public static void autoLogin(final Context context) {
        String acc = (String) Utils.getParam(context, "acc", "");
        String dwp = (String) Utils.getParam(context, "dwp", "");
        if (StringUtils.isNotEmpty(dwp)) {
            Hook.getInstance(context).login(acc, dwp, new Response.Listener<ResponseGson>() {
                @Override
                public void onResponse(ResponseGson response) {
                    if (!Utils.handleResponseError(context, response)) {
                        Log.d(TAG, "自动登录成功");
                        Log.d("AutoLoginService", "当前时间为:" + new Date().getTime());
                        Intent intent = new Intent(Constants.ACTION_LOGIN);
                        context.sendBroadcast(intent);
                    } else {
                        Log.d(TAG, "自动登录失败");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d(TAG, "自动登录网络请求失败：" + volleyError);
                }
            });
            RetrofitManager.getInstance()
                    .getApi()
                    .retrieveUserImge()
                    .compose(ThreadTransformer.<UserFragment.ImageBean>applySchedulers())
                    .subscribe(new BaseSubscriber<UserFragment.ImageBean>() {
                        @Override
                        public void onSuccess(UserFragment.ImageBean imageBean) {
                            Utils.setParam(context, UserFragment.IMAGE_URL, imageBean.getBody().getPathUrl());
                        }
                    });
        }
    }


    /**
     * 用于高敏感度的操作，例如支付，不会自动发送密码
     * 对返回结果进行一般性处理：
     * ①当出现身份校验错误时跳转到login界面
     * ②其余错误状态进行toast处理
     *
     * @param context
     * @param response
     * @return 若response中不包含错误则返回false
     */
    public static boolean handleError(Activity context, ResponseGson response) {
        return handleResponseError(context, response, "");
    }

    public static boolean handleError(Activity context, int requestCode, ResponseGson response) {
        if (response.getCode().equals(VolleyTool.SUCCESS)) {
            return false;
        } else if (response.getCode().equals(VolleyTool.ATHENTICATION_REQUIRED)) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("rem", false);
            context.startActivityForResult(intent, requestCode);
            ToastUtil.showShort(context, "需重新登陆");
        } else if (response.getCode().equals(VolleyTool.VALIDATE_ERROR)) {
            try {
                ToastUtil.showShort(context, response.getHeader().error);
            } catch (NullPointerException e) {
                Log.e("VolleyTool", "验证错误缺少错误信息");
            }
        } else if (response.getCode().equals(VolleyTool.GENERIC_ERROR)) {
            try {
                ToastUtil.showShort(context, response.getHeader().error);
            } catch (NullPointerException e) {
                Log.e("VolleyTool", "系统错误缺少错误信息");
            }
        } else {
            ToastUtil.showShort(context, "包含未处理的错误码");
        }
        return true;
    }

    public static boolean handleError(Activity context, ResponseGson response, String str) {

        if (response.getCode().equals(VolleyTool.SUCCESS)) {
            return false;
        } else if (response.getCode().equals(VolleyTool.ATHENTICATION_REQUIRED)) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivityForResult(intent, Constants.ACTION_REQUEST_LOGIN);
            ToastUtil.showShort(context, "需重新登陆");
        } else if (response.getCode().equals(VolleyTool.VALIDATE_ERROR)) {
            try {
                ToastUtil.showShort(context, str + response.getHeader().error);
            } catch (NullPointerException e) {
                Log.e("VolleyTool", "验证错误缺少错误信息");
            }
        } else if (response.getCode().equals(VolleyTool.GENERIC_ERROR)) {
            try {
                if (context instanceof OrderActivity) {
                    return true;
                }
                ToastUtil.showShort(context, response.getHeader().error);
            } catch (NullPointerException e) {
                Log.e("VolleyTool", "系统错误缺少错误信息");
            }
        } else {
            ToastUtil.showShort(context, str + "包含未处理的错误码");
        }
        return true;
    }

    private static void showLoginActivity(Context context, int requestCode, String str) {
        try {
            Intent intent = new Intent(context, LoginActivity.class);
            ((Activity) context).startActivityForResult(intent, requestCode);
            ToastUtil.showShort(context, str);
        } catch (Exception e) {
            Log.e("login", "request login activity has finished");
        }
    }


    /**
     * 将editText中的字串返回
     *
     * @param editText 必须为EditView
     * @return 假如传入非EditView的参数则返回null
     */

    public static String getEditTextStr(View editText) {
        try {
            if (editText == null) return null;
            Editable text = ((EditText) editText).getText();
            if (text != null)
                return text.toString();
        } catch (ClassCastException e) {
            Log.e("Utils", e.getStackTrace().toString());
        }
        return null;
    }


    public static void erasePWDInfo(Context context) {
        boolean rember = (boolean) Utils.getParam(context, "rem", false);
        if (!rember) {
            Utils.removeParam(context, "acc");
            Utils.removeParam(context, "dwp");
        }
        Utils.removeParam(context, "image_url");
        Utils.removeParam(context, "isBackGroundAccount");
    }

    public static void savePWDInfo(Context context, String acc, String pwd, boolean remenber_me, String image_url, String isBackGroundAccount) {
        Utils.setParam(context, "acc", acc);
        Utils.setParam(context, "dwp", pwd);
        Utils.setParam(context, "rem", remenber_me);
        Utils.setParam(context, UserFragment.IMAGE_URL, image_url);
        Utils.setParam(context, "isBackGroundAccount", isBackGroundAccount);
    }

    public static boolean noListData(int page, int totalPage, ArrayList list) {
        return (page > totalPage || list == null || list.size() == 0);

    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void savephone(Context context, String acc) {
        Utils.setParam(context, "phone", acc);
    }
}


