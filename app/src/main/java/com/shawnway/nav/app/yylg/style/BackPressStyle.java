package com.shawnway.nav.app.yylg.style;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shawnway.nav.app.yylg.bean.CarItemBean;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;

import java.util.ArrayList;
import java.util.List;


public class BackPressStyle {
    long back_pressed;

    Activity mContext;

    public BackPressStyle() {
        back_pressed = System.currentTimeMillis();
    }

    public void onBackPressed(Context context) {

        mContext = (Activity) context;

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            Log.d("backPress", "time:" + back_pressed);
            int realLength = (int) Utils.getParam(context, "realLength", 0);
            if(realLength > 0){
                for(int i = 0;i<realLength;i++){
                    String realValue = (String) Utils.getParam(context, "real" + i, "");
                    Utils.setParam(context,realValue,"");
                }
            }
//            Utils.erasePWDInfo(mContext);//退出APP之后干掉保存的密码？TODO:
            ToastUtil.stopFlag = true;
//            saveCartData(context);
           ((Activity) context).finish();
//            CloseActivityUtil.close();
        } else {
            Toast ts = Toast.makeText(context, "再按一次离开", Toast.LENGTH_SHORT);
            ts.setGravity(Gravity.CENTER, 0, 0);
            ts.show();
        }

        back_pressed = System.currentTimeMillis();
    }

    private List<CarItemBean> mData;

    public void initCarData(){
        try {
            mData = new Gson().fromJson((String) Utils.getParam(mContext, "cart", "[]"), new TypeToken<ArrayList<CarItemBean>>() {
            }.getType());
        }catch (Exception e){
            mData=new ArrayList<>();
        }
    }

//    public void saveCartData(Context context){
//        initCarData();
//        Gson gson = new Gson();
//        String cartData = gson.toJson(mData);
//        Log.d("购物车", "退出APP之前保存购物车数据:" + cartData);
//        Utils.setParam(context, "cart", cartData);
//    }

}