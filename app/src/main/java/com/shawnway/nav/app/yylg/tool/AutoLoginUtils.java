package com.shawnway.nav.app.yylg.tool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by 裘 on 2016/10/31 0031.
 */

public class AutoLoginUtils {

    public static void startPollingService(Context context, Class<?> cls, String action) {

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //触发起始时间
        long triggerAtTime = SystemClock.elapsedRealtime();

        //使用AlarmManager的setRepeating方法设置定期执行的时间间隔和需要执行的service
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
                60 * 1000 * 10, pendingIntent);

    }

    public static void stopPollingService(Context context, Class<?> cls, String action) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }
}
