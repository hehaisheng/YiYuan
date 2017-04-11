package com.shawnway.nav.app.yylg.tool;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class TimeUtils {

    public TimeUtils(){}
    public TimeUtils(Context context){
        requestCurrentTime(context);
    }
    public static long dataOne(String time) {
        //2016-05-03T17:47:40.000+08:00
        time = time.trim();
        String[] strs = time.split("T");
        String[] strings = strs[1].split("'+'");
        time = strs[0].concat(" ").concat(strings[0]);
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",
                Locale.CHINA);
        Date date;
        long times = 0;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = Long.valueOf(stf);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    long time = 0l;
    private boolean locked;
    public void lock(){locked = true;}
    public void unLock(){locked = false;}

    private void requestCurrentTime(Context context) {
        lock();
        VolleyTool.getInstance(context).sendGsonRequest(context, TimeWrapperResponseWrapper.class, Constants.GETCURRENTTIME, new Response.Listener<TimeWrapperResponseWrapper>() {
            @Override
            public void onResponse(TimeWrapperResponseWrapper response) {
                time = response.getBody().nowMillis;
                Log.d("TimeUtils", "获取的服务器的当前时间：" + time);
                unLock();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    class TimeWrapperResponseWrapper extends ResponseGson<TimeBody> {

    }

    class TimeBody{
        public long nowMillis;
    }
}
