package com.shawnway.nav.app.yylg.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.android.volley.Response;
import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.bean.CarItemBean;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.net.VolleyTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eiffel on 2015/11/9.
 */
public class AppUtils {
    public static void addTocart(Activity activity, CarItemBean item) {
        ArrayList<CarItemBean> goods = ((MyApplication) activity.getApplication()).getData();

        boolean exist = false;
        for (int i = 0; i < goods.size(); i++) {
            CarItemBean appCartItem = goods.get(i);
            if (appCartItem.getGood().getDrawCycleID() == item.getGood().getDrawCycleID()) {
                appCartItem.setBuyed(appCartItem.getBuyed() + 1);//设置商品的购买份数，已经购买的加上每个商品的最小购买份数
                exist = true;
                break;
            }
        }
        if (!exist)
            goods.add(item);

        Intent intent = new Intent(Constants.ACTION_CART_CHANGE);
        activity.sendBroadcast(intent);
    }
    public static void addTocart(Activity activity, GoodBean dataFromIntent) {
        addTocart(activity, new CarItemBean(dataFromIntent));
    }

    public static void addTocart(Activity activity, GoodDetail dataFromIntent) {
        addTocart(activity,new CarItemBean(dataFromIntent));
    }

    /**
     * 更新购物车商品数据
     * @param mData  购物车商品数据
     * @param context 上下文
     * @param listener 网络请求成功之后的监听
     * @param errorListener  网络请求失败之后的监听
     */
    public static void updateCartList(List<CarItemBean> mData, Context context, Response.Listener<GoodListGsonResponse> listener, Response.ErrorListener errorListener) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mData.size(); i++) {
            builder.append(mData.get(i).getGood().getDrawCycleID() + ",");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        Map<String, String> params = new HashMap<>();
        params.put("drawCycleIdList", builder.toString());
        VolleyTool.getInstance(context).sendGsonRequest(context, GoodListGsonResponse.class, Constants.GET_GOOD_INFO_URL, params, listener, errorListener);

    }

    /**
     *
     *
     *
     * @param activity
     *
     * @return false 为没有可用网络 true 为当前网络正常
     */

    public static boolean hasInternet(Activity activity) {

        ConnectivityManager manager = (ConnectivityManager) activity

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info == null || !info.isAvailable()) {

            return false;

        }

        if (info.isRoaming()) {

            return true;

        }

        return true;

    }


    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Activity activity){
        int width = 0;
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        width=display.getWidth();
        return width;
    }


    public static String getDeviceId() {
        return Build.MODEL;
    }
}
