package com.shawnway.nav.app.yylg.bean;

import android.content.Context;
import android.util.Log;

import com.shawnway.nav.app.yylg.tool.Utils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Eiffel on 2016/2/5.
 * 赠与金额时的填写记录，而GrantRecordBean是在赠与记录页使用
 * {@link com.shawnway.nav.app.yylg.adapter.GrantRecordAdapter.GrantRecordBean}
 *
 */
public class GrantHisBean {
    private JSONArray mHisJA;

    public JSONArray getHis(Context context) throws JSONException {
        if(mHisJA==null) {
            String jaStr=(String) Utils.getParam(context, "grant_history", new JSONArray().toString());
            Log.d("GrantHistBean","history  ->"+jaStr);
            mHisJA = new JSONArray(jaStr);
        }
        return mHisJA;
    }

    public void saveHist(Context context,JSONArray array){
        try {
            Utils.setParam(context,"grant_history",new JSONArray(array.toString()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
